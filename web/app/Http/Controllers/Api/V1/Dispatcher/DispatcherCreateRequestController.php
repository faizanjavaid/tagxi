<?php

namespace App\Http\Controllers\Api\V1\Dispatcher;

use Ramsey\Uuid\Uuid;
use App\Jobs\NotifyViaMqtt;
use App\Models\Admin\Driver;
use App\Jobs\NotifyViaSocket;
use App\Models\Admin\ZoneType;
use App\Models\Request\Request;
use Illuminate\Support\Facades\DB;
use App\Models\Request\RequestMeta;
use Illuminate\Support\Facades\Log;
use App\Base\Constants\Masters\PushEnums;
use App\Http\Controllers\Api\V1\BaseController;
use App\Http\Requests\Request\CreateTripRequest;
use App\Jobs\Notifications\AndroidPushNotification;
use App\Transformers\Requests\TripRequestTransformer;

/**
 * @group Dispatcher-trips-apis
 *
 * APIs for Dispatcher-trips apis
 */
class DispatcherCreateRequestController extends BaseController
{
    protected $request;

    public function __construct(Request $request)
    {
        $this->request = $request;
    }
    /**
    * Create Request
    * @bodyParam pick_lat double required pikup lat of the user
    * @bodyParam pick_lng double required pikup lng of the user
    * @bodyParam drop_lat double required drop lat of the user
    * @bodyParam drop_lng double required drop lng of the user
    * @bodyParam vehicle_type string required id of zone_type_id
    * @bodyParam payment_opt tinyInteger required type of ride whther cash or card, wallet('0 => card,1 => cash,2 => wallet)
    * @bodyParam pick_address string required pickup address of the trip request
    * @bodyParam drop_address string required drop address of the trip request
    * @responseFile responses/requests/create-request.json
    *
    */
    public function createRequest(CreateTripRequest $request)
    {
        /**
        * Validate payment option is available.
        * if card payment choosen, then we need to check if the user has added thier card.
        * if the paymenr opt is wallet, need to check the if the wallet has enough money to make the trip request
        * Check if thge user created a trip and waiting for a driver to accept. if it is we need to cancel the exists trip and create new one
        * Find the zone using the pickup coordinates & get the nearest drivers
        * create request along with place details
        * assing driver to the trip depends the assignment method
        * send emails and sms & push notifications to the user& drivers as well.
        */

        // Validate payment option is available.
        // @TODO
        // get type id
        $zone_type_detail = ZoneType::where('id', $request->vehicle_type)->first();
        $type_id = $zone_type_detail->type_id;

        // Get currency code of Request
        $service_location = $zone_type_detail->zone->serviceLocation;
        $currency_code = $service_location->currency_symbol;
        //Find the zone using the pickup coordinates & get the nearest drivers
        // $nearest_drivers =  $this->getDrivers($request, $type_id);
        $nearest_drivers =  $this->getFirebaseDrivers($request, $type_id);


        // fetch unit from zone
        $unit = $zone_type_detail->zone->unit;
        // Fetch user detail
        $user_detail = auth()->user();
        // Get last request's request_number
        $request_number = $this->request->orderBy('updated_at', 'DESC')->pluck('request_number')->first();
        if ($request_number) {
            $request_number = explode('_', $request_number);
            $request_number = $request_number[1]?:000000;
        } else {
            $request_number = 000000;
        }
        // Generate request number
        $request_number = 'REQ_'.sprintf("%06d", $request_number+1);

        $request_params = [
            'request_number'=>$request_number,
            'zone_type_id'=>$request->vehicle_type,
            'if_dispatch'=>true,
            'dispatcher_id'=>$user_detail->admin->id,
            'payment_opt'=>$request->payment_opt,
            'unit'=>$unit,
            'requested_currency_code'=>$currency_code,
            'service_location_id'=>$service_location->id];
        // store request details to db
        // DB::beginTransaction();
        // try {
        $request_detail = $this->request->create($request_params);
        // request place detail params
        $request_place_params = [
            'pick_lat'=>$request->pick_lat,
            'pick_lng'=>$request->pick_lng,
            'drop_lat'=>$request->drop_lat,
            'drop_lng'=>$request->drop_lng,
            'pick_address'=>$request->pick_address,
            'drop_address'=>$request->drop_address];
        // store request place details
        $request_detail->requestPlace()->create($request_place_params);
        $ad_hoc_user_params = $request->only(['customer_name','phone_number']);
        // Store ad hoc user detail of this request
        $request_detail->adHocuserDetail()->create($ad_hoc_user_params);

        $selected_drivers = [];
        $notification_android = [];
        $notification_ios = [];
        $i = 0;
        foreach ($nearest_drivers as $driver) {
            // $selected_drivers[$i]["request_id"] = $request_detail->id;
            $selected_drivers[$i]["driver_id"] = $driver->id;
            $selected_drivers[$i]["active"] = $i == 0 ? 1 : 0;
            $selected_drivers[$i]["assign_method"] = 1;
            $selected_drivers[$i]["created_at"] = date('Y-m-d H:i:s');
            $selected_drivers[$i]["updated_at"] = date('Y-m-d H:i:s');
            if ($i == 0) {
                if ($driver->user->login_by == 1) {
                    $notification_android[] = $driver->user->device_token;
                } else {
                    $notification_ios[] = $driver->user->device_token;
                }
            }
            $i++;
        }

        // Send notification to the very first driver
        $first_meta_driver = $selected_drivers[0]['driver_id'];
        $request_result =  fractal($request_detail, new TripRequestTransformer)->parseIncludes('userDetail');

        $socket_data = new \stdClass();
        $socket_data->success = true;
        $socket_data->success_message  = PushEnums::REQUEST_CREATED;
        $socket_data->result = $request_result;

        $driver = Driver::find($first_meta_driver);
        // Form a socket sturcture using users'id and message with event name
        $socket_message = structure_for_socket($driver->id, 'driver', $socket_data, 'create_request');

        dispatch(new NotifyViaSocket('transfer_msg', $socket_message));

        dispatch(new NotifyViaMqtt('create_request_'.$driver->id, json_encode($socket_data), $driver->id));

        foreach ($selected_drivers as $key => $selected_driver) {
            $request_detail->requestMeta()->create($selected_driver);
        }
        // @TODO send sms & email to the user
        // } catch (\Exception $e) {
        //     DB::rollBack();
        //     Log::error($e);
        //     Log::error('Error while Create new request. Input params : ' . json_encode($request->all()));
        //     return $this->respondBadRequest('Unknown error occurred. Please try again later or contact us if it continues.');
        // }
        DB::commit();

        return $this->respondSuccess($request_result);
    }


    /**
    * Get nearest Drivers using requested co-ordinates
    *  @param request
    */
    public function getDrivers($request, $type_id)
    {
        $driver_detail = [];
        $driver_ids = [];


        $pick_lat = $request->pick_lat;
        $pick_lng = $request->pick_lng;
        $driver_search_radius = get_settings('driver_search_radius')?:30;

        $haversine = "(6371 * acos(cos(radians($pick_lat)) * cos(radians(pick_lat)) * cos(radians(pick_lng) - radians($pick_lng)) + sin(radians($pick_lat)) * sin(radians(pick_lat))))";

        // Get Drivers who are all going to accept or reject the some request that nears the user's current location.

        $driver_ids = Driver::whereHas('requestDetail.requestPlace', function ($query) use ($haversine,$driver_search_radius) {
            $query->select('request_places.*')->selectRaw("{$haversine} AS distance")
                ->whereRaw("{$haversine} < ?", [$driver_search_radius]);
        })->pluck('id')->toArray();

        $meta_drivers = RequestMeta::whereIn('driver_id', $driver_ids)->pluck('driver_id')->toArray();

        $driver_haversine = "(6371 * acos(cos(radians($pick_lat)) * cos(radians(latitude)) * cos(radians(longitude) - radians($pick_lng)) + sin(radians($pick_lat)) * sin(radians(latitude))))";
        // get nearest driver exclude who are all struck with request meta
        $drivers = Driver::whereHas('driverDetail', function ($query) use ($driver_haversine,$driver_search_radius,$type_id) {
            $query->select('driver_details.*')->selectRaw("{$driver_haversine} AS distance")
                ->whereRaw("{$driver_haversine} < ?", [$driver_search_radius]);
        })->whereNotIn('id', $meta_drivers)->limit(10)->get();

        if ($drivers->isEmpty()) {
            $this->throwCustomException('all drivers are busy');
        }
        return $drivers;
    }

    /**
    * Get Drivers from firebase
    */
    public function getFirebaseDrivers($request, $type_id)
    {
        $pick_lat = $request->pick_lat;
        $pick_lng = $request->pick_lng;

        // NEW flow
        $client = new \GuzzleHttp\Client();
        $url = env('NODE_APP_URL').':'.env('NODE_APP_PORT').'/'.$pick_lat.'/'.$pick_lng.'/'.$type_id;

        $res = $client->request('GET', "$url");
        if ($res->getStatusCode() == 200) {
            $fire_drivers = \GuzzleHttp\json_decode($res->getBody()->getContents());
            if (empty($fire_drivers->data)) {
                $this->throwCustomException('no drivers available');
            } else {
                $nearest_driver_ids = [];
                foreach ($fire_drivers->data as $key => $fire_driver) {
                    $nearest_driver_ids[] = $fire_driver->id;
                }

                $driver_search_radius = get_settings('driver_search_radius')?:30;

                $haversine = "(6371 * acos(cos(radians($pick_lat)) * cos(radians(pick_lat)) * cos(radians(pick_lng) - radians($pick_lng)) + sin(radians($pick_lat)) * sin(radians(pick_lat))))";
                // Get Drivers who are all going to accept or reject the some request that nears the user's current location.
                $meta_drivers = RequestMeta::whereHas('request.requestPlace', function ($query) use ($haversine,$driver_search_radius) {
                    $query->select('request_places.*')->selectRaw("{$haversine} AS distance")
                ->whereRaw("{$haversine} < ?", [$driver_search_radius]);
                })->pluck('driver_id')->toArray();

                $nearest_drivers = Driver::where('active', 1)->where('approve', 1)->where('available', 1)->where('vehicle_type', $type_id)->whereIn('id', $nearest_driver_ids)->whereNotIn('id', $meta_drivers)->limit(10)->get();

                if ($nearest_drivers->isEmpty()) {
                    $this->throwCustomException('all drivers are busy');
                }

                return $nearest_drivers;
            }
        } else {
            $this->throwCustomException('there is an error-getting-drivers');
        }
    }
}

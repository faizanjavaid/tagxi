<?php

namespace App\Http\Controllers\Api\V1\Auth\Registration;

use App\Models\User;
use App\Models\Country;
use App\Models\Admin\Driver;
use Illuminate\Http\Request;
use Kreait\Firebase\Database;
use App\Base\Constants\Auth\Role;
use Illuminate\Support\Facades\DB;
use App\Events\Auth\UserRegistered;
use Illuminate\Support\Facades\Log;
use App\Models\Admin\ServiceLocation;
use App\Base\Constants\Masters\WalletRemarks;
use App\Http\Controllers\Api\V1\BaseController;
use App\Jobs\Notifications\AndroidPushNotification;
use App\Base\Services\OTP\Handler\OTPHandlerContract;
use App\Http\Controllers\Api\V1\Auth\LoginController;
use App\Http\Requests\Auth\Registration\DriverRegistrationRequest;
use App\Jobs\Notifications\Auth\Registration\UserRegistrationNotification;
use App\Base\Services\ImageUploader\ImageUploaderContract;

/**
 * @group SignUp-And-Otp-Validation
 *
 * APIs for Driver Register
 */
class DriverSignupController extends LoginController
{
    protected $user;
    protected $driver;
    protected $otpHandler;
    protected $country;
    protected $database;
    protected $imageUploader;



    public function __construct(User $user, Driver $driver, Country $country, OTPHandlerContract $otpHandler, Database $database,ImageUploaderContract $imageUploader)
    {
        $this->user = $user;
        $this->driver = $driver;
        $this->otpHandler = $otpHandler;
        $this->country = $country;
        $this->database = $database;
        $this->imageUploader = $imageUploader;
    }

    /**
    * Register the driver and send welcome email.
    * @bodyParam name string required name of the driver
    * @bodyParam company_key string optional company key of demo
    * @bodyParam mobile integer required mobile of driver
    * @bodyParam email email required email of the driver
    * @bodyParam device_token string required device_token of the driver
    * @bodyParam terms_condition boolean required it should be 0 or 1
    * @bodyParam service_location_id uuid required service location of the driver. it can be listed from service location list apis
     * @bodyParam refferal_code string optional refferal_code of the another driver
    * @bodyParam login_by tinyInt required from which device the driver registered
    * @bodyParam is_company_driver tinyInt required value can be 0 or 1.
    * @bodyParam vehicle_type uuid required vehicle types. listed by types api
    * @bodyParam car_make string required car make of the driver
    * @bodyParam car_model string required car model of the driver
    * @bodyParam car_number string required car number of the driver
    * @bodyParam car_color string required car color of the driver
    *
    * @param \App\Http\Requests\Auth\Registration\UserRegistrationRequest $request
    * @return \Illuminate\Http\JsonResponse
    * @responseFile responses/auth/register.json
    */

    public function register(DriverRegistrationRequest $request)
    {
        $mobileUuid = $request->input('uuid');


            $created_params = $request->only(['service_location_id', 'name','mobile','email','address','state','city','country','gender','vehicle_type','car_make','car_model','car_color','car_number','vehicle_year']);

        $created_params['postal_code'] = $request->postal_code;

        if ($request->input('service_location_id')) {
            $timezone = ServiceLocation::where('id', $request->input('service_location_id'))->pluck('timezone')->first();
        } else {
            $timezone = env('SYSTEM_DEFAULT_TIMEZONE');
        }

        $country_code = $this->country->where('dial_code', $request->input('country'))->exists();

        $validate_exists_email = $this->user->belongsTorole([Role::DRIVER,Role::OWNER])->where('email', $request->email)->exists();

        if ($validate_exists_email) {
            $this->throwCustomException('Provided email has already been taken');
        }

        // $mobile = $this->otpHandler->getMobileFromUuid($mobileUuid);
        $mobile = $request->mobile;

        $validate_exists_mobile = $this->user->belongsTorole([Role::DRIVER,Role::OWNER])->where('mobile', $mobile)->exists();

        if ($validate_exists_mobile) {
            $this->throwCustomException('Provided mobile has already been taken');
        }
        
        if (!$country_code) {
            $this->throwCustomException('unable to find country');
        }
        $country_id = $this->country->where('dial_code', $request->input('country'))->pluck('id')->first();

        $created_params['country'] = $country_id;

        $profile_picture = null;

        if ($uploadedFile = $this->getValidatedUpload('profile_picture', $request)) {
            $profile_picture = $this->imageUploader->file($uploadedFile)
                ->saveProfilePicture();
        }
        DB::beginTransaction();
        try {

        $data = [
            'name' => $request->input('name'),
            'email' => $request->input('email'),
            'password' => bcrypt($request->input('password')),
            'mobile' => $mobile,
            'mobile_confirmed' => true,
            'fcm_token'=>$request->input('device_token'),
            'login_by'=>$request->input('login_by'),
            'timezone'=>$timezone,
            'country'=>$country_id,
            'profile_picture'=>$profile_picture,
            'refferal_code'=>str_random(6),
            'lang'=>$request->input('lang')
        ];
        // DB::enableQueryLog();
        if (env('APP_FOR')=='demo' && $request->has('company_key') && $request->input('company_key')) {
            $data['company_key'] = $request->input('company_key');
        }
        $user = $this->user->create($data);
        // dd($user);
        // dd(DB::getQueryLog());
        $created_params['mobile'] = $mobile;
        $created_params['uuid'] = driver_uuid();
        $created_params['active'] = false; //@TODO need to remove in future
            $driver = $user->driver()->create($created_params); //Create drivers table data

        // // Store records to firebase
        $this->database->getReference('drivers/'.$driver->id)->set(['id'=>$driver->id,'vehicle_type'=>$request->input('vehicle_type'),'active'=>1,'updated_at'=> Database::SERVER_TIMESTAMP]);

        $driver_detail_data = $request->only(['is_company_driver','company']);
        $driver_detail = $driver->driverDetail()->create($driver_detail_data);//create driver details table data

        // Create Empty Wallet to the driver
        $driver_wallet = $driver->driverWallet()->create(['amount_added'=>0]);

        $user->attachRole(Role::DRIVER);
        // $this->dispatch(new UserRegistrationNotification($user));
        event(new UserRegistered($user));
        } catch (\Exception $e) {
            DB::rollBack();
            Log::error('Error while Registering a driver account. Input params : ' . json_encode($request->all()));
            return $this->respondBadRequest('Unknown error occurred. Please try again later or contact us if it continues.');
        }
        DB::commit();
        return $this->authenticateAndRespond($user, $request, $needsToken=true);
    }

    /**
    * Validate Mobile-For-Driver
    * @bodyParam mobile integer required mobile of driver
     * @response {
     * "success":true,
     * "message":"mobile_validated",
     * }
    *
    */
    public function validateDriverMobile(Request $request)
    {
        $mobile = $request->mobile;


        $validate_exists_mobile = $this->user->belongsTorole(Role::DRIVER)->where('mobile', $mobile)->exists();

        if($request->has('role') && $request->role=='driver'){

            $validate_exists_mobile = $this->user->belongsTorole(Role::DRIVER)->where('mobile', $mobile)->exists();
        
        }
        if($request->has('role') && $request->role=='owner'){

             $validate_exists_mobile = $this->user->belongsTorole(Role::OWNER)->where('mobile', $mobile)->exists();
        }


        if($request->has('email')){

        $validate_exists_email = $this->user->belongsTorole(Role::DRIVER)->where('email', $request->email)->exists();

        if($request->has('role')&& $request->role=='driver'){

            $validate_exists_email = $this->user->belongsTorole(Role::DRIVER)->where('email', $request->email)->exists();
        }
        if($request->has('role')&& $request->role=='owner'){

            $validate_exists_email = $this->user->belongsTorole(Role::OWNER)->where('email', $request->email)->exists();
        }

        if ($validate_exists_email) {
            $this->throwCustomException('Provided email has already been taken');
        }
        
        }

        if ($validate_exists_mobile) {
            $this->throwCustomException('Provided mobile has already been taken');
        }

        return $this->respondSuccess(null, 'mobile_validated');
    }
   
    /**
    * Validate Mobile-For-Driver-For-Login
    * @bodyParam mobile integer required mobile of driver
     * @response {
     * "success":true,
     * "message":"mobile_exists",
     * }
     */
    public function validateDriverMobileForLogin(Request $request)
    {
        $mobile = $request->mobile;

        $validate_exists_mobile = $this->user->belongsTorole(Role::DRIVER)->where('mobile', $mobile)->exists();

        if($request->has('role') && $request->role=='driver'){

        $validate_exists_mobile = $this->user->belongsTorole(Role::DRIVER)->where('mobile', $mobile)->exists();

        }
        if($request->has('role') && $request->role=='owner'){

            $validate_exists_mobile = $this->user->belongsTorole(Role::OWNER)->where('mobile', $mobile)->exists();
        }   

        if ($validate_exists_mobile) {
            return $this->respondSuccess(null, 'mobile_exists');
        }

        return $this->respondFailed('mobile_does_not_exists');
    }


    /**
    * Add Commission to the referred driver
    *
    */
    public function addCommissionToRefferedUser($reffered_user)
    {
        $driver_wallet = $reffered_user->driverWallet;
        $referral_commision = get_settings('referral_commision_for_driver')?:0;

        $driver_wallet->amount_added += $referral_commision;
        $driver_wallet->amount_balance += $referral_commision;
        $driver_wallet->save();

        // Add the history
        $reffered_user->driverWalletHistory()->create([
            'amount'=>$referral_commision,
            'transaction_id'=>str_random(6),
            'remarks'=>WalletRemarks::REFERRAL_COMMISION,
            'refferal_code'=>$reffered_user->refferal_code,
            'is_credit'=>true]);

        // Notify user
        $title = trans('push_notifications.referral_earnings_notify_title');
        $body = trans('push_notifications.referral_earnings_notify_body');

        $reffered_user->user->notify(new AndroidPushNotification($title, $body));
    }


    /**
     * Owner Register
     * @bodyParam name string required name of the owner
     * @bodyParam company_name string required name of the the company
     * @bodyParam address string required address of the the company
     * @bodyParam city string required city of the the company
     * @bodyParam tax_number string required tax_number of the the company
     * @bodyParam country string required country dial code of the the company
     * @bodyParam postal_code string required postal_code of the the company
     * @bodyParam mobile integer required mobile of owner
     * @bodyParam email email required email of the owner
     * @bodyParam device_token string required device_token of the owner
     * @bodyParam service_location_id uuid required service location of the owner. it can be listed from service location list apis
     * @bodyParam login_by tinyInt required from which device the owner registered
     * @return \Illuminate\Http\JsonResponse
     * @responseFile responses/auth/register.json
     * 
     * */
    public function ownerRegister(Request $request){

        $request->validate([
            'company_name' => 'required|unique:owners,company_name,NULL,id,deleted_at,NULL',
            'name' => 'required',
            'email' => 'required|email|unique:users,email',
            'mobile'=>'required|unique:users,mobile',
            'address'=>'required|min:10',
            'postal_code'=>'required|numeric',
            'city'=>'required',
            'service_location_id' => 'sometimes|required',
            'tax_number' => 'required',
            'device_token'=>'required',
            'login_by'=>'required|in:android,ios',
            'country'=>'required|exists:countries,dial_code',
        ]);


         $created_params = $request->only(['service_location_id','company_name','owner_name','mobile','email','address','postal_code','city','tax_number','name']);

         $created_params['owner_name'] = $request->name;

         $created_params['approve'] = false;

        if ($request->input('service_location_id')) {
            $timezone = ServiceLocation::where('id', $request->input('service_location_id'))->pluck('timezone')->first();
        } else {
            $timezone = env('SYSTEM_DEFAULT_TIMEZONE');
        }

        $country_id = $this->country->where('dial_code', $request->input('country'))->pluck('id')->first();

        $profile_picture = null;

        if ($uploadedFile = $this->getValidatedUpload('profile', $request)) {
            $profile_picture = $this->imageUploader->file($uploadedFile)
                ->saveDriverProfilePicture();
        }

        $validate_exists_email = $this->user->belongsTorole([Role::DRIVER,Role::OWNER])->where('email', $request->email)->exists();

        if ($validate_exists_email) {
            $this->throwCustomException('Provided email has already been taken');
        }

        // $mobile = $this->otpHandler->getMobileFromUuid($mobileUuid);
        $mobile = $request->mobile;

        $validate_exists_mobile = $this->user->belongsTorole([Role::DRIVER,Role::OWNER])->where('mobile', $mobile)->exists();

        if ($validate_exists_mobile) {
            $this->throwCustomException('Provided mobile has already been taken');
        }

        $data = [
            'name' => $request->input('name'),
            'email' => $request->input('email'),
            'mobile' => $mobile,
            'mobile_confirmed' => true,
            'fcm_token'=>$request->input('device_token'),
            'login_by'=>$request->input('login_by'),
            'timezone'=>$timezone,
            'country'=>$country_id,
            'profile_picture'=>$profile_picture,
            'refferal_code'=>str_random(6),
        ];

        DB::beginTransaction();
        try {

        $user = $this->user->create($data);

        $user->attachRole(Role::OWNER);

        $owner = $user->owner()->create($created_params);

        $owner_wallet = $owner->ownerWalletDetail()->create(['amount_added'=>0]);
        

        $this->database->getReference('owners/'.$owner->id)->set(['id'=>$owner->id,'active'=>1,'updated_at'=> Database::SERVER_TIMESTAMP]);


        } catch (\Exception $e) {
            DB::rollBack();
            Log::error($e);
            Log::error('Error while Registering a owner account. Input params : ' . json_encode($request->all()));
            return $this->respondBadRequest('Unknown error occurred. Please try again later or contact us if it continues.');
        }
        DB::commit();
        return $this->authenticateAndRespond($user, $request, $needsToken=true);

    }
}

<?php

namespace App\Transformers\Dispatcher;

use App\Models\User;
use App\Base\Constants\Auth\Role;
use App\Transformers\Transformer;
use App\Transformers\Access\RoleTransformer;
use App\Transformers\Requests\TripRequestTransformer;
use App\Models\Admin\Sos;
use App\Transformers\Common\SosTransformer;
use App\Transformers\User\FavouriteLocationsTransformer;
use App\Base\Constants\Setting\Settings;
use Carbon\Carbon;
use App\Transformers\Requests\UserRequestForDispatcherAppTransformer;

class UserForDispatcherRideTransformer extends Transformer
{
    /**
     * Resources that can be included if requested.
     *
     * @var array
     */
    protected $availableIncludes = [
        'tripRequestHistory'
    ];
    /**
     * Resources that can be included default.
     *
     * @var array
     */
    protected $defaultIncludes = [
        'tripRequestHistory'
    ];

    /**
     * A Fractal transformer.
     *
     * @return array
     */
    public function transform(User $user)
    {
        $params = [
            'id' => $user->id,
            'name' => $user->name,
            'last_name' => $user->last_name,
            'username' => $user->username,
            'email' => $user->email,
            'mobile' => $user->countryDetail->dial_code.$user->mobile,
            'profile_picture' => $user->profile_picture,
            'active' => $user->active,
            'email_confirmed' => $user->email_confirmed,
            'mobile_confirmed' => $user->mobile_confirmed,
            'last_known_ip' => $user->last_known_ip,
            'last_login_at' => $user->last_login_at,
            'rating' => round($user->rating, 2),
            'no_of_ratings' => $user->no_of_ratings,
            'refferal_code'=>$user->refferal_code,
            'currency_code'=>get_settings('currency_code'),
            'currency_symbol'=>get_settings('currency_symbol'),
        ];


        return $params;
    }

    
    /**
     * Include the request of the user.
     *
     * @param User $user
     * @return \League\Fractal\Resource\Collection|\League\Fractal\Resource\NullResource
     */
    public function includeTripRequestHistory(User $user)
    {
        $request = $user->requestDetail()->orderBy('created_at', 'desc')->where('is_completed',true)->get();

        return $request
        ? $this->collection($request, new UserRequestForDispatcherAppTransformer)
        : $this->null();
    }
     
}

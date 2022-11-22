<?php

/*
|--------------------------------------------------------------------------
| SPA Auth Routes
|--------------------------------------------------------------------------
|
| These routes are prefixed with '/'.
| These routes use the root namespace 'App\Http\Controllers\Web'.
|
 */

use App\Base\Constants\Auth\Role;

/*
 * These routes are used for web authentication.
 *
 * Route prefix 'api/spa'.
 * Root namespace 'App\Http\Controllers\Web\Admin'.
 */

/**
 * Temporary dummy route for testing SPA.
 */


Route::middleware('guest')->namespace('Admin')->group(function () {

    // Get admin-login form
    Route::get('login', 'AdminViewController@viewLogin');


    Route::get('company-login', 'FleetOwnerController@viewLogin');
   
    Route::get('login/{provider}', 'AdminViewController@redirectToProvider');

    Route::get('login/callback/{provider}', 'AdminViewController@handleProviderCallback');
});

Route::middleware('auth:web')->group(function () {

        //cMS
    Route::group(['prefix' => 'cms'], function () {
    
            Route::post('/frontpagecmsadd','FrontPageController@frontpageadd')->name('frontpagecmsadd');
            Route::get('/frontpagecms', 'FrontPageController@frontpage')->name('frontpagecms');
            Route::post('/safetypageadd','FrontPageController@safetypagecmsadd')->name('safetypageadd');
            Route::get('/safetypagecms', 'FrontPageController@safetypagecms')->name('safetypagecms');
            Route::post('/servicepageadd','FrontPageController@servicepagecmsadd')->name('servicepageadd');
            Route::get('/servicepage', 'FrontPageController@servicepagecms')->name('servicepage');
            Route::post('/privacypageadd','FrontPageController@privacypagecmsadd')->name('privacypageadd');
            Route::get('/privacypagecms', 'FrontPageController@privacypagecms')->name('privacypagecms');
            Route::post('/dmvpageadd','FrontPageController@dmvpagecmsadd')->name('dmvpageadd');
            Route::get('/dmvpagecms', 'FrontPageController@dmvpagecms')->name('dmvpagecms');
            Route::post('/complaincepageadd','FrontPageController@complaincepagecmsadd')->name('complaincepageadd');
            Route::get('/complaincepagecms', 'FrontPageController@complaincepagecms')->name('complaincepagecms');
            Route::post('/termspageadd','FrontPageController@termspagecmsadd')->name('termspageadd');
            Route::get('/termspagecms', 'FrontPageController@termspagecms')->name('termspagecms');
            Route::post('/drreqpageadd','FrontPageController@drreqpagecmsadd')->name('drreqpageadd');
            Route::get('/drreqpagecms', 'FrontPageController@drreqpagecms')->name('drreqpagecms');
            Route::post('/applydriverpageadd','FrontPageController@applydriverpagecmsadd')->name('applydriverpageadd');
            Route::get('/applydriverpagecms', 'FrontPageController@applydriverpagecms')->name('applydriverpagecms');
            Route::post('/howdriverpageadd','FrontPageController@howdriverpagecmsadd')->name('howdriverpageadd');
            Route::get('/howdriverpagecms', 'FrontPageController@howdriverpagecms')->name('howdriverpagecms');
            Route::post('/contactpageadd','FrontPageController@contactpagecmsadd')->name('contactpageadd');
            Route::get('/contactpagecms', 'FrontPageController@contactpagecms')->name('contactpagecms');
            Route::post('/playstorepageadd','FrontPageController@playstorepagecmsadd')->name('playstorepageadd');
            Route::get('/playstorepagecms', 'FrontPageController@playstorepagecms')->name('playstorepagecms');
            Route::post('/footerpageadd','FrontPageController@footerpagecmsadd')->name('footerpageadd');
            Route::get('/footerpagecms', 'FrontPageController@footerpagecms')->name('footerpagecms');
            Route::post('/colorthemepageadd','FrontPageController@colorthemepagecmsadd')->name('colorthemepageadd');
            Route::get('/colorthemepagecms', 'FrontPageController@colorthemepagecms')->name('colorthemepagecms');

    });

    Route::namespace('Admin')->group(function () {
        Route::get('dispatcher-request','AdminViewController@dispatchRequest');
    // Owner Management (Company Management)
    Route::group(['prefix' => 'owners'], function () {
        // Route::get('/', 'OwnerController@index')->name('ownerView');
        // Route::get('/fetch', 'OwnerController@getAllOwner');
        Route::get('by_area/{area}', 'OwnerController@index')->name('ownerByArea');
        Route::get('by_area/fetch/{area}', 'OwnerController@getAllOwner');
        Route::get('/create/{area}', 'OwnerController@create');
        Route::post('store', 'OwnerController@store');
        Route::get('/{owner}', 'OwnerController@getById');
        Route::post('update/{owner}', 'OwnerController@update');
        Route::get('toggle_status/{owner}', 'OwnerController@toggleStatus');
        Route::get('toggle_approve/{owner}', 'OwnerController@toggleApprove');
        Route::get('delete/{owner}', 'OwnerController@delete');
        Route::get('get/owner', 'OwnerController@getOwnerByArea')->name('getOwnerByArea');
        Route::get('document/view/{owner}', 'OwnerDocumentController@index')->name('ownerDocumentView');
        Route::get('upload/document/{owner}/{needed_document}', 'OwnerDocumentController@documentUploadView');
        Route::post('upload/document/{owner}/{needed_document}', 'OwnerDocumentController@uploadDocument')->name('updateOwnerDocument');
        Route::post('approve/documents', 'OwnerDocumentController@approveOwnerDocument')->name('approveOwnerDocument');
    });

    // Fleet CRUD
    Route::group(['prefix' => 'fleets'], function () {
        Route::get('/', 'FleetController@index')->name('viewFleet');
        Route::get('/fetch', 'FleetController@fetch')->name('fetchFleet');
        Route::get('/create', 'FleetController@create')->name('createFleet');
        Route::post('store', 'FleetController@store')->name('storeFleet');
        Route::get('edit/{fleet}', 'FleetController@getById')->name('editFleet');
        Route::post('update/{fleet}', 'FleetController@update')->name('updateFleet');
        Route::get('toggle_status/{fleet}', 'FleetController@toggleStatus')->name('toggleFleetStatus');
        Route::get('toggle_approve/{fleet}', 'FleetController@toggleApprove')->name('toggleFleetApprove');
        Route::get('delete/{fleet}', 'FleetController@delete')->name('deleteFleet');
        Route::post('update/decline/reason', 'FleetController@updateFleetDeclineReason')->name('updateFleetDeclineReason');
        Route::get('assign_driver/{fleet}', 'FleetController@assignDriverView')->name('assignFleetToDriverView');
        Route::post('assign_driver/{fleet}', 'FleetController@assignDriver')->name('assignFleetToDriver');
        Route::get('document/view/{fleet}', 'FleetDocumentController@index')->name('FleetDocumentView');
        Route::get('upload/document/{fleet}/{needed_document}', 'FleetDocumentController@documentUploadView');
        Route::post('upload/document/{fleet}/{needed_document}', 'FleetDocumentController@uploadDocument')->name('updateFleetDocument');
        Route::post('approve/documents', 'FleetDocumentController@approveFleetDocument')->name('approveFleetDocument');

    });

    // Driver Management
    Route::group(['prefix' => 'company/drivers','namespace'=>'Company'], function () {
        // prefix('drivers')->group(function () {
        Route::get('/', 'DriverController@index')->name('companyDriverView');
        Route::get('/fetch', 'DriverController@getAllDrivers');
        Route::get('/create', 'DriverController@create');
        Route::post('store', 'DriverController@store');
        Route::get('/{driver}', 'DriverController@getById');
        Route::post('update/{driver}', 'DriverController@update');
        Route::get('toggle_status/{driver}', 'DriverController@toggleStatus');
        Route::get('toggle_approve/{driver}', 'DriverController@toggleApprove');
        Route::get('toggle_available/{driver}', 'DriverController@toggleAvailable');
        Route::get('delete/{driver}', 'DriverController@delete');
        Route::get('document/view/{driver}', 'DriverDocumentController@index')->name('companyDriverDocumentView');
        Route::get('get/carmodel', 'DriverController@getCarModel')->name('getCarModel');
        Route::get('profile/{driver}', 'DriverController@profile');
        Route::get('hire/view', 'DriverController@hireDriverView')->name('hireDriverView');
        Route::post('hire', 'DriverController@hireDriver')->name('hireDriver');
        Route::get('vehicle/privileges/{driver}','DriverController@fleetPrivilegeView')->name('fleetPrivilegeView');
        Route::post('store/vehicle/privileges/{driver}','DriverController@storePrivilegedVehicle')->name('storePrivilegedVehicle');
        Route::get('unlink/fleet/{driver}/{vehicle}','DriverController@unlinkVehicle')->name('unlinkVehicle');
    });

});
});



Route::middleware('guest')->namespace('Dispatcher')->group(function () {
    // Get admin-login form
    Route::get('dispatch-login', 'DispatcherController@loginView');
});


Route::namespace('Admin')->group(function () {
    Route::get('track/request/{request}', 'AdminViewController@trackTripDetails');
});


Route::middleware('auth:web')->group(function () {
    Route::post('logout', function () {
        auth('web')->logout();
        request()->session()->invalidate();
        return redirect('login');
    });
    // Masters Crud
    Route::middleware(role_middleware(Role::webPanelLoginRoles()))->group(function () {
        /**
         * Vehicle Types
         */
        Route::namespace('Admin')->group(function () {
            Route::get('view-services', 'AdminViewController@viewServices');
            Route::prefix('types')->group(function () {
                Route::get('/', 'VehicleTypeController@index');
                Route::get('/fetch', 'VehicleTypeController@getAllTypes');
                Route::get('by/admin', 'VehicleTypeController@byAdmin');
                Route::get('/create', 'VehicleTypeController@create');
                Route::post('/store', 'VehicleTypeController@store');
                Route::get('edit/{id}', 'VehicleTypeController@edit');
                Route::post('/update/{vehicle_type}', 'VehicleTypeController@update');
                Route::get('toggle_status/{vehicle_type}', 'VehicleTypeController@toggleStatus');
                Route::get('/delete/{vehicle_type}', 'VehicleTypeController@delete');
            });
        });
    });

    Route::namespace('Admin')->group(function () {
        // Change Locale
        Route::get('/change/lang/{lang}', 'AdminViewController@changeLocale')->name('changeLocale');

        Route::get('dashboard', 'DashboardController@dashboard');
        // Route::get('dashboard', 'AdminViewController@dashboard');
        Route::get('/admin_dashboard', 'AdminViewController@viewTestDashboard')->name('admin_dashboard');
        Route::get('/driver_profile_dashboard', 'AdminViewController@driverPrfDashboard')->name('driver_profile_dashboard');
        Route::get('/driver_profile_dashboard_view/{driver}', 'AdminViewController@driverPrfDashboardView');

        Route::group(['prefix' => 'company',  'middleware' => 'permission:view-companies'], function () {
            // prefix('company')->group(function () {
            Route::get('/', 'CompanyController@index');
            Route::get('/fetch', 'CompanyController@getAllCompany');
            Route::get('by/admin', 'CompanyController@byAdmin');
            Route::get('/create', 'CompanyController@create');
            Route::post('store', 'CompanyController@store');
            Route::get('edit/{company}', 'CompanyController@getById');
            Route::post('update/{company}', 'CompanyController@update');
            Route::get('toggle_status/{company}', 'CompanyController@toggleStatus');
            Route::get('delete/{company}', 'CompanyController@delete');
        });

//drivers

    Route::group(['prefix' => 'drivers'], function () {
        // prefix('drivers')->group(function () {
        Route::get('/', 'DriverController@index');
        Route::get('/fetch/approved', 'DriverController@getApprovedDrivers');

        Route::get('/waiting-for-approval', 'DriverController@approvalPending');
        // Route::get('/fetch', 'DriverController@getAllDrivers');
        Route::get('/fetch/approval-pending-drivers', 'DriverController@getApprovalPendingDrivers');
        Route::get('/fetch/driver-ratings', 'DriverController@fetchDriverRatings');

        Route::get('/create', 'DriverController@create');
        Route::post('store', 'DriverController@store');
        Route::get('/{driver}', 'DriverController@getById');
        Route::get('request-list/{driver}', 'DriverController@DriverTripRequestIndex');
        Route::get('request-list/{driver}/fetch', 'DriverController@DriverTripRequest');
        Route::get('payment-history/{driver}', 'DriverController@DriverPaymentHistory');
        Route::post('payment-history/{driver}', 'DriverController@StoreDriverPaymentHistory');
        Route::post('update/{driver}', 'DriverController@update');
        Route::get('toggle_status/{driver}', 'DriverController@toggleStatus');
        Route::get('toggle_approve/{driver}/{approval_status}', 'DriverController@toggleApprove');
        Route::get('toggle_available/{driver}', 'DriverController@toggleAvailable');
        Route::get('delete/{driver}', 'DriverController@delete');
        Route::get('document/view/{driver}', 'DriverDocumentController@index');
        Route::get('upload/document/{driver}/{needed_document}', 'DriverDocumentController@documentUploadView');
        Route::post('upload/document/{driver}/{needed_document}', 'DriverDocumentController@uploadDocument');
        Route::post('approve/documents', 'DriverDocumentController@approveDriverDocument')->name('approveDriverDocument');
        Route::get('get/carmodel', 'DriverController@getCarModel')->name('getCarModel');
        Route::post('update/decline/reason', 'DriverController@UpdateDriverDeclineReason')->name('UpdateDriverDeclineReason');
       
        });

        Route::group(['prefix'=>'driver-ratings'], function () {
             Route::get('/','DriverController@driverRatings');
             Route::get('/view/{driver}','DriverController@driverRatingView');
        });
         Route::group(['prefix'=>'withdrawal-requests-lists'], function () {
             Route::get('/','DriverController@withdrawalRequestsList');
             Route::get('/view/{driver}','DriverController@withdrawalRequestDetail');
             Route::get('/approve/{wallet_withdrawal_request}','DriverController@approveWithdrawalRequest');
             Route::get('/decline/{wallet_withdrawal_request}','DriverController@declineWithdrawalRequest');
       Route::get('/negative_balance_drivers','DriverController@NeagtiveBalanceDrivers');
       Route::get('fetch/negative-balance-drivers', 'DriverController@NegativeBalanceFetch');
        });

//Fleet drivers

    Route::group(['prefix' => 'fleet-drivers'], function () {
        // prefix('drivers')->group(function () {
        Route::get('/', 'FleetDriverController@index');
        Route::get('/fetch/approved', 'FleetDriverController@getApprovedFleetDrivers');

        Route::get('/waiting-for-approval', 'FleetDriverController@approvalPending');
        // Route::get('/fetch', 'DriverController@getAllDrivers');
        Route::get('/fetch/approval-pending-drivers', 'FleetDriverController@getApprovalPendingFleetDrivers');
        Route::get('/fetch/driver-ratings', 'FleetDriverController@fetchFleetDriverRatings');

        Route::get('/create', 'FleetDriverController@create');
        Route::post('store', 'FleetDriverController@store');
        Route::get('/{driver}', 'FleetDriverController@getById');
        Route::get('request-list/{driver}', 'FleetDriverController@DriverTripRequestIndex');
        Route::get('request-list/{driver}/fetch', 'FleetDriverController@FleetDriverTripRequest');
        Route::get('payment-history/{driver}', 'FleetDriverController@FleetDriverPaymentHistory');
        Route::post('payment-history/{driver}', 'FleetDriverController@StoreFleetDriverPaymentHistory');
        Route::post('update/{driver}', 'FleetDriverController@update');
        Route::get('toggle_status/{driver}', 'FleetDriverController@toggleStatus');
        Route::get('toggle_approve/{driver}/{approval_status}', 'FleetDriverController@toggleApprove');
        Route::get('toggle_available/{driver}', 'FleetDriverController@toggleAvailable');
        Route::get('delete/{driver}', 'FleetDriverController@delete');
        Route::get('document/view/{driver}', 'FleetDriverDocumentController@index');
        Route::get('upload/document/{driver}/{needed_document}', 'FleetDriverDocumentController@documentUploadView');
        Route::post('upload/document/{driver}/{needed_document}', 'FleetDriverDocumentController@uploadDocument');
        Route::post('approve/documents', 'FleetDriverDocumentController@approveFleetDriverDocument')->name('approveFleetDriverDocument');
        Route::get('get/carmodel', 'FleetDriverController@getCarModel')->name('getCarModel');
        Route::post('update/decline/reason', 'FleetDriverController@UpdateDriverDeclineReason')->name('UpdateFleetDriverDeclineReason');
       
        });
        Route::group(['prefix' => 'admins',  'middleware' => 'permission:admin'], function () {
            // prefix('admins')->group(function () {
            Route::get('/', 'AdminController@index');
            Route::get('/fetch', 'AdminController@getAllAdmin');
            Route::get('/create', 'AdminController@create');
            Route::post('store', 'AdminController@store');
            Route::get('edit/{admin}', 'AdminController@getById');
            Route::post('update/{admin}', 'AdminController@update');
            Route::get('toggle_status/{user}', 'AdminController@toggleStatus');
            Route::get('delete/{user}', 'AdminController@delete');
            Route::get('profile/{user}', 'AdminController@viewProfile');
            Route::post('profile/update/{user}', 'AdminController@updateProfile');
        });
        // Zone CRUD
        Route::group(['prefix' => 'zone',  'middleware' => 'permission:view-zone'], function () {
            // prefix('zone')->group(function () {
            Route::get('/', 'ZoneController@index');
            Route::get('/fetch', 'ZoneController@getAllZone');
            Route::get('/mapview/{id}', 'ZoneController@zoneMapView');
            Route::get('/create', 'ZoneController@create');
            Route::get('/edit/{id}', 'ZoneController@zoneEdit');
            Route::post('update/{zone}', 'ZoneController@update');
            Route::get('/assigned/types/{zone}', 'ZoneController@assignTypesView');
            Route::get('/assign/types/{zone}', 'ZoneController@assignTypesCreateView');
            Route::post('/assign/types/{zone}', 'ZoneController@assignTypesStore');
            Route::get('/types/edit/{zone_type}', 'ZoneController@typesEditPriceView');
            Route::post('/types/edit/{zone_type}', 'ZoneController@typesPriceUpdate')->name('updateTypePrice');
            Route::post('store', 'ZoneController@store');
            Route::get('/{id}', 'ZoneController@getById');
            Route::get('/delete/{zone}', 'ZoneController@delete');
            Route::get('/toggle_status/{zone}', 'ZoneController@toggleZoneStatus');
            Route::get('/types/toggleStatus/{zone_type}', 'ZoneController@toggleStatus');
            Route::get('/types/delete/{zone_type}', 'ZoneController@deleteZoneType');
            Route::get('/surge/{zone}', 'ZoneController@surgeView');
            Route::post('/surge/update/{zone}', 'ZoneController@updateSurgePrice');
            Route::get('/set/default/{zone_type}', 'ZoneController@setDefaultType');
            Route::get('/coords/by_keyword/{keyword}', 'ZoneController@getCoordsByKeyword')->name('getCoordsByKeyword');
            Route::get('/search/city', 'ZoneController@getCityBySearch')->name('getCityBySearch');

             Route::get('/types/zone_package_price/index/{zone_type}', 'ZoneController@packageIndex');
             
             Route::get('/types/zone_package_price/{zone_type}', 'ZoneController@packageCreate');
             Route::post('/types/zone_package_price/store/{zone_type}', 'ZoneController@packageStore');
             Route::get('/types/zone_package_price/edit/{package}', 'ZoneController@packageEdit');
             Route::post('/types/zone_package_price/update/{package}', 'ZoneController@packageUpdate');
             Route::get('/types/zone_package_price/delete/{package}', 'ZoneController@packageDelete');
             Route::get('/types/zone_package_price/toggleStatus/{package}', 'ZoneController@PackagetoggleStatus');
        });

                // Zone CRUD
        Route::group(['prefix' => 'airport',  'middleware' => 'permission:list-airports'], function () {
            Route::get('/', 'AirportController@index');
            Route::get('/fetch', 'AirportController@getAllAirports');
            Route::get('/mapview/{id}', 'AirportController@airportMapView');
            Route::get('/create', 'AirportController@create');
            Route::get('/edit/{id}', 'AirportController@airportEdit');
            Route::post('update/{airport}', 'AirportController@update');
            Route::post('store', 'AirportController@store');
            Route::get('/{id}', 'AirportController@getById');
            Route::get('/delete/{airport}', 'AirportController@delete');
            Route::get('/toggle_status/{airport}', 'AirportController@toggleAirportStatus');
        });

        Route::group(['prefix' => 'users',  'middleware' => 'permission:user-menu'], function () {
            // prefix('users')->group(function () {
            Route::get('/', 'UserController@index');
            Route::get('/fetch', 'UserController@getAllUser');
            Route::get('/create', 'UserController@create');
            Route::post('store', 'UserController@store');
            Route::get('edit/{user}', 'UserController@getById');
            Route::post('update/{user}', 'UserController@update');
            Route::get('toggle_status/{user}', 'UserController@toggleStatus');
            Route::get('delete/{user}', 'UserController@delete');
            Route::get('/request-list/{user}', 'UserController@UserTripRequest');
            Route::get('payment-history/{user}', 'UserController@userPaymentHistory');
            Route::post('payment-history/{user}', 'UserController@StoreUserPaymentHistory');      




        });

        Route::group(['prefix' => 'sos',  'middleware' => 'permission:view-sos'], function () {
            // prefix('sos')->group(function () {
            Route::get('/', 'SosController@index');
            Route::get('/fetch', 'SosController@getAllSos');
            Route::get('/create', 'SosController@create');
            Route::post('store', 'SosController@store');
            Route::get('/{sos}', 'SosController@getById');
            Route::post('update/{sos}', 'SosController@update');
            Route::get('toggle_status/{sos}', 'SosController@toggleStatus');
            Route::get('delete/{sos}', 'SosController@delete');
        });

        Route::group(['prefix' => 'service_location',  'middleware' => 'permission:service_location'], function () {
            // prefix('service_location')->group(function () {
            Route::get('/', 'ServiceLocationController@index');
            Route::get('/fetch', 'ServiceLocationController@getAllLocation');
            Route::get('/create', 'ServiceLocationController@create');
            Route::post('store', 'ServiceLocationController@store');
            Route::get('edit/{service_location}', 'ServiceLocationController@getById');
            Route::post('update/{service_location}', 'ServiceLocationController@update');
            Route::get('toggle_status/{service_location}', 'ServiceLocationController@toggleStatus');
            Route::get('delete/{service_location}', 'ServiceLocationController@delete');
            Route::get('get/currency/', 'ServiceLocationController@getCurrencyByCountry')->name('getCurrencyByCountry');
        });

        Route::group(['prefix' => 'requests',  'middleware' => 'permission:view-requests'], function () {
            Route::get('/', 'RequestController@index');
            Route::get('/fetch', 'RequestController@getAllRequest');
            Route::get('/{request}', 'RequestController@getSingleRequest');
            Route::get('trip_view/{request}','RequestController@requestDetailedView');
            Route::get('/request/{request}', 'RequestController@fetchSingleRequest');
            Route::get('/fetch/request/{request}', 'RequestController@retrieveSingleRequest');
            Route::get('view-customer-invoice/{request_detail}','RequestController@viewCustomerInvoice');
            Route::get('view-driver-invoice/{request_detail}','RequestController@viewDriverInvoice');

           
        });

         Route::group(['prefix' => 'scheduled-rides',  'middleware' => 'permission:view-requests'], function () {
            Route::get('/', 'RequestController@indexScheduled');
            Route::get('/fetch', 'RequestController@getAllScheduledRequest');
            
        });

         // Cancellation Rides Reason CRUD
        Route::group(['prefix' => 'cancellation-rides',  'middleware' => 'permission:view-requests'], function () {
            Route::get('/', 'CancellationRideController@index');
            Route::get('/fetch', 'CancellationRideController@getAllRides');
            
        });



        // Faq CRUD
        Route::group(['prefix' => 'faq',  'middleware' => 'permission:manage-faq'], function () {
            Route::get('/', 'FaqController@index');
            Route::get('/fetch', 'FaqController@fetch');
            Route::get('/create', 'FaqController@create');
            Route::post('store', 'FaqController@store');
            Route::get('/{faq}', 'FaqController@getById');
            Route::post('update/{faq}', 'FaqController@update');
            Route::get('toggle_status/{faq}', 'FaqController@toggleStatus');
            Route::get('delete/{faq}', 'FaqController@delete');
        });

        // Cancellation Reason CRUD
        Route::group(['prefix' => 'cancellation',  'middleware' => 'permission:cancellation-reason'], function () {
            Route::get('/', 'CancellationReasonController@index');
            Route::get('/fetch', 'CancellationReasonController@fetch');
            Route::get('/create', 'CancellationReasonController@create');
            Route::post('store', 'CancellationReasonController@store');
            Route::get('/{reason}', 'CancellationReasonController@getById');
            Route::post('update/{reason}', 'CancellationReasonController@update');
            Route::get('toggle_status/{reason}', 'CancellationReasonController@toggleStatus');
            Route::get('delete/{reason}', 'CancellationReasonController@delete');
        });

       

        // Promo Codes CRUD
        Route::group(['prefix' => 'promo',  'middleware' => 'permission:manage-promo'], function () {
            Route::get('/', 'PromoCodeController@index');
            Route::get('/fetch', 'PromoCodeController@fetch');
            Route::get('/create', 'PromoCodeController@create');
            Route::post('store', 'PromoCodeController@store');
            Route::get('/{promo}', 'PromoCodeController@getById');
            Route::post('update/{promo}', 'PromoCodeController@update');
            Route::get('toggle_status/{promo}', 'PromoCodeController@toggleStatus');
            Route::get('delete/{promo}', 'PromoCodeController@delete');
        });

        // Manage Notifications
        Route::group(['prefix' => 'notifications',  'middleware' => 'permission:manage-promo'], function () {
            Route::get('/push', 'NotificationController@index');
            Route::get('push/fetch', 'NotificationController@fetch');
            Route::get('push/view', 'NotificationController@pushView');
            Route::post('push/send', 'NotificationController@sendPush');
            Route::get('push/delete/{notification}', 'NotificationController@delete');
        });

        // Complaint Title CRUD
        Route::group(['prefix' => 'complaint/title',  'middleware' => 'permission:cancellation-reason'], function () {
            Route::get('/', 'ComplaintTitleController@index');
            Route::get('/fetch', 'ComplaintTitleController@fetch');
            Route::get('/create', 'ComplaintTitleController@create');
            Route::post('store', 'ComplaintTitleController@store');
            Route::get('/{title}', 'ComplaintTitleController@getById');
            Route::post('update/{title}', 'ComplaintTitleController@update');
            Route::get('toggle_status/{title}', 'ComplaintTitleController@toggleStatus');
            Route::get('delete/{title}', 'ComplaintTitleController@delete');
        });

        Route::group(['prefix' => 'complaint'], function () {
            Route::get('/users', 'ComplaintController@userComplaint');
            Route::get('/users/general', 'ComplaintController@userGeneralComplaint');
            Route::get('/users/request', 'ComplaintController@userRequestComplaint');
            Route::get('/drivers', 'ComplaintController@driverComplaint');
             Route::get('/drivers/general', 'ComplaintController@driverGeneralComplaint');
            Route::get('/drivers/request', 'ComplaintController@driverRequestComplaint');
            Route::get('/owner', 'ComplaintController@ownerComplaint');
             Route::get('/owner/general', 'ComplaintController@ownerGeneralComplaint');
            Route::get('/owner/request', 'ComplaintController@ownerRequestComplaint');
            Route::get('/taken/{complaint}', 'ComplaintController@takeComplaint');
            Route::get('/solved/{complaint}', 'ComplaintController@solveComplaint');
        });

        // Report page
        Route::group(['prefix' => 'reports',  'middleware' => 'permission:reports'], function () {
            Route::get('/user', 'ReportController@userReport')->name('userReport');
            Route::get('/driver', 'ReportController@driverReport')->name('driverReport');

            Route::get('/owner', 'ReportController@ownerReport')->name('ownerReport');


            Route::get('/driver-duties', 'ReportController@driverDutiesReport')->name('driverDutiesReport');
            Route::get('/travel', 'ReportController@travelReport')->name('travelReport');
            Route::any('/download', 'ReportController@downloadReport')->name('downloadReport');
        });

        // Manage Map
        Route::group(['prefix' => 'map',  'middleware' => 'permission:manage-map'], function () {
            Route::get('/view', 'MapController@mapView')->name('mapView');
            Route::get('/mapbox-view', 'MapController@mapViewMapbox')->name('mapViewMapbox');
            Route::get('/heatmap{zone_id?}', 'MapController@heatMapView')->name('heatMapView');
            Route::get('/get/zone', 'MapController@getZoneByServiceLocation')->name('getZoneByServiceLocation');
        });

    });

    Route::namespace('Master')->group(function () {

        Route::prefix('roles')->group(function () {
            Route::get('/', 'RoleController@index');
            Route::get('create', 'RoleController@create');
            Route::post('store', 'RoleController@store');
            Route::get('edit/{id}', 'RoleController@getById');
            Route::post('update/{role}', 'RoleController@update');
            Route::get('assign/permissions/{id}', 'RoleController@assignPermissionView');
            Route::post('assign/permissions/update/{role}', 'RoleController@attachAndDetachPermissions');
        });
        Route::prefix('system/settings')->group(function () {
            Route::get('/', 'SettingController@index');
            Route::post('/', 'SettingController@store');
        });

        // Car Make CRUD
        Route::group(['prefix' => 'carmake',  'middleware' => 'permission:manage-carmake'], function () {
            Route::get('/', 'CarMakeController@index');
            Route::get('/fetch', 'CarMakeController@fetch');
            Route::get('/create', 'CarMakeController@create');
            Route::post('store', 'CarMakeController@store');
            Route::get('/{make}', 'CarMakeController@getById');
            Route::post('update/{make}', 'CarMakeController@update');
            Route::get('toggle_status/{make}', 'CarMakeController@toggleStatus');
            Route::get('delete/{make}', 'CarMakeController@delete');
        });

        // Car Model CRUD
        Route::group(['prefix' => 'carmodel',  'middleware' => 'permission:manage-carmodel'], function () {
            Route::get('/', 'CarModelController@index');
            Route::get('/fetch', 'CarModelController@fetch');
            Route::get('/create', 'CarModelController@create');
            Route::post('store', 'CarModelController@store');
            Route::get('/{model}', 'CarModelController@getById');
            Route::post('update/{model}', 'CarModelController@update');
            Route::get('toggle_status/{model}', 'CarModelController@toggleStatus');
            Route::get('delete/{model}', 'CarModelController@delete');
        });

        // Driver Needed Document CRUD
        Route::group(['prefix' => 'needed_doc',  'middleware' => 'permission:manage-needed-document'], function () {
            Route::get('/', 'DriverNeededDocumentController@index');
            Route::get('/fetch', 'DriverNeededDocumentController@fetch');
            Route::get('/create', 'DriverNeededDocumentController@create');
            Route::post('store', 'DriverNeededDocumentController@store');
            Route::get('/{needed_doc}', 'DriverNeededDocumentController@getById');
            Route::post('update/{needed_doc}', 'DriverNeededDocumentController@update');
            Route::get('toggle_status/{needed_doc}', 'DriverNeededDocumentController@toggleStatus');
            Route::get('delete/{needed_doc}', 'DriverNeededDocumentController@delete');
        }); 
         // Owner Needed Document CRUD
                Route::group(['prefix' => 'owner_needed_doc',  'middleware' => 'permission:manage-owner-needed-document'], function () {
                    Route::get('/', 'OwnerNeededDocumentController@index');
                    Route::get('/fetch', 'OwnerNeededDocumentController@fetch');
                    Route::get('/create', 'OwnerNeededDocumentController@create');
                    Route::post('store', 'OwnerNeededDocumentController@store');
                    Route::get('/{needed_doc}', 'OwnerNeededDocumentController@getById');
                    Route::post('update/{needed_doc}', 'OwnerNeededDocumentController@update');
                    Route::get('toggle_status/{needed_doc}', 'OwnerNeededDocumentController@toggleStatus');
                    Route::get('delete/{needed_doc}', 'OwnerNeededDocumentController@delete');
                }); 
          // Fleet Needed Document CRUD
            Route::group(['prefix' => 'fleet_needed_doc',  'middleware' => 'permission:manage-fleet-needed-document'], function () {
                Route::get('/', 'FleetNeededDocumentController@index');
                Route::get('/fetch', 'FleetNeededDocumentController@fetch');
                Route::get('/create', 'FleetNeededDocumentController@create');
                Route::post('store', 'FleetNeededDocumentController@store');
                Route::get('/{needed_doc}', 'FleetNeededDocumentController@getById');
                Route::post('update/{needed_doc}', 'FleetNeededDocumentController@update');
                Route::get('toggle_status/{needed_doc}', 'FleetNeededDocumentController@toggleStatus');
                Route::get('delete/{needed_doc}', 'FleetNeededDocumentController@delete');
                }); 
        // Package type CRUD
        Route::group(['prefix' => 'package_type',  'middleware' => 'permission:package-type'], function () {
            Route::get('/', 'PackageTypeController@index');
            Route::get('/fetch', 'PackageTypeController@fetch');
            Route::get('/create', 'PackageTypeController@create');
            Route::post('store', 'PackageTypeController@store');
            Route::get('/{package}', 'PackageTypeController@getById');
            Route::post('update/{package}', 'PackageTypeController@update');
            Route::get('toggle_status/{package}', 'PackageTypeController@toggleStatus');
            Route::get('delete/{package}', 'PackageTypeController@delete');
        });


    });
});

    Route::middleware('auth:web')->namespace('Dispatcher')->group(function () {
        Route::prefix('dispatch')->group(function () {
        Route::get('/new', 'DispatcherController@dispatchView');
        Route::get('/', 'DispatcherController@index');
        Route::post('create/request', 'DispatcherController@createRequest');
        Route::get('/request/{requestmodel}', 'DispatcherController@fetchSingleRequest');
       
    });
});


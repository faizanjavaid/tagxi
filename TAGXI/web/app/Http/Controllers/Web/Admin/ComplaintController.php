<?php

namespace App\Http\Controllers\Web\Admin;

use App\Base\Constants\Masters\ComplaintType;
use App\Base\Constants\Masters\PushEnums;
use App\Base\Constants\Masters\UserType;
use App\Http\Controllers\Controller;
use App\Jobs\Notifications\AndroidPushNotification;
use App\Models\Admin\Complaint;
use App\Models\User;
use Illuminate\Http\Request;

class ComplaintController extends Controller
{
    protected $complaint;

    /**
     * FaqController constructor.
     *
     * @param \App\Models\Admin\Complaint $complaint
     */
    public function __construct(Complaint $complaint)
    {
        $this->complaint = $complaint;
    }


    public function userComplaint()
    {
        $page = trans('pages_names.user_complaints');

        $main_menu = 'complaints';
        $sub_menu = 'user-complaint';

        $results = $this->complaint->where('user_id', '!=', null)->paginate(10);

        return view('admin.complaints.user.index', compact('page', 'main_menu', 'sub_menu', 'results'));
    }

    public function userGeneralComplaint()
    {
        $page = trans('pages_names.general_complaints');

        $main_menu = 'complaints';
         $sub_menu = 'user-general-complaint';
        // $sub_sub_menu = 'user_general-complaint';

        $results = $this->complaint->where('user_id', '!=', null)->where('complaint_type','general')->paginate(10);

        return view('admin.complaints.user.index', compact('page', 'main_menu', 'sub_menu', 'results'));
    }
    public function userRequestComplaint()
    {
        $page = trans('pages_names.request_complaints');

        $main_menu = 'complaints';
        $sub_menu = 'user-request-complaint';
        // $sub_sub_menu = 'user_request-complaint';

        $results = $this->complaint->where('user_id', '!=', null)->where('complaint_type','request_help')->paginate(10);

        return view('admin.complaints.user.index', compact('page', 'main_menu', 'sub_menu','results'));
    }

    public function driverComplaint()
    {
        $page = trans('pages_names.driver_complaints');

        $main_menu = 'complaints';
        $sub_menu = 'driver-complaint';

        $results = $this->complaint->where('driver_id', '!=', null)->paginate(10);

        return view('admin.complaints.driver.index', compact('page', 'main_menu', 'sub_menu', 'results'));
    }

    public function driverGeneralComplaint()
    {
        $page = trans('pages_names.general_complaints');

        $main_menu = 'complaints';
         $sub_menu = 'driver-general-complaint';
        // $sub_sub_menu = 'driver_general-complaint';

        $results = $this->complaint->where('driver_id', '!=', null)->where('complaint_type','general')->paginate(10);

        return view('admin.complaints.driver.index', compact('page', 'main_menu', 'sub_menu', 'results'));
    }
    public function driverRequestComplaint()
    {
        $page = trans('pages_names.request_complaints');

        $main_menu = 'complaints';
        $sub_menu = 'driver-request-complaint';
        // $sub_sub_menu = 'driver_request-complaint';

        $results = $this->complaint->where('driver_id', '!=', null)->where('complaint_type','request_help')->paginate(10);

        return view('admin.complaints.driver.index', compact('page', 'main_menu', 'sub_menu','results'));
    }

    public function takeComplaint(Complaint $complaint)
    {
        $complaint->update([
            'status' => ComplaintType::TAKEN
        ]);

        $user = User::whereId($complaint->user_id)->first();

        $title = trans('push_notifications.complaint_taken_title');
        $body = trans('push_notifications.complaint_taken_body');
        $push_data = ['notification_enum'=>PushEnums::COMPLAINT_TAKEN];

        $user->notify(new AndroidPushNotification($title, $body, $push_data));

        $message = trans('succes_messages.complaint_taken_succesfully');

        return back()->with('success', $message);
    }

    public function solveComplaint(Complaint $complaint)
    {
        $complaint->update([
            'status' => ComplaintType::SOLVED
        ]);

        $user = User::whereId($complaint->user_id)->first();

        $title = trans('push_notifications.complaint_solved_title');
        $body = trans('push_notifications.complaint_solved_body');
        $push_data = ['notification_enum'=>PushEnums::COMPLAINT_SOLVED];

        $user->notify(new AndroidPushNotification($title, $body, $push_data));

        $message = trans('succes_messages.complaint_solved_succesfully');

        return back()->with('success', $message);
    }
}

@extends('admin.layouts.app')
@section('title', 'Main page')

@section('content')
{{-- {{session()->get('errors')}} --}}

    <!-- Start Page content -->
    <div class="content">
        <div class="container-fluid">

            <div class="row">
                <div class="col-sm-12">
                    <div class="box">

                        <div class="box-header with-border">
                            <a href="{{ url('cancellation') }}">
                                <button class="btn btn-danger btn-sm pull-right" type="submit">
                                    <i class="mdi mdi-keyboard-backspace mr-2"></i>
                                    @lang('view_pages.back')
                                </button>
                            </a>
                        </div>

                        <div class="col-sm-12">

                            <form method="post" class="form-horizontal" action="{{ url('cancellation/store') }}">
                                @csrf

                                <div class="row">
                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label for="user_type">@lang('view_pages.user_type') <span class="text-danger">*</span></label>
                                            <select name="user_type" id="user_type" class="form-control" required>
                                                <option value="" selected disabled>@lang('view_pages.select')</option>
                                                <option value="user" {{ old('user_type') == 'user' ? 'selected' : '' }} >@lang('view_pages.user')</option>
                                                <option value="driver" {{ old('user_type') == 'driver' ? 'selected' : '' }} >@lang('view_pages.driver')</option>
                                                {{-- <option value="both" {{ old('user_type') == 'both' ? 'selected' : '' }} >@lang('view_pages.both')</option> --}}
                                            </select>
                                            <span class="text-danger">{{ $errors->first('user_type') }}</span>
                                        </div>
                                    </div>

                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label for="payment_type">@lang('view_pages.payment_type') <span class="text-danger">*</span></label>
                                            <select name="payment_type" id="payment_type" class="form-control" required>
                                                <option value="" selected disabled>@lang('view_pages.select')</option>
                                                <option value="free" {{ old('payment_type') == 'free' ? 'selected' : '' }} >@lang('view_pages.free')</option>
                                                <option value="compensate" {{ old('payment_type') == 'compensate' ? 'selected' : '' }} >@lang('view_pages.compensate')</option>
                                            </select>
                                            <span class="text-danger">{{ $errors->first('payment_type') }}</span>
                                        </div>
                                    </div>

                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label for="arrival_status">@lang('view_pages.arrival_status') <span class="text-danger">*</span></label>
                                            <select name="arrival_status" id="arrival_status" class="form-control" required>
                                                <option value="" selected disabled>@lang('view_pages.select')</option>
                                                <option value="before" {{ old('arrival_status') == 'before' ? 'selected' : '' }} >@lang('view_pages.before')</option>
                                                <option value="after" {{ old('arrival_status') == 'after' ? 'selected' : '' }} >@lang('view_pages.after')</option>
                                            </select>
                                            <span class="text-danger">{{ $errors->first('arrival_status') }}</span>
                                        </div>
                                    </div>

                                    <div class="col-sm-6">
                                        <div class="form-group">
                                            <label for="reason">@lang('view_pages.reason') <span class="text-danger">*</span></label>
                                            <input class="form-control" type="text" id="reason" name="reason"
                                                value="{{ old('reason') }}" required
                                                placeholder="@lang('view_pages.enter') @lang('view_pages.reason')">
                                            <span class="text-danger">{{ $errors->first('reason') }}</span>
                                        </div>
                                    </div>
                                </div>


                                <div class="form-group">
                                    <div class="col-12">
                                        <button class="btn btn-primary btn-sm pull-right m-5" type="submit">
                                            @lang('view_pages.save')
                                        </button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- container -->
</div>
    <!-- content -->
@endsection

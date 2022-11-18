@extends('admin.layouts.app')

@section('title', 'Admin Profile')
@section('content')

<section class="content">

    <div class="row">
      <div class="col-12">
        <!-- Profile Image -->
        <div class="box">
          <div class="box-body box-profile">            
            <div class="row">
                <div class="col-md-4 col-12">
                    <div class="profile-user-info">
                      <p>Email :<span class="text-gray pl-10">{{ $user->email }}</span> </p>
                      <p>Phone :<span class="text-gray pl-10">{{ $user->mobile }}</span></p>
                      <p>Address :<span class="text-gray pl-10">{{ $user->admin ? $user->admin->address : '' }}</span></p>
                  </div>
               </div>
                <div class="col-md-3 col-12">
                    {{-- <div class="profile-user-info">						
                      <p class="margin-bottom">Social Profile</p>
                      <div class="user-social-acount">
                          <button class="btn btn-circle btn-social-icon btn-facebook"><i class="fa fa-facebook"></i></button>
                          <button class="btn btn-circle btn-social-icon btn-twitter"><i class="fa fa-twitter"></i></button>
                          <button class="btn btn-circle btn-social-icon btn-instagram"><i class="fa fa-instagram"></i></button>
                      </div>
                  </div> --}}
               </div>
                <div class="col-md-5 col-12">
                    <div class="profile-user-info">
                      <div class="map-box">
                        <img src="{{ $user->profile_picture ?? asset('assets/img/user-dummy.svg') }}" class="float-right rounded-circle" alt="" style="max-width: 25%;">
                      </div>
                  </div>
               </div>
            </div>
          </div>
          <!-- /.box-body -->
        </div>
        <!-- /.box -->
      </div>
      <!-- /.col -->


      <div class="col-12">
        <div class="nav-tabs-custom box-profile">
          <ul class="nav nav-tabs">
            <li><a class="{{ old('tab','basic_info') == 'basic_info' ? 'active' : ''}}" href="#basic_info" data-toggle="tab">Basic Information</a></li>
            <li><a class="{{ old('tab') == 'manage_password' ? 'active' : ''}}" href="#manage_password" data-toggle="tab">Manage Password</a></li>
          </ul>
                      
          <div class="tab-content"> 
            
            <div class="{{ old('tab','basic_info') == 'basic_info' ? 'active' : ''}} tab-pane" id="basic_info">	
                <form  method="post" class="form-horizontal" action="{{url('admins/profile/update',$user->id)}}" enctype="multipart/form-data">
                    @csrf	
                <input type="hidden" name="tab" value="basic_info">
                <div class="box p-15">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                            <label for="first_name">@lang('view_pages.first_name')</label>
                            <input class="form-control" type="text" id="first_name" name="first_name" value="{{old('first_name',$user->admin->first_name)}}" required="" placeholder="@lang('view_pages.enter_first_name')">
                            <span class="text-danger">{{ $errors->first('first_name') }}</span>
                
                        </div>
                        </div>
                                <div class="col-sm-6">
                                <div class="form-group">
                                <label for="last_name">@lang('view_pages.last_name')</label>
                                <input class="form-control" type="text" id="last_name" name="last_name" value="{{old('last_name',$user->admin->last_name)}}" required="" placeholder="@lang('view_pages.enter_last_name')">
                                <span class="text-danger">{{ $errors->first('last_name') }}</span>
                    
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-sm-12">
                            <div class="form-group">
                            <label for="address">@lang('view_pages.address')</label>
                            <input class="form-control" type="text" id="address" name="address" value="{{old('address',$user->admin->address)}}" required="" placeholder="@lang('view_pages.enter_address')">
                            <span class="text-danger">{{ $errors->first('address') }}</span>

                        </div>
                    </div>
                    </div>


                    <div class="row">
                        <div class="col-sm-6">
                            <div class="form-group">
                            <label for="name">@lang('view_pages.mobile')</label>
                            <input class="form-control" type="text" id="mobile" name="mobile" value="{{old('mobile',$user->admin->mobile)}}" required="" placeholder="@lang('view_pages.enter_mobile')">
                            <span class="text-danger">{{ $errors->first('mobile') }}</span>

                        </div>
                    </div>

                    <div class="col-sm-6">
                            <div class="form-group">
                            <label for="email">@lang('view_pages.email')</label>
                            <input class="form-control" type="email" id="email" name="email" value="{{old('email',$user->admin->email)}}" required="" placeholder="@lang('view_pages.enter_email')">
                            <span class="text-danger">{{ $errors->first('email') }}</span>

                        </div>
                    </div>

                    </div>

                    <div class="form-group">
                        <div class="col-6">
                            <label for="profile_picture">@lang('view_pages.profile')</label><br>
                            <img id="blah" src="{{ $user->profile_picture ?? asset('assets/img/user-dummy.svg') }}" class="rounded-circle mb-4" alt="" style="max-width: 25%;"><br>
                            <input type="file" id="profile_picture" onchange="readURL(this)" name="profile_picture" style="display:none">
                            <button class="btn btn-primary btn-sm" type="button" onclick="$('#profile_picture').click()" id="upload">Browse</button>
                            <button class="btn btn-danger btn-sm" type="button" id="remove_img" style="display: none;">Remove</button><br>
                            <span class="text-danger">{{ $errors->first('profile_picture') }}</span>
                    </div>
                    </div>
      
                    <div class="form-group">
                        <div class="col-12">
                            <button class="btn btn-primary btn-sm pull-right" type="submit">
                                @lang('view_pages.update')
                            </button>
                        </div>
                    </div>

                    </div>	
                    </form>		  
                </div>

            <div class="{{ old('tab') == 'manage_password' ? 'active' : ''}} tab-pane" id="manage_password">		
                <div class="box p-15">		
                    <form  method="post" class="form-horizontal" action="{{url('admins/profile/update',$user->id)}}" enctype="multipart/form-data">
                        @csrf	
                        <input type="hidden" name="tab" value="manage_password">
                        <div class="row">
                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="password">@lang('view_pages.password')</label>
                                    <input class="form-control" type="password" id="password" name="password" value="" required="" placeholder="@lang('view_pages.enter_password')">
                                    <span class="text-danger">{{ $errors->first('password') }}</span>
                                </div>
                            </div>

                            <div class="col-sm-6">
                                <div class="form-group">
                                    <label for="password_confrim">@lang('view_pages.confirm_password')</label>
                                    <input class="form-control" type="password" id="password_confirmation" name="password_confirmation" value="" required="" placeholder="@lang('view_pages.enter_password_confirmation')">
                                    <span class="text-danger">{{ $errors->first('password') }}</span>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-12">
                                <button class="btn btn-primary btn-sm pull-right" name="action" value="password" type="submit">
                                    @lang('view_pages.update')
                                </button>
                            </div>
                        </div>
                    </form>
                </div>			  
            </div>
            <!-- /.tab-pane -->
          </div>
          <!-- /.tab-content -->
        </div>
        <!-- /.nav-tabs-custom -->
      </div>
      <!-- /.col -->
        
    </div>
    <!-- /.row -->

  </section>

@endsection
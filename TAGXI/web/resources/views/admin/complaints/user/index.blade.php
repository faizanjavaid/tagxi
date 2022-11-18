@extends('admin.layouts.app')

@section('title', 'Users')

@section('content')

    <section class="content">
        <div class="row">
            <div class="col-12">
                <div class="box table-responsive">
                    <div class="box-header with-border">
                        <div class="row text-right">
                            {{-- <div class="col-2">
                        <div class="form-group">
                            <input type="text" id="search_keyword" name="search" class="form-control" placeholder="Enter keyword">
                        </div>
                    </div>

                    <div class="col-1">
                        <button class="btn btn-success btn-outline btn-sm mt-5" type="submit">
                            @lang('view_pages.search')
                        </button>
                    </div> 
                        

                    <div class="col-12 text-right">
                        <a href="{{url('complaint/title/create')}}" class="btn btn-primary btn-sm">
                            <i class="mdi mdi-plus-circle mr-2"></i>@lang('view_pages.add_complaint_title')</a>
                    </div> --}}
                        </div>
                    </div>

                    <table class="table table-hover">
                        <thead>
                            <tr>
                                <th> @lang('view_pages.s_no')</th>
                                <th> @lang('view_pages.complaint_type')</th>
                                <th> @lang('view_pages.user_name')</th>
                                <th> @lang('view_pages.title')</th>
                                <th> @lang('view_pages.description')</th>
                                <th> @lang('view_pages.status')</th>
                                <th> @lang('view_pages.action')</th>
                            </tr>
                        </thead>

                        <tbody>
                            @php  $i= $results->firstItem(); @endphp

                            @forelse($results as $key => $result)
                                <tr>
                                    <td>{{ $i++ }} </td>
                                    <td>
                                        @if ($result->complaint_type == 'request_help')
                                            <span
                                                class="badge badge-warning"> Trip Complaint</span>
                                        @else
                                            <span class="badge badge-danger">General Complaint</span>
                                        @endif
                                    </td>
                                    <td>{{ $result->user->name }}</td>
                                    <td>{{ $result->complaint->title }}</td>
                                    <td>{{ $result->description }}</td>

                                    <td>
                                        @if ($result->status == 'new')
                                            <span class="badge badge-danger">{{ strtoupper($result->status) }} </span>
                                        @elseif($result->status == 'taken')
                                            <span class="badge badge-warning">{{ strtoupper($result->status) }} </span>
                                        @elseif($result->status == 'solved')
                                            <span class="badge badge-success">{{ strtoupper($result->status) }} </span>
                                        @endif
                                    </td>


                                    @if ($result->status == 'solved')
                                        <td>-</td>
                                    @else
                                        <td>
                                            <div class="dropdown">
                                                <button type="button" class="btn btn-info btn-sm dropdown-toggle"
                                                    data-toggle="dropdown" aria-haspopup="true"
                                                    aria-expanded="false">@lang('view_pages.action')
                                                </button>
                                                <div class="dropdown-menu">
                                                    @if ($result->status == 'new')
                                                        <a class="dropdown-item"
                                                            href="{{ url('complaint/taken', $result->id) }}"><i
                                                                class="fa fa-dot-circle-o"></i>@lang('view_pages.taken')</a>
                                                    @elseif ($result->status == 'taken')
                                                        <a class="dropdown-item"
                                                            href="{{ url('complaint/solved', $result->id) }}"><i
                                                                class="fa fa-dot-circle-o"></i>@lang('view_pages.solved')</a>
                                                    @endif
                                                </div>
                                            </div>
                                        </td>
                                    @endif
                                </tr>
                            @empty
                                <tr>
                                    <td colspan="11">
                                        <p id="no_data" class="lead no-data text-center">
                                            <img src="{{ asset('assets/img/dark-data.svg') }}"
                                                style="width:150px;margin-top:25px;margin-bottom:25px;" alt="">
                                        <h4 class="text-center" style="color:#333;font-size:25px;">NO DATA FOUND</h4>
                                        </p>
                                    </td>
                                </tr>
                            @endforelse

                        </tbody>
                    </table>
                    <ul class="pagination pagination-sm pull-right">
                        <li>
                            <a href="#">{{ $results->links() }}</a>
                        </li>
                    </ul>

                </div>
            </div>
        </div>
    @endsection

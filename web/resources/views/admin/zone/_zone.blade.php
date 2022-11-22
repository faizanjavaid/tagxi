<div class="box-body no-padding">
    <div class="table-responsive">
      <table class="table table-hover">
    <thead>
    <tr>


    <th> @lang('view_pages.s_no')
    <span style="float: right;">

    </span>
    </th>

    <th> @lang('view_pages.name')
    <span style="float: right;">
    </span>
    </th>
    <th> @lang('view_pages.status')
    <span style="float: right;">
    </span>
    </th>
    <th> @lang('view_pages.action')
    <span style="float: right;">
    </span>
    </th>
    </tr>
    </thead>
    <tbody>
    @if(count($results)<1)
    <tr>
        <td colspan="11">
        <p id="no_data" class="lead no-data text-center">
        <img src="{{asset('assets/img/dark-data.svg')}}" style="width:150px;margin-top:25px;margin-bottom:25px;" alt="">
     <h4 class="text-center" style="color:#333;font-size:25px;">@lang('view_pages.no_data_found')</h4>
 </p>
    </tr>
    @else

    @php  $i= $results->firstItem(); @endphp

    @foreach($results as $key => $result)

    <tr>
    <td>{{ $i++ }} </td>
    <td> {{$result->name}}</td>
    @if($result->active)
    <td><button class="btn btn-success btn-sm">@lang('view_pages.active')</button></td>
    @else
    <td><button class="btn btn-danger btn-sm">@lang('view_pages.inactive')</button></td>
    @endif
    <td>

    <button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">@lang('view_pages.action')
    </button>

        <div class="dropdown-menu" x-placement="bottom-start">
             <a class="dropdown-item" href="{{url('zone/mapview',$result->id)}}"><i class="fa fa-eye"></i>@lang('view_pages.map_view')</a>
            
             <a class="dropdown-item" href="{{url('zone/edit',$result->id)}}"><i class="fa fa-pencil"></i>@lang('view_pages.edit')</a>
          
           <a class="dropdown-item" href="{{url('zone/assigned/types',$result->id)}}"><i class="fa fa-plus"></i>@lang('view_pages.assign_types')</a>
            
            @if($result->active)
            <a class="dropdown-item" href="{{url('zone/toggle_status',$result->id)}}">
            <i class="fa fa-dot-circle-o"></i>@lang('view_pages.inactive')</a>
            @else
            <a class="dropdown-item" href="{{url('zone/toggle_status',$result->id)}}">
            <i class="fa fa-dot-circle-o"></i>@lang('view_pages.active')</a>
            @endif

           <a class="dropdown-item" href="{{url('zone/surge',$result->id)}}"><i class="fa fa-book"></i>@lang('view_pages.surge_price')</a>

           <!--  <a class="dropdown-item sweet-delete" href="#" data-url="{{url('zone/delete',$result->id)}}"><i class="fa fa-trash-o"></i>@lang('view_pages.delete')</a> -->
        </div>

    </td>
    </tr>
    @endforeach
    @endif
    </tbody>
    </table>
    <div class="text-right">
    <span  style="float:right">
    {{$results->links()}}
    </span>
    </div>
    </div>
    </div>

<table class="table table-hover">
    <thead>
        <tr>
            <th> @lang('view_pages.s_no')</th>
            <th> @lang('view_pages.name')</th>
            <th> @lang('view_pages.doc_type')</th>
            <th> @lang('view_pages.has_expiry_date')</th>
            <th> @lang('view_pages.status')</th>
            <th> @lang('view_pages.action')</th>
        </tr>
    </thead>

<tbody>
    @php  $i= $results->firstItem(); @endphp

    @forelse($results as $key => $result)
        <tr>
            <td>{{ $i++ }} </td>
            <td>{{ $result->name }}</td>
            <td>{{ ucfirst($result->doc_type) }}</td>
            <td>{{ $result->has_expiry_date ? 'Yes' : 'No' }}</td>
            @if($result->active)
            <td><span class="label label-success">@lang('view_pages.active')</span></td>
            @else
            <td><span class="label label-danger">@lang('view_pages.inactive')</span></td>
            @endif
            <td>

            <button type="button" class="btn btn-info btn-sm dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">@lang('view_pages.action')
            </button>
                <div class="dropdown-menu">
                    <a class="dropdown-item" href="{{url('owner_needed_doc',$result->id)}}"><i class="fa fa-pencil"></i>@lang('view_pages.edit')</a>

                    @if($result->active)
                    <a class="dropdown-item" href="{{url('owner_needed_doc/toggle_status',$result->id)}}"><i class="fa fa-dot-circle-o"></i>@lang('view_pages.inactive')</a>
                    @else
                    <a class="dropdown-item" href="{{url('owner_needed_doc/toggle_status',$result->id)}}"><i class="fa fa-dot-circle-o"></i>@lang('view_pages.active')</a>
                    @endif

                    {{-- <a class="dropdown-item sweet-delete" href="{{url('needed_doc/delete',$result->id)}}"><i class="fa fa-trash-o"></i>@lang('view_pages.delete')</a> --}}
                </div>
            </div>

            </td>
        </tr>
    @empty
        <tr>
            <td colspan="11">
                <p id="no_data" class="lead no-data text-center">
                    <img src="{{asset('assets/img/dark-data.svg')}}" style="width:150px;margin-top:25px;margin-bottom:25px;" alt="">
                    <h4 class="text-center" style="color:#333;font-size:25px;">@lang('view_pages.no_data_found')</h4>
                </p>
            </td>
        </tr>
    @endforelse

    </tbody>
    </table>
    <nav class="mt-15 pb-10">
        <ul class="pagination pagination-sm pull-right">
            <li class="page-item">
                <a class="page-link" href="#">{{$results->links()}}</a>
            </li>
        </ul>
    </nav>

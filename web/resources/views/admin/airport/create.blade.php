@extends('admin.layouts.app')

@section('title', 'Main page')

@section('content')


<style>
#map {
height: 400px;
width: 80%;
left: 10px;
}
html, body {
padding: 0;
margin: 0;
height: 100%;
}

#panel {
width: 200px;
font-family: Arial, sans-serif;
font-size: 13px;
float: right;
margin: 10px;
margin-top: 100px;
}

#delete-button, #add-button, #delete-all-button, #save-button {
margin-top: 5px;
}
#search-box {
background-color: #f7f7f7;
font-size: 15px;
font-weight: 300;
margin-top: 10px;
margin-bottom: 10px;
margin-left: 10px;
padding: 0 11px 0 13px;
text-overflow: ellipsis;
height: 25px;
width: 80%;
border: 1px solid #c7c7c7;
}
.map_icons{
font-size: 24px;
color: white;
padding: 10px;
background-color: #43439999;
margin: 5px;
}
</style>

<div class="content">
<div class="container-fluid">
<div class="row">
<div class="col-sm-12">
<div class="box">

<div class="box-header with-border">
<a href="{{ url('airport') }}">
<button class="btn btn-danger btn-sm pull-right" type="submit">
<i class="mdi mdi-keyboard-backspace mr-2"></i>
@lang('view_pages.back')
</button>
</a>
</div>
<div class="col-sm-12">
    <form  method="post" class="form-horizontal" action="{{url('airport/store')}}" enctype="multipart/form-data">
{{csrf_field()}}
<input type="hidden" id="info" name="coordinates" value="">

<input type="hidden" id="city_polygon" name="city_polygon" value="{{ old('city_polygon') }}">

<div class="row">
<div class="col-sm-6">
<div class="form-group">
<label for="zone_admin" class="">@lang('view_pages.select_area') <sup>*</sup></label>
<select name="admin_id" id="zone_admin" class="form-control" required>
<option value="" >@lang('view_pages.select_area')</option>
@foreach($services as $key=>$service)
<option value="{{$service->id}}">{{$service->name}}</option>
@endforeach
</select>
</div>
</div>

<div class="col-sm-6">
<div class="form-group">
<label> @lang('view_pages.name') <sup>*</sup></label>
<input class="form-control" id="zone_name" type="text" name="zone_name" value="{{ old('zone_name') }}" placeholder="@lang('view_pages.enter_name')" required>
<span class="text-danger">{{ $errors->first('zone_name') }}</span>
</div>
</div>

<div class="col-sm-6">
<div class="form-group">
<label> @lang('view_pages.airport_surge_fee') <sup>*</sup></label>
<input class="form-control" id="airport_surge_fee" type="text" name="airport_surge_fee" value="{{ old('airport_surge_fee') }}" placeholder="@lang('view_pages.enter_airport_surge_fee')" required>
<span class="text-danger">{{ $errors->first('airport_surge_fee') }}</span>
</div>
</div>

</div>
<div class="row">
<div class="col-sm-12">
    <input id="search-box" class="form-control controls" type="text" placeholder="@lang('view_pages.search')" />
<div id="map" class="col-sm-10" style="float:left;"></div>

<div id="" class="col-sm-2" style="float:right;">
<ul style="list-style: none;">
<li>
<a id="select-button" href="javascript:void(0)" onclick="drawingManager.setDrawingMode(null)" class="btn-floating zone-add-btn btn-large waves-effect waves-light tooltipped" >
<i class="fa fa-hand-pointer-o map_icons"></i>
</a>
</li>

<li>
<a id="add-button" href="javascript:void(0)" onclick="drawingManager.setDrawingMode(google.maps.drawing.OverlayType.POLYGON)" class="btn-floating zone-add-btn btn-large waves-effect waves-light tooltipped" >
<i class="fa fa-plus-circle map_icons"></i>
</a>
</li>

<li>
<a id="delete-button" href="javascript:void(0)" onclick="deleteSelectedShape()" class="btn-floating zone-delete-btn btn-large waves-effect waves-light tooltipped" >
<i class="fa fa-times map_icons"></i>
</a>
</li>
<li>
<a id="delete-all-button" href="javascript:void(0)" onclick="clearMap()" class="btn-floating zone-delete-all-btn btn-large waves-effect waves-light tooltipped" >
<i class="fa fa-trash-o map_icons"></i>
</a>
</li>

</ul>
</div>

</div>
</div>
<div class="form-group text-right m-b-0"><br>
<button id="save-button" class="btn btn-primary btn-sm m-5 pull-right" type="submit">
@lang('view_pages.save')
</button>
</div>
</form>
</div>
</div>
</div>
</div>
</div>
</div>

<script src="https://maps.google.com/maps/api/js?key={{get_settings('google_map_key')}}&libraries=drawing,geometry,places"></script>

<script src="{{asset('assets/js/polygon/main.js')}}"></script>
<script src="{{asset('assets/js/polygon/nucleu.js')}}"></script>

<script type="text/javascript">

    $(document).ready(function() {
        var keyword = $('#city').val();

        if(keyword) getCoordsByKeyword(keyword);
    });

    $(document).on('change','#city',function(){
        var val = $(this).val();
        getCoordsByKeyword(val);
    });

    $(document).on('click','.searchCity',function(){
        var val = $('#city option:selected').val();
        if(val) getCoordsByKeyword(val);
    });

    $(document).on('keyup','.select2-search__field',function(){
        var val = $(this).val();

        if(val != '' && val.length > 2){
            $.ajax({
                url: '{{ route("getCityBySearch") }}',
                data: {search:val},
                method: 'get',
                success: function(results){
                    if(results.length > 0 ){
                        $('#city').html('');

                        results.forEach(city => {
                            $('#city').append('<option value="'+city[0]+'">'+city[0]+'</option>');
                        });
                    }
                }
            });
        }
    });

    function getCoordsByKeyword(keyword){
        
        $.ajax({
            url: "{{ url('zone/coords/by_keyword') }}/"+keyword,
            data: '',
            method: 'get',
            success: function(results){
                if(results){
                    $('#city_polygon').val(results);
                    window.onload = initMap()
                }
            }
        });
    }
</script>
@endsection


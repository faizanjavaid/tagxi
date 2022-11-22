@extends('admin.layouts.app')

@section('title', 'Users')

@section('content')
    <style>
        #map {
            height: 60vh;
            margin: 15px;
        }

        #floating-panel {
            background-color: #fff;
            border: 1px solid #999;
            left: 30%;
            padding: 5px;
            position: absolute;
            top: 10px;
            z-index: 5;
        }

    </style>
    <!-- Start Page content -->
    <section class="content">
        <div class="row">
            <div class="col-12">
                <div class="box">

                    <div class="box-header with-border">
                        <h3>{{ $page }}</h3>
                    </div>

                    <div class="row">
                       <!--  <div class="col-md-6">
                            <div class="form-group m-20">
                                <label for="service_location">@lang('view_pages.service_location') <span
                                        class="text-danger">*</span></label>
                                <select name="service_location" id="service_location" class="form-control">
                                    <option value="">@lang('view_pages.select_service_location')</option>
                                    @foreach ($serviceLocation as $service)
                                        <option value="{{ $service->id }}">{{ $service->name }}</option>
                                    @endforeach
                                </select>
                            </div>
                        </div>

                        <div class="col-md-6">
                            <div class="form-group m-20">
                                <label for="zone">@lang('view_pages.zone') <span class="text-danger">*</span></label>
                                <select name="zone" id="zone" class="form-control">
                                    <option value="" selected disabled>@lang('view_pages.select_zone')</option>
                                </select>
                            </div>
                        </div>
 -->
                        <div class="col-12">
                            <div id="floating-panel">
                                <button class="btn btn-sm btn-danger mt-1 mt-md-0" onclick="toggleHeatmap()">@lang('view_pages.toggle_heatmap')</button>
                                <button class="btn btn-sm btn-danger mt-1 mt-md-0" onclick="changeGradient()">@lang('view_pages.change_gradient')</button>
                                <button class="btn btn-sm btn-danger mt-1 mt-md-0" onclick="changeRadius()">@lang('view_pages.change_radius')</button>
                                <button class="btn btn-sm btn-danger mt-1 mt-md-0" onclick="changeOpacity()">@lang('view_pages.change_opacity')</button>
                            </div>

                            <div id="map"></div>
                        </div>
                    </div>

                </div>
            </div>
        </div>
    </section>


    <script type="text/javascript"
        src="https://maps.google.com/maps/api/js?key={{get_settings('google_map_key')}}&callback=initialize&libraries=visualization"
        async defer></script>

    <script type="text/javascript">
        var heatmapData = [];
        var pickLat = [];
        var pickLng = [];
        let map, heatmap;
        
       // new google.maps.event.addDomListener(window, 'load', initialize);

        function initialize() {

            var results = {!! $results !!};

            console.log(results);
            
            results.forEach(element => {
                heatmapData.push(new google.maps.LatLng(element.request_place.pick_lat, element.request_place
                    .pick_lng));
                pickLat.push(element.request_place.pick_lat)
                pickLng.push(element.request_place.pick_lng)
            });

            Lat = findAvg(pickLat);
            Lng = findAvg(pickLng);

            var centerLatLng = new google.maps.LatLng(Lat, Lng);

            map = new google.maps.Map(document.getElementById('map'), {
                center: centerLatLng,
                zoom: 11,
            });

            heatmap = new google.maps.visualization.HeatmapLayer({
                data: heatmapData,
                map: map,
                radius: 20
            });
            heatmap.setMap(map);
        }


        function toggleHeatmap() {
            heatmap.setMap(heatmap.getMap() ? null : map);
        }

        function changeGradient() {
            const gradient = [
                "rgba(0, 255, 255, 0)",
                "rgba(0, 255, 255, 1)",
                "rgba(0, 191, 255, 1)",
                "rgba(0, 127, 255, 1)",
                "rgba(0, 63, 255, 1)",
                "rgba(0, 0, 255, 1)",
                "rgba(0, 0, 223, 1)",
                "rgba(0, 0, 191, 1)",
                "rgba(0, 0, 159, 1)",
                "rgba(0, 0, 127, 1)",
                "rgba(63, 0, 91, 1)",
                "rgba(127, 0, 63, 1)",
                "rgba(191, 0, 31, 1)",
                "rgba(255, 0, 0, 1)"
            ];
            heatmap.set("gradient", heatmap.get("gradient") ? null : gradient);
        }

        function changeRadius() {
            heatmap.set("radius", heatmap.get("radius") ? null : 20);
        }

        function changeOpacity() {
            heatmap.set("opacity", heatmap.get("opacity") ? null : 0.2);
        }


        function findAvg(LatLng) {
            return LatLng.reduce((a, b) => a + b) / LatLng.length;
        }

        $(document).on('change', '#service_location', function() {
            var service_id = $(this).val();

            $.ajax({
                url: '{{ route('getZoneByServiceLocation') }}',
                data: {
                    id: service_id
                },
                method: 'get',
                success: function(results) {
                    let zone = $('#zone');
                    var option = '<option selected disabled>Select Zone</option>';

                    results.forEach(result => {
                        option += '<option value="' + result.id + '">' + result.name +
                            '</option>';
                    });

                    zone.html(option);
                }
            });
        });


        $(document).on('change', '#zone', function() {
            var zone_id = $(this).val();

            window.location.href = '{{ route('heatMapView') }}?zone_id=' + zone_id;
            //     $.ajax({
            //         url: '{{ route('heatMapView') }}',
            //         data: {zone_id:zone_id},
            //         method: 'get',
            //         success: function(results){
            //             results.forEach(element => {
            //                 heatmapData.push(new google.maps.LatLng(element.request_place.pick_lat, element.request_place.pick_lng));
            //                 pickLat.push(element.request_place.pick_lat);
            //                 pickLng.push(element.request_place.pick_lng);

            //                 google.maps.event.addDomListener(window, 'load', initialize);
            //                 $("#map").load(" #map");
            //             });
            //         }
            //     });
        });

    </script>

@endsection

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
</head>
<body>
    <div class="body">
        @include('email.partials._header')
        @yield('content')
        @include('email.partials._footer')
    </div>
</body>
</html>
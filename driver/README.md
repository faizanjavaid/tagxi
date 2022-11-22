# tagyourtaxi_driver

A new Flutter project.

## Getting Started

This project is a starting point for a Flutter application.

A few resources to get you started if this is your first Flutter project:

- [Lab: Write your first Flutter app](https://flutter.dev/docs/get-started/codelab)
- [Cookbook: Useful Flutter samples](https://flutter.dev/docs/cookbook)

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.


## code examples

install packages by typing, 'flutter pub get' in terminal

lib/functions/functions.dart contains all functions and api calls

mqtt used for getting ride status, ride request and drivers document status

lib/styles/styles.dart contains colors of pages and buttons etc and font sizes for mediaquery

lib/widgets/widgets.dart contains input fiels and button widget

lib/pages contains all pages of this app
  
   chatpage - chat page on ride
   language - choose language
   loadingpage - launchpage and loader 
   login - login page , otp page, getting started page
   navdrawer - navdrawer
   navigatorpages - the pages which contains in navdrawer
   nointernet - no internet popup page
   ontrippage - home page, invoive page and review page (map_page.dart contains all widgets of ride from accept to complete trip)
   vehicleinformations - getting all vehicle information of driver before signup
   
the every pages are named as their process, so you can easily find pages in pages folder

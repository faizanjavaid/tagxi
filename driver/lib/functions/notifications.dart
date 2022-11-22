import 'package:awesome_notifications/awesome_notifications.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:tagyourtaxi_driver/functions/functions.dart';
import 'package:tagyourtaxi_driver/styles/styles.dart';

// create an instance
FirebaseMessaging messaging = FirebaseMessaging.instance;
FlutterLocalNotificationsPlugin fltNotification =
    FlutterLocalNotificationsPlugin();

void initMessaging() {
  AwesomeNotifications().initialize(
      'resource://drawable/logo',
      [
        NotificationChannel(
            channelGroupKey: 'push_notification',
            channelKey: 'normal_push',
            channelName: 'normal_notification',
            channelDescription: 'normal notification',
            defaultColor: Colors.grey,
            ledColor: Colors.blueGrey,
            enableLights: true,
            importance: NotificationImportance.Max,
            channelShowBadge: true,
            locked: false,
            playSound: true,
            defaultPrivacy: NotificationPrivacy.Public),
        NotificationChannel(
          channelGroupKey: 'trip_request',
          channelKey: 'trip_request',
          channelName: 'Trip Requests',
          channelDescription: 'Channel with Trip Request',
          defaultColor: buttonColor,
          importance: NotificationImportance.High,
          ledColor: Colors.white,
          channelShowBadge: true,
          playSound: false,
          locked: true,
          defaultPrivacy: NotificationPrivacy.Public,
          enableLights: true,
        ),
      ],
      debug: true);

  AwesomeNotifications().actionStream.listen((event) {
    if (event.buttonKeyPressed == 'accept') {
      requestAccept();
    } else if (event.buttonKeyPressed == 'reject') {
      requestReject();
    }
  });

  FirebaseMessaging.onMessage.listen((RemoteMessage message) {
    RemoteNotification? notification = message.notification;
    AndroidNotification? android = message.notification?.android;
    if (notification != null && android != null) {
      AwesomeNotifications().createNotification(
          content: NotificationContent(
              id: 12346,
              channelKey: 'normal_push',
              autoDismissible: true,
              title: notification.title,
              body: notification.body,
              displayOnBackground: true,
              displayOnForeground: true,
              wakeUpScreen: true,
              notificationLayout: NotificationLayout.BigText,
              category: NotificationCategory.Message));
    }
  });
}

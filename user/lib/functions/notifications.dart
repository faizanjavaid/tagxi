import 'package:awesome_notifications/awesome_notifications.dart';
import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/material.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:tagyourtaxi_driver/functions/functions.dart';

// create an instance
FirebaseMessaging messaging = FirebaseMessaging.instance;
FlutterLocalNotificationsPlugin fltNotification =
    FlutterLocalNotificationsPlugin();

void initMessaging() {
  AwesomeNotifications().initialize('resource://drawable/logo', [
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
  ]);

  FirebaseMessaging.onMessage.listen((RemoteMessage message) {
    RemoteNotification? notification = message.notification;
    if (notification != null) {
      if (requestStreamEnd == null &&
          requestStreamStart == null &&
          rideStreamStart == null &&
          rideStreamUpdate == null) {
        getUserDetails();
      }
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

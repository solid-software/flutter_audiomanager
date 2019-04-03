import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_audiomanager/src/headphones_connection_state.dart';
import 'package:flutter_audiomanager/src/headphones_type.dart';

abstract class _HeadphonesChannel {
  static const String detectConnected =
      'com.example.flutter_headphones/detect_state';

  static const String onPlugged =
      'com.example.flutter_headphones/wired_state_events';

  static const String onBluetoothHeadsetConnected =
      'com.example.flutter_headphones/bluetooth_state_events';
}

class HeadphonesDetector {
  final MethodChannel _detectConnectedChannel =
      MethodChannel(_HeadphonesChannel.detectConnected);
  final EventChannel _wiredConnectionChannel =
      EventChannel(_HeadphonesChannel.onPlugged);
  final EventChannel _bluetoothConnectionChannel =
      EventChannel(_HeadphonesChannel.onBluetoothHeadsetConnected);

  Future<HeadphonesConnectionState> headphonesState({
    HeadphonesType headphonesType = HeadphonesType.any,
  }) async {
    int state = await _detectConnectedChannel.invokeMethod(
        'isHeadphonesConnected', {'headphonesType': headphonesType.index});
    return HeadphonesConnectionState.values[state];
  }

  Stream<HeadphonesConnectionState> get wiredHeadphonesConnectionState {
    return _wiredConnectionChannel
        .receiveBroadcastStream()
        .map((dynamic event) {
      return HeadphonesConnectionState.values[event as int];
    });
  }

  Stream<HeadphonesConnectionState> get bluetoothHeadphonesConnectionState {
    return _bluetoothConnectionChannel
        .receiveBroadcastStream()
        .map((dynamic event) {
      return HeadphonesConnectionState.values[event as int];
    });
  }
}

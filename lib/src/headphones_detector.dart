import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_audiomanager/src/headphones_connection_state.dart';
import 'package:flutter_audiomanager/src/headphones_type.dart';

abstract class _HeadphonesChannel {
  static const String detectConnected = 'com.example.flutter_headphones/finder';

  static const String onPlugged = 'com.example.flutter_headphones/receiver';

  static const String onBluetoothHeadsetConnected =
      'com.example.flutter_headphones/bluetooth_receiver';
}

class HeadphonesDetector {
  final MethodChannel _detectConnectedChannel =
      MethodChannel(_HeadphonesChannel.detectConnected);
  final EventChannel _wiredConnectionChannel =
      EventChannel(_HeadphonesChannel.onPlugged);
  final EventChannel _bluetoothConnectionChannel =
      EventChannel(_HeadphonesChannel.onBluetoothHeadsetConnected);

  StreamController<HeadphonesConnectionState> _bluetoothConnectedController;
  StreamController<HeadphonesConnectionState> _wiredConnectedController;

  StreamSubscription<HeadphonesConnectionState> _wiredListener;
  StreamSubscription<HeadphonesConnectionState> _bluetoothListener;

  Future<HeadphonesConnectionState> headphonesState({
    HeadphonesType headphonesType = HeadphonesType.any,
  }) async {
    int state = await _detectConnectedChannel.invokeMethod(
        'isHeadphonesConnected', {'headphonesType': headphonesType.index});
    return HeadphonesConnectionState.values[state];
  }

  Stream<HeadphonesConnectionState> get wiredHeadphonesConnectionState {
    if (_wiredConnectedController == null) {
      _wiredConnectedController = StreamController.broadcast();

      _wiredListener =
          _wiredConnectionChannel.receiveBroadcastStream().map((dynamic event) {
        return HeadphonesConnectionState.values[event as int];
      }).listen(_wiredConnectedController.add);
    }

    return _wiredConnectedController.stream;
  }

  Stream<HeadphonesConnectionState> get bluetoothHeadphonesConnectionState {
    if (_bluetoothConnectedController == null) {
      _bluetoothConnectedController = StreamController.broadcast();

      _bluetoothListener = _bluetoothConnectionChannel
          .receiveBroadcastStream()
          .map((dynamic event) {
        return HeadphonesConnectionState.values[event as int];
      }).listen(_bluetoothConnectedController.add);

      headphonesState(headphonesType: HeadphonesType.bluetooth)
          .then(_bluetoothConnectedController.add);
    }

    return _bluetoothConnectedController.stream;
  }

  void close() {
    _wiredConnectedController?.close();
    _bluetoothConnectedController?.close();
    _wiredListener?.cancel();
    _bluetoothListener?.cancel();
  }
}

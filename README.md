# Flutter Audiomanager

Flutter plugin to detect whether any headphones (wired or bluetooth) are connected to device. 
It supports only Android at the moment. Work examples:
* There are no connected headphones:
![Alt text](screenshots/no_connected.jpeg?raw=true "No connected")

* There are wired connected headphones:
![Alt text](screenshots/wired_connected.jpeg?raw=true "Wired connected")

* There are Bluetooth connected headphones:
![Alt text](screenshots/bluetooth_connected.jpeg?raw=true "Bluetooth connected")

* All types are connected:
![Alt text](screenshots/all_connected.jpeg?raw=true "Bluetooth connected")

## Getting Started

**Detect current connection state**

To detect are there any connected headphones use following:
```dart
final HeadphonesDetector headphonesDetector = HeadphonesDetector();
HeadphonesConnectionState currentState = await headphonesDetector.headphonesState();
```

Use HeadphonesType to specify detector behavior:
```dart
final HeadphonesDetector headphonesDetector = HeadphonesDetector();
HeadphonesConnectionState currentWiredConnectionState = await headphonesDetector.headphonesState(headphonesType: HeadphonesType.wired);
HeadphonesConnectionState currentBluetoothConnectionState = await headphonesDetector.headphonesState(headphonesType: HeadphonesType.bluetooth);
```

**Using Streams**

To receive changes in connection state for wired headphones you can use following:
```dart
final HeadphonesDetector headphonesDetector = HeadphonesDetector();
headphonesDetector.wiredHeadphonesConnectionState.listen(print);
```

You can receive changes for Bluetooth headphones as well:
```dart
final HeadphonesDetector headphonesDetector = HeadphonesDetector();
headphonesDetector.bluetoothHeadphonesConnectionState.listen(print);
```

## Current issues

Current issues list [is here](https://github.com/solid-software/flutter_audiomanager/issues).   
Found a bug? [Open the issue](https://github.com/solid-software/flutter_audiomanager/issues/new).
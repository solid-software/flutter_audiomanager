import 'package:flutter/material.dart';
import 'package:flutter_audiomanager/flutter_audiomanager.dart';
import 'dart:async';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final HeadphonesDetector headphonesDetector = HeadphonesDetector();
  Future<HeadphonesConnectionState> _headphonesBoth;
  Future<HeadphonesConnectionState> _headphonesWired;
  Future<HeadphonesConnectionState> _headphonesBluetooth;

  @override
  void initState() {
    super.initState();
    _refreshState();
  }

  void _refreshState() {
    setState(() {
      _headphonesBoth = headphonesDetector.headphonesState();
      _headphonesWired = headphonesDetector.headphonesState(
        headphonesType: HeadphonesType.wired,
      );
      _headphonesBluetooth = headphonesDetector.headphonesState(
        headphonesType: HeadphonesType.bluetooth,
      );
    });
  }

  Color _mapStateToColor(HeadphonesConnectionState state) {
    switch (state) {
      case HeadphonesConnectionState.connected:
        return Colors.green;
      case HeadphonesConnectionState.disconnected:
        return Colors.red;
      default:
        return Colors.yellow;
    }
  }

  String _mapStateToText(HeadphonesConnectionState state) {
    switch (state) {
      case HeadphonesConnectionState.connected:
        return 'Connected';
      case HeadphonesConnectionState.connecting:
        return 'Connecting';
      case HeadphonesConnectionState.disconnected:
        return 'Disconnected';
      case HeadphonesConnectionState.disconnecting:
        return 'Disconnecting';
      default:
        return 'Unknown';
    }
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: Text('Plugin example app'),
          centerTitle: true,
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: _refreshState,
          child: Icon(Icons.refresh),
          tooltip: 'Refresh detection state',
        ),
        body: ListView(
          children: <Widget>[
            FutureBuilder(
              future: _headphonesBoth,
              builder: (context, snapshot) {
                return ListTile(
                  title: Text('Any type detection'),
                  subtitle: Text(_mapStateToText(snapshot.data)),
                  trailing: Icon(
                    Icons.brightness_1,
                    color: _mapStateToColor(snapshot.data),
                  ),
                );
              },
            ),
            FutureBuilder(
              future: _headphonesWired,
              builder: (context, snapshot) {
                return ListTile(
                  title: Text('Wired detection'),
                  subtitle: Text(_mapStateToText(snapshot.data)),
                  trailing: Icon(
                    Icons.brightness_1,
                    color: _mapStateToColor(snapshot.data),
                  ),
                );
              },
            ),
            FutureBuilder(
              future: _headphonesBluetooth,
              builder: (context, snapshot) {
                return ListTile(
                  title: Text('Bluetooth detection'),
                  subtitle: Text(_mapStateToText(snapshot.data)),
                  trailing: Icon(
                    Icons.brightness_1,
                    color: _mapStateToColor(snapshot.data),
                  ),
                );
              },
            ),
            StreamBuilder(
              stream: headphonesDetector.wiredHeadphonesConnectionState,
              builder: (context, snapshot) {
                return ListTile(
                  title: Text('Current wired connection state'),
                  subtitle: Text(_mapStateToText(snapshot.data)),
                  trailing: Icon(
                    Icons.brightness_1,
                    color: _mapStateToColor(snapshot.data),
                  ),
                );
              },
            ),
            StreamBuilder(
              stream: headphonesDetector.bluetoothHeadphonesConnectionState,
              builder: (context, snapshot) {
                return ListTile(
                  title: Text('Current bluetooth connection state'),
                  subtitle: Text(_mapStateToText(snapshot.data)),
                  trailing: Icon(
                    Icons.brightness_1,
                    color: _mapStateToColor(snapshot.data),
                  ),
                );
              },
            ),
          ],
        ),
      ),
    );
  }

  @override
  void dispose() {
    headphonesDetector.close();
    super.dispose();
  }
}

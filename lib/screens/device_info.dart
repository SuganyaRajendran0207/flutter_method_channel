import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class DeviceInfoPage extends StatefulWidget {
  const DeviceInfoPage({super.key, required this.title});

  final String title;

  @override
  State<DeviceInfoPage> createState() => _DeviceInfoPageState();
}

class _DeviceInfoPageState extends State<DeviceInfoPage> {
  static const platform = MethodChannel('samples.flutter.dev/device_info');

  // Get device id.
  String _deviceId = 'Unknown device.';

  Future<void> _getDeviceInfo() async {
    String deviceId;
    try {
      final String result = await platform.invokeMethod('getDeviceId');
      deviceId = 'Device id - $result .';
    } on PlatformException catch (e) {
      deviceId = "Failed to get device id: '${e.message}'.";
    }

    setState(() {
      _deviceId = deviceId;
    });
  }

  @override
  void initState() {
    _getDeviceInfo();
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Text(
                'You device id info here',
                style: Theme.of(context).textTheme.subtitle1,
              ),
              const SizedBox(height: 16),
              Text(
                _deviceId,
                style: Theme.of(context).textTheme.bodyText1,
              ),
            ],
          ),
        ),
      ),
    );
  }
}
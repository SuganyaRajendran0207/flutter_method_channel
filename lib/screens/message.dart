import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class Message extends StatelessWidget {
  ValueNotifier<String> data = ValueNotifier('');
  static const platform = MethodChannel('samples.flutter.dev/message');

  Future<String> getMessageFromNative() async {
    try {
      final String result = await platform
          .invokeMethod('getMessageFromNative', {"message": data.value});
      print(result);
      return result;
    } on PlatformException catch (e) {
      return '${e.message}';
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Message from Native'),
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              const Text('Message that received from the native code',
                  style: TextStyle(fontSize: 16)),
              const SizedBox(
                height: 12,
              ),
              ValueListenableBuilder(
                  valueListenable: data,
                  builder: (BuildContext context, val, _) {
                    return Container(
                        width: MediaQuery.of(context).size.width,
                        padding: const EdgeInsets.all(16),
                        alignment: Alignment.center,
                        decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(12),
                            border: Border.all(color: Colors.grey)),
                        child: Text(val.isEmpty ? 'No Message' : val, style: const TextStyle(fontSize: 14)));
                  }),
              const SizedBox(
                height: 12,
              ),
              MaterialButton(
                onPressed: () async {
                  data.value = await getMessageFromNative();
                },
                color: Colors.blue,
                child: const Text('Go to Native',
                    style: TextStyle(color: Colors.white)),
              )
            ],
          ),
        ),
      ),
    );
  }
}

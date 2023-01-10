import 'package:flutter/material.dart';
import 'package:flutter_method_channel/screens/battery.dart';
import 'package:flutter_method_channel/screens/message.dart';

class HomeScreen extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Method Channel'),),
      body: ListView(
        padding: const EdgeInsets.all(16.0),
        shrinkWrap: true,
        children: [
          InkWell(
            onTap: (){
              Navigator.push(context, MaterialPageRoute(builder: (BuildContext context){
                return const BatteryPage(title: 'Battery level');
              }));
            },
            child: Container(
              color: Colors.blue,
              width: MediaQuery.of(context).size.width,
              alignment: Alignment.center,
              padding: const EdgeInsets.all(16),
              child: const Text('Battery', style: TextStyle(color: Colors.white),),
            ),
          ),
          const SizedBox(height: 16,),
          InkWell(
            onTap: (){
              Navigator.push(context, MaterialPageRoute(builder: (BuildContext context){
                return Message();
              }));
            },
            child: Container(
              color: Colors.blue,
              width: MediaQuery.of(context).size.width,
              alignment: Alignment.center,
              padding: const EdgeInsets.all(16),
              child: const Text('Message', style: TextStyle(color: Colors.white),),
            ),
          )
        ],
      ),
    );
  }
}

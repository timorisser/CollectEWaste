import 'package:collew/api/apiCommunication.dart';
import 'package:collew/pages/login.dart';
import 'package:flutter/material.dart';

import 'myColors.dart';
import 'myRoute.dart';

class MyButton {
  String text;
  StatelessWidget destination;
  List<TextEditingController> data;
  bool is_company;
  String request;
  BuildContext context;

  MyButton(this.text, this.destination, this.data, this.is_company,
      this.request, this.context);

  Widget getButtonWithSizedBox() {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return SizedBox(
      width: width * 0.65,
      height: height * 0.045,
      child: Container(
        decoration: const BoxDecoration(
          boxShadow: [
            BoxShadow(
              color: Color.fromRGBO(0, 0, 0, 0.1),
              blurRadius: 15.0,
            ),
          ],
        ),
        child: ElevatedButton(
          style: ElevatedButton.styleFrom(
            backgroundColor: MyColors.getBDD9A0(),
            shadowColor: Colors.black,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(15.0),
            ),
          ),
          onPressed: () {
            if (data.isNotEmpty) {
              List<String> parameterData = List.generate(
                  data.length, (index) => data.elementAt(index).text);
              MyButton buttonForApi = MyButton(
                  text, destination, data, is_company, request, context);
              ApiCommunication apiCommunication =
                  ApiCommunication(buttonForApi);
              apiCommunication.sendRequest(
                  parameterData, is_company, request, context);
            } else {
              Navigator.of(context).push(createRoute(destination));
            }
          },
          child: Text(
            text,
            style: TextStyle(
              fontFamily: 'OpenSans',
              color: Colors.black,
              fontSize: height * 0.025,
            ),
          ),
        ),
      ),
    );
  }

  void apiRequestFinished(bool successfully) {
    if (successfully) {
      Navigator.of(context).push(createRoute(destination));
    } else {
      debugPrint("request failed");
    }
  }
}

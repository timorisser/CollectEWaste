import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'products.dart';
import '../custom/myButton.dart';
import '../custom/myTextFormField.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';

class ChangeUserData extends StatelessWidget {
  const ChangeUserData({super.key, required this.userData});
  final String userData;

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: ChangeUserDataPage(userData: userData),
    );
  }
}

class ChangeUserDataPage extends StatefulWidget {
  const ChangeUserDataPage({super.key, required this.userData});
  final String userData;

  @override
  State<ChangeUserDataPage> createState() => _ChangeUserDataPageState(userData);
}

class _ChangeUserDataPageState extends State<ChangeUserDataPage> {
  _ChangeUserDataPageState(String this.userData);
  final String userData;

  final List<TextEditingController> controllers =
      List.generate(4, (index) => TextEditingController());

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.22,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [materialCard(width, height, userData)],
        ),
      ],
    );
  }

  materialCard(double pageWidth, double pageHeight, String userData) {
    double cardWidth = pageWidth * 0.8;
    double cardHeight = pageHeight * 0.35;
    MyTextFormField newPassword = MyTextFormField(
        "Neues Passwort",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(0),
        true,
        pageWidth,
        pageHeight);
    MyTextFormField newEmail = MyTextFormField(
        "Neue Email",
        "Email",
        TextInputType.emailAddress,
        controllers.elementAt(1),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField confirmNewPassword = MyTextFormField(
        "Neues Passwort bestätigen",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(2),
        true,
        pageWidth,
        pageHeight);
    MyTextFormField confirmNewEmail = MyTextFormField(
        "Neue Email bestätigen",
        "Email",
        TextInputType.emailAddress,
        controllers.elementAt(3),
        false,
        pageWidth,
        pageHeight);
    MyButton continueButton =
        MyButton("weiter", const Products(), [], false, "", context);
    return SizedBox(
      width: cardWidth,
      height: cardHeight,
      child: Container(
        decoration: const BoxDecoration(
          boxShadow: [
            BoxShadow(
              color: Color.fromRGBO(0, 0, 0, 0.2),
              blurRadius: 20.0,
            ),
          ],
        ),
        child: Card(
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(15.0),
          ),
          child: Column(
            children: [
              if (userData == "password")
                newPassword.getTextFormFieldWithSizedBox(),
              if (userData == "email") newEmail.getTextFormFieldWithSizedBox(),
              if (userData == "password")
                confirmNewPassword.getTextFormFieldWithSizedBox(),
              if (userData == "email")
                confirmNewEmail.getTextFormFieldWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.21,
              ),
              continueButton.getButtonWithSizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}

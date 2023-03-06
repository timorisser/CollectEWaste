import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../custom/myBottomNavBar.dart';
import '../custom/myFloatingActionButton.dart';
import 'products.dart';
import '../custom/myButton.dart';
import '../custom/myTextFormField.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';

class ChangePassword extends StatelessWidget {
  const ChangePassword({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: ChangePasswordPage(),
      bottomNavigationBar: MyBottomNavBar(4),
      floatingActionButton: floatingActionButton(context, width, false),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }
}

class ChangePasswordPage extends StatefulWidget {
  const ChangePasswordPage({super.key});

  @override
  State<ChangePasswordPage> createState() => _ChangePasswordPageState();
}

class _ChangePasswordPageState extends State<ChangePasswordPage> {
  final List<TextEditingController> controllers =
      List.generate(3, (index) => TextEditingController());

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.17,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [materialCard(width, height)],
        ),
      ],
    );
  }

  materialCard(double pageWidth, double pageHeight) {
    double cardWidth = pageWidth * 0.8;
    double cardHeight = pageHeight * 0.42;
    MyTextFormField newPassword = MyTextFormField(
        "Neues Passwort",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(0),
        true,
        pageWidth,
        pageHeight);
    MyTextFormField confirmNewPassword = MyTextFormField(
        "Neues Passwort bestätigen",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(1),
        true,
        pageWidth,
        pageHeight);
    MyTextFormField oldPassword = MyTextFormField(
        "Altes Passwort",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(2),
        true,
        pageWidth,
        pageHeight);
    MyButton continueButton = MyButton("Passwort ändern", const Products(),
        controllers, false, "changePassword", context);
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
              newPassword.getTextFormFieldWithSizedBox(),
              confirmNewPassword.getTextFormFieldWithSizedBox(),
              oldPassword.getTextFormFieldWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.11,
              ),
              continueButton.getButtonWithSizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}

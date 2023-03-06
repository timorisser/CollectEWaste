import 'package:collew/pages/login.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'products.dart';
import '../custom/myTextFormField.dart';
import '../custom/myButton.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';

class CreatePrivateAccount extends StatelessWidget {
  const CreatePrivateAccount({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: const CreatePrivateAccountPage(),
    );
  }
}

class CreatePrivateAccountPage extends StatefulWidget {
  const CreatePrivateAccountPage({super.key});

  @override
  State<CreatePrivateAccountPage> createState() =>
      _CreatePrivateAccountPageState();
}

class _CreatePrivateAccountPageState extends State<CreatePrivateAccountPage> {
  final List<TextEditingController> controllers =
      List.generate(6, (index) => TextEditingController());

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.06,
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
    double cardHeight = pageHeight * 0.7;
    MyTextFormField firstname = MyTextFormField(
        "Vorname",
        "Vorname",
        TextInputType.text,
        controllers.elementAt(0),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField lastname = MyTextFormField(
        "Nachname",
        "Nachname",
        TextInputType.text,
        controllers.elementAt(1),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField emailAddress = MyTextFormField(
        "E-Mail-Adresse",
        "E-Mail-Adresse",
        TextInputType.emailAddress,
        controllers.elementAt(2),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField phonenumber = MyTextFormField(
        "Telefonnummer",
        "Telefonnummer",
        TextInputType.number,
        controllers.elementAt(3),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField password = MyTextFormField(
        "Passwort",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(4),
        true,
        pageWidth,
        pageHeight);
    MyTextFormField confirmPassword = MyTextFormField(
        "Passwort bestätigen",
        "Passwort bestätigen",
        TextInputType.text,
        controllers.elementAt(5),
        true,
        pageWidth,
        pageHeight);
    MyButton createAccount = MyButton(
        "Account anlegen", const Login(), controllers, false, "register", context);

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
              firstname.getTextFormFieldWithSizedBox(),
              lastname.getTextFormFieldWithSizedBox(),
              emailAddress.getTextFormFieldWithSizedBox(),
              phonenumber.getTextFormFieldWithSizedBox(),
              password.getTextFormFieldWithSizedBox(),
              confirmPassword.getTextFormFieldWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.07,
              ),
              createAccount.getButtonWithSizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}

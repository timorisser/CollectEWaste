import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../custom/myButton.dart';
import '../custom/myTextFormField.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';
import 'login.dart';

class CreateCompanyAccount extends StatelessWidget {
  const CreateCompanyAccount({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: const CreateCompanyAccountPage(),
    );
  }
}

class CreateCompanyAccountPage extends StatefulWidget {
  const CreateCompanyAccountPage({super.key});

  @override
  State<CreateCompanyAccountPage> createState() =>
      _CreateCompanyAccountPageState();
}

class _CreateCompanyAccountPageState extends State<CreateCompanyAccountPage> {
  final List<TextEditingController> controllers =
      List.generate(5, (index) => TextEditingController());

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.08,
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
    double cardHeight = pageHeight * 0.65;
    MyTextFormField companyName = MyTextFormField(
        "Firmenname",
        "Firmenname",
        TextInputType.text,
        controllers.elementAt(0),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField emailAddress = MyTextFormField(
        "E-Mail-Adresse",
        "E-Mail-Adresse",
        TextInputType.emailAddress,
        controllers.elementAt(1),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField phonenumber = MyTextFormField(
        "Telefonnummer",
        "Telefonnummer",
        TextInputType.number,
        controllers.elementAt(2),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField password = MyTextFormField(
        "Passwort",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(3),
        true,
        pageWidth,
        pageHeight);
    MyTextFormField confirmPassword = MyTextFormField(
        "Passwort bestätigen",
        "Passwort bestätigen",
        TextInputType.text,
        controllers.elementAt(4),
        true,
        pageWidth,
        pageHeight);
    MyButton createAccount = MyButton(
        "Account anlegen", const Login(), controllers, true, "register", context);
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
              companyName.getTextFormFieldWithSizedBox(),
              emailAddress.getTextFormFieldWithSizedBox(),
              phonenumber.getTextFormFieldWithSizedBox(),
              password.getTextFormFieldWithSizedBox(),
              confirmPassword.getTextFormFieldWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.13,
              ),
              createAccount.getButtonWithSizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}

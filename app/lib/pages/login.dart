import 'package:collew/api/apiCommunication.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'confirmEmail.dart';
import 'changeUserData.dart';
import 'createPrivateAccount.dart';
import 'createCompanyAccount.dart';
import 'products.dart';
import '../custom/myButton.dart';
import '../custom/myTextFormField.dart';
import '../custom/myColors.dart';
import '../custom/myRoute.dart';

class Login extends StatelessWidget {
  const Login({super.key});
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
          fontFamily: 'OpenSans', primarySwatch: MyColors.getBDD9A0()),
      home: const LoginPage(),
    );
  }
}

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final List<TextEditingController> controllers =
      List.generate(2, (index) => TextEditingController());

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    Color backgroundColor = MyColors.getBDD9A0();
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: backgroundColor,
      appBar: AppBar(
        toolbarHeight: height * 0.12,
        backgroundColor: const Color(0x00000000),
        elevation: 0,
        title: Center(
          child: Image(
            image: const AssetImage('images/collew-logo.png'),
            fit: BoxFit.cover,
            height: height * 0.06,
          ),
        ),
      ),
      body: Column(
        children: [
          SizedBox(
            height: height * 0.04,
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [materialCard(width, height)],
          ),
        ],
      ),
    );
  }

  materialCard(double pageWidth, double pageHeight) {
    double cardWidth = pageWidth * 0.8;
    double cardHeight = pageHeight * 0.7;
    MyTextFormField emailAddress = MyTextFormField(
        "E-Mail-Adresse",
        "E-Mail-Adresse",
        TextInputType.emailAddress,
        controllers.elementAt(0),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField password = MyTextFormField(
        "Passwort",
        "Passwort",
        TextInputType.text,
        controllers.elementAt(1),
        true,
        pageWidth,
        pageHeight);
    MyButton login = MyButton(
        "Anmelden", const Products(), controllers, false, "login", context);
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
              Text(
                "Login",
                style: TextStyle(
                  fontFamily: 'OpenSans',
                  fontSize: cardHeight * 0.07,
                  fontWeight: FontWeight.bold,
                  height: cardHeight * 0.004,
                  color: Colors.black,
                ),
              ),
              SizedBox(
                height: cardHeight * 0.02,
              ),
              emailAddress.getTextFormFieldWithSizedBox(),
              password.getTextFormFieldWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.02,
              ),
              InkWell(
                onTap: () =>
                    Navigator.of(context).push(createRoute(const ConfirmEmail(
                        destination: ChangeUserData(
                  userData: "password",
                )))),
                child: const Text(
                  "Passwort vergessen?",
                  style: TextStyle(
                    decoration: TextDecoration.underline,
                    color: Colors.black,
                  ),
                ),
              ),
              SizedBox(
                height: cardHeight * 0.09,
              ),
              login.getButtonWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.09,
              ),
              InkWell(
                onTap: () => Navigator.of(context)
                    .push(createRoute(const CreatePrivateAccount())),
                child: Text(
                  "Privataccount anlegen",
                  style: TextStyle(
                    fontSize: cardHeight * 0.03,
                    decoration: TextDecoration.underline,
                    color: Colors.black,
                  ),
                ),
              ),
              SizedBox(
                height: cardHeight * 0.08,
              ),
              InkWell(
                onTap: () => Navigator.of(context)
                    .push(createRoute(const CreateCompanyAccount())),
                child: Text(
                  "Firmenaccount anlegen",
                  style: TextStyle(
                    fontSize: cardHeight * 0.03,
                    decoration: TextDecoration.underline,
                    color: Colors.black,
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}

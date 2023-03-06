import 'package:collew/pages/changePassword.dart';
import 'package:flutter/material.dart';
import '../custom/myFloatingActionButton.dart';

import 'addresses.dart';
import 'changeUserData.dart';
import 'confirmEmail.dart';
import '../custom/myButton.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';
import '../custom/myRoute.dart';
import '../custom/myBottomNavBar.dart';

class Profile extends StatelessWidget {
  const Profile({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: const ProfilePage(),
      bottomNavigationBar: MyBottomNavBar(4),
      floatingActionButton: floatingActionButton(context, width, false),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }
}

class ProfilePage extends StatefulWidget {
  const ProfilePage({super.key});

  @override
  State<ProfilePage> createState() => _ProfilePagePageState();
}

class _ProfilePagePageState extends State<ProfilePage> {
  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.03,
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
    MyButton manageAddresses = MyButton(
        "Adressen verwalten", const Addresses(), [], false, "", context);

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
              SizedBox(
                height: cardHeight * 0.05,
              ),
              Icon(
                Icons.account_circle,
                size: pageWidth * 0.08,
              ),
              SizedBox(
                height: cardHeight * 0.01,
              ),
              Container(
                alignment: Alignment.center,
                child: Text(
                  "Max",
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: cardHeight * 0.04,
                  ),
                ),
              ),
              SizedBox(
                height: cardHeight * 0.01,
              ),
              Container(
                alignment: Alignment.center,
                child: Text(
                  "Mustermann",
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: cardHeight * 0.04,
                  ),
                ),
              ),
              SizedBox(
                height: cardHeight * 0.05,
              ),
              Icon(
                Icons.email_outlined,
                size: pageWidth * 0.08,
              ),
              SizedBox(
                height: cardHeight * 0.01,
              ),
              Container(
                alignment: Alignment.center,
                child: Text(
                  "example@gmail.com",
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: cardHeight * 0.04,
                  ),
                ),
              ),
              SizedBox(
                height: cardHeight * 0.05,
              ),
              Icon(
                Icons.phone,
                size: pageWidth * 0.08,
              ),
              SizedBox(
                height: cardHeight * 0.01,
              ),
              Container(
                alignment: Alignment.center,
                child: Text(
                  "+43 123 456789",
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: cardHeight * 0.04,
                  ),
                ),
              ),
              SizedBox(
                height: cardHeight * 0.07,
              ),
              manageAddresses.getButtonWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.06,
              ),
              InkWell(
                onTap: () =>
                    Navigator.of(context).push(createRoute(const ConfirmEmail(
                        destination: ChangeUserData(
                  userData: "email",
                )))),
                child: Text(
                  "Email ändern",
                  style: TextStyle(
                    fontSize: cardHeight * 0.03,
                    decoration: TextDecoration.underline,
                    color: Colors.black,
                  ),
                ),
              ),
              SizedBox(
                height: cardHeight * 0.05,
              ),
              InkWell(
                onTap: () =>
                    Navigator.of(context).push(createRoute(const ChangePassword())),
                child: Text(
                  "Passwort ändern",
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

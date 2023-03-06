import 'package:collew/pages/login.dart';
import 'package:flutter/material.dart';
import 'dart:math' as math;

import '../pages/profile.dart';
import '../pages/support.dart';
import 'myColors.dart';
import 'myRoute.dart';

class MyBottomNavBar extends StatelessWidget {
  MyBottomNavBar(this.colorId);
  final int colorId;

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return ClipRRect(
      borderRadius: BorderRadius.only(
        topLeft: Radius.circular(height * 0.03),
        topRight: Radius.circular(height * 0.03),
      ),
      child: BottomAppBar(
        color: MyColors.getF2F2F2(),
        shape: const CircularNotchedRectangle(),
        notchMargin: 0,
        child: IconTheme(
          data: IconThemeData(color: Theme.of(context).colorScheme.onPrimary),
          child: Row(
            children: [
              Spacer(),
              IconButton(
                tooltip: "Logout öffnen",
                onPressed: () => showDialog(
                    context: context,
                    builder: (BuildContext context) {
                      return showLogoutDialog(context);
                    }),
                icon: Transform(
                  alignment: Alignment.center,
                  transform: Matrix4.rotationY(math.pi),
                  child: Icon(
                    Icons.logout,
                    color:
                        this.colorId == 1 ? MyColors.get6CA632() : Colors.black,
                    size: width * 0.08,
                  ),
                ),
              ),
              Spacer(),
              IconButton(
                tooltip: "Support öffnen",
                onPressed: () =>
                    Navigator.of(context).push(createRoute(Support())),
                icon: Icon(
                  Icons.info,
                  color:
                      this.colorId == 2 ? MyColors.get6CA632() : Colors.black,
                  size: width * 0.08,
                ),
              ),
              for (int i = 0; i < 5; i++) Spacer(),
              IconButton(
                tooltip: "Einkaufswagen öffnen",
                onPressed: () => print("Open shopping cart here"),
                icon: Icon(
                  Icons.shopping_cart,
                  color:
                      this.colorId == 3 ? MyColors.get6CA632() : Colors.black,
                  size: width * 0.08,
                ),
              ),
              Spacer(),
              IconButton(
                tooltip: "Konto",
                onPressed: () =>
                    Navigator.of(context).push(createRoute(Profile())),
                icon: Icon(
                  Icons.account_circle,
                  color:
                      this.colorId == 4 ? MyColors.get6CA632() : Colors.black,
                  size: width * 0.08,
                ),
              ),
              Spacer(),
            ],
          ),
        ),
      ),
    );
  }

  showLogoutDialog(BuildContext context) {
    return AlertDialog(
      title: Text("Abmelden"),
      content: Text("Wollen Sie sich von der App abmelden?"),
      actions: <Widget>[
        TextButton(
          onPressed: () {
            Navigator.of(context).pop(false);
          },
          child: const Text(
            "Nein",
            style: TextStyle(
              color: Colors.black,
            ),
          ),
        ),
        TextButton(
          onPressed: () {
            Navigator.of(context).push(createRoute(Login()));
          },
          child: const Text(
            "Ja",
            style: TextStyle(
              color: Colors.black,
            ),
          ),
        ),
      ],
    );
  }
}

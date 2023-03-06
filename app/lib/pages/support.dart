import 'package:flutter/material.dart';
import '../custom/myFloatingActionButton.dart';

import '../custom/myColors.dart';
import '../custom/myBottomNavBar.dart';
import '../custom/myAppBar.dart';

class Support extends StatelessWidget {
  const Support({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: const SupportPage(),
      bottomNavigationBar: MyBottomNavBar(2),
      floatingActionButton: floatingActionButton(context, width, false),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }
}

class SupportPage extends StatefulWidget {
  const SupportPage({super.key});

  @override
  State<SupportPage> createState() => _SupportPageState();
}

class _SupportPageState extends State<SupportPage> {
  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.05,
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
                "Support",
                style: TextStyle(
                  fontFamily: 'OpenSans',
                  fontSize: cardHeight * 0.07,
                  fontWeight: FontWeight.bold,
                  height: cardHeight * 0.004,
                  color: Colors.black,
                ),
              ),
              SizedBox(
                height: cardHeight * 0.15,
              ),
              Icon(
                Icons.email_outlined,
                size: pageWidth * 0.08,
              ),
              SizedBox(
                height: cardHeight * 0.07,
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
                height: cardHeight * 0.15,
              ),
              Icon(
                Icons.phone,
                size: pageWidth * 0.08,
              ),
              SizedBox(
                height: cardHeight * 0.07,
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
            ],
          ),
        ),
      ),
    );
  }
}

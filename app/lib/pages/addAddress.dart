import 'package:collew/pages/addresses.dart';
import 'package:http/http.dart';

import 'products.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../custom/myButton.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';
import '../custom/myBottomNavBar.dart';
import '../custom/myTextFormField.dart';
import '../custom/myFloatingActionButton.dart';

class AddAddress extends StatelessWidget {
  const AddAddress({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: const AddAddressPage(),
      bottomNavigationBar: MyBottomNavBar(4),
      floatingActionButton: floatingActionButton(context, width, false),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }
}

class AddAddressPage extends StatefulWidget {
  const AddAddressPage({super.key});

  @override
  State<AddAddressPage> createState() => _AddAddressPageState();
}

class _AddAddressPageState extends State<AddAddressPage> {
  final List<TextEditingController> controllers =
      List.generate(7, (index) => TextEditingController());

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.025,
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
    MyTextFormField city = MyTextFormField(
        "Stadt",
        "Stadt",
        TextInputType.text,
        controllers.elementAt(0),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField street = MyTextFormField(
        "Straße",
        "Straße",
        TextInputType.text,
        controllers.elementAt(1),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField streetNumber = MyTextFormField(
        "Hausnummer",
        "Hausnummer",
        TextInputType.number,
        controllers.elementAt(2),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField door = MyTextFormField(
        "Tür (optional)",
        "Tür",
        TextInputType.number,
        controllers.elementAt(3),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField staircase = MyTextFormField(
        "Stiege (optional)",
        "Stiege",
        TextInputType.number,
        controllers.elementAt(4),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField state = MyTextFormField(
        "Bundesland",
        "Bundesland",
        TextInputType.text,
        controllers.elementAt(5),
        false,
        pageWidth,
        pageHeight);
    MyTextFormField postcode = MyTextFormField(
        "Postleitzahl",
        "Postleitzahl",
        TextInputType.number,
        controllers.elementAt(6),
        false,
        pageWidth,
        pageHeight);
    MyButton addAddress = MyButton("Adresse hinzufügen", const Addresses(), controllers, false, "addLocation", context);
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
              city.getTextFormField(),
              street.getTextFormField(),
              streetNumber.getTextFormField(),
              door.getTextFormField(),
              staircase.getTextFormField(),
              state.getTextFormField(),
              postcode.getTextFormField(),
              SizedBox(
                height: cardHeight * 0.075,
              ),
              addAddress.getButtonWithSizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}

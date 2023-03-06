import 'package:collew/pages/addresses.dart';
import 'package:http/http.dart';

import '../api/apiCommunication.dart';
import '../api/location.dart';
import 'products.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../custom/myButton.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';
import '../custom/myBottomNavBar.dart';
import '../custom/myTextFormField.dart';
import '../custom/myFloatingActionButton.dart';

class AddProduct extends StatelessWidget {
  const AddProduct({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: const AddProductPage(),
      bottomNavigationBar: MyBottomNavBar(0),
      floatingActionButton: floatingActionButton(context, width, false),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }
}

class AddProductPage extends StatefulWidget {
  const AddProductPage({super.key});

  @override
  State<AddProductPage> createState() => _AddProductPageState();
}

class _AddProductPageState extends State<AddProductPage> {
  final List<TextEditingController> controllers =
      List.generate(4, (index) => TextEditingController());

  Widget dropdownButton = CircularProgressIndicator();
  late String dropdownValue = "";
  late double width, height, cardWidth, cardHeight;
  bool initialize = true;

  setSizes(double widthPar, double heightPar, double cardWidthPar,
      double cardHeightPar) {
    width = widthPar;
    height = heightPar;
    cardWidth = cardWidthPar;
    cardHeight = cardHeightPar;
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    setSizes(width, height, width * 0.8, height * 0.7);
    dropdownButton = getDropdownButton();
    return Column(
      children: [
        SizedBox(
          height: height * 0.025,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [materialCard()],
        ),
      ],
    );
  }

  Widget getDropdownButton() {
    return FutureBuilder(
        future: ApiCommunication.getAllLocations(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            createDropdownButton(snapshot.data!);
            return dropdownButton;
          }
          return const CircularProgressIndicator();
        });
  }

  createDropdownButton(List<Location> locations) {
    List<String> locationsForDropdownButton = [];
    for (Location location in locations) {
      locationsForDropdownButton
          .add("${location.getStreet()} ${location.getStreetNumber()}");
    }
    if (initialize) {
      dropdownValue = locationsForDropdownButton.elementAt(0);
      initialize = false;
    }
    dropdownButton = SizedBox(
        width: width * 0.65,
        height: height * 0.08,
        child: DropdownButton<String>(
          value: dropdownValue,
          icon: const Icon(Icons.arrow_downward),
          elevation: 16,
          onChanged: (String? value) {
            setState(() {
              dropdownValue = value!;
            });
          },
          items: locationsForDropdownButton
              .map<DropdownMenuItem<String>>((String value) {
            return DropdownMenuItem<String>(
              value: value,
              child: Text(value),
            );
          }).toList(),
        ));
  }

  materialCard() {
    MyTextFormField description = MyTextFormField(
        "Beschreibung",
        "Beschreibung",
        TextInputType.text,
        controllers.elementAt(0),
        false,
        width,
        height);
    MyTextFormField type = MyTextFormField("Produkttyp", "Produkttyp",
        TextInputType.text, controllers.elementAt(1), false, width, height);
    MyTextFormField brand = MyTextFormField("Hersteller", "Hersteller",
        TextInputType.text, controllers.elementAt(2), false, width, height);
    MyTextFormField model = MyTextFormField("Modell", "Modell",
        TextInputType.text, controllers.elementAt(3), false, width, height);
    MyButton addProduct = MyButton("Produkt hinzufügen", const Products(),
        controllers, false, "addProduct", context);
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
              description.getTextFormField(),
              type.getTextFormField(),
              brand.getTextFormField(),
              model.getTextFormField(),
              dropdownButton,
              SizedBox(
                height: cardHeight * 0.075,
              ),
              addProduct.getButtonWithSizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}

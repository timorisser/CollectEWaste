import 'package:collew/api/apiCommunication.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart';
import 'package:shared_preferences/shared_preferences.dart';

import '../api/location.dart';
import '../custom/myButton.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';
import '../custom/myBottomNavBar.dart';
import '../custom/myFloatingActionButton.dart';
import 'addAddress.dart';

class Addresses extends StatelessWidget {
  const Addresses({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: AddressesPage(),
      bottomNavigationBar: MyBottomNavBar(4),
      floatingActionButton: floatingActionButton(context, width, false),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }
}

class AddressesPage extends StatefulWidget {
  AddressesPage({super.key});

  @override
  State<AddressesPage> createState() => AddressesPageState();
}

class AddressesPageState extends State<AddressesPage> {
  AddressesPageState();
  List<Location> locations = [];
  late double width, height, cardWidth, cardHeight;
  Widget scrollbar = CircularProgressIndicator();
  String? locationsString = "";

  setLocations(List<Location> locationsParam) async {
    locations = locationsParam;
  }

  setSizes(double widthPar, double heightPar, double cardWidthPar,
      double cardHeightPar) {
    width = widthPar;
    height = heightPar;
    cardWidth = cardWidthPar;
    cardHeight = cardHeightPar;
  }

  @override
  Widget build(BuildContext context) {
    MyButton addAddress = MyButton(
        "Adresse hinzufügen", const AddAddress(), [], false, "", context);
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    setSizes(width, height, width * 0.8, height * 0.6);
    scrollbar = getScrollbar();
    return Column(
      children: [
        SizedBox(
          height: height * 0.076,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            materialCard(MyButton("Adresse hinzufügen", const AddAddress(), [],
                false, "", context))
          ],
        ),
      ],
    );
  }

  Widget getScrollbar() {
    return FutureBuilder(
        future: ApiCommunication.getAllLocations(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            createScrollbar(snapshot.data!);
            return scrollbar;
          }
          return const CircularProgressIndicator();
        });
  }

  createScrollbar(List<Location> currentLocations) {
    scrollbar = SizedBox(
      width: cardWidth * 0.8,
      height: cardHeight * 0.625,
      child: Scrollbar(
        thumbVisibility: true,
        child: SingleChildScrollView(
          child: Column(
            children: [
              for (Location location in currentLocations)
                Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        SizedBox(
                          width: cardWidth * 0.005,
                        ),
                        Text(
                          "${location.getStreet()} ${location.getStreetNumber()}",
                          style: TextStyle(
                            color: Colors.black,
                            fontSize: cardHeight * 0.04,
                          ),
                        ),
                        IconButton(
                          onPressed: () async {
                            await ApiCommunication.removeLocation(location);
                            currentLocations.remove(location);
                            setLocations(currentLocations);
                            setState(() => scrollbar = getScrollbar());
                          },
                          icon: Icon(
                            Icons.remove,
                            size: width * 0.08,
                          ),
                        ),
                        SizedBox(
                          width: cardWidth * 0.005,
                        ),
                      ],
                    ),
                    SizedBox(
                      height: cardHeight * 0.02,
                    ),
                  ],
                )
            ],
          ),
        ),
      ),
    );
  }

  SizedBox materialCard(MyButton addAddress) {
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
                "Adressen",
                style: TextStyle(
                  fontFamily: 'OpenSans',
                  fontSize: cardHeight * 0.07,
                  fontWeight: FontWeight.bold,
                  height: cardHeight * 0.004,
                  color: Colors.black,
                ),
              ),
              SizedBox(
                height: cardHeight * 0.05,
              ),
              scrollbar,
              SizedBox(
                height: cardHeight * 0.05,
              ),
              Expanded(
                child: Align(
                  alignment: FractionalOffset.bottomCenter,
                  child: Padding(
                    padding: EdgeInsets.only(bottom: 24.0),
                    child: addAddress.getButtonWithSizedBox(),
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

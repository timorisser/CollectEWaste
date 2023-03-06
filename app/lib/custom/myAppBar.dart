import 'package:collew/custom/myRoute.dart';
import 'package:collew/pages/addProduct.dart';
import 'package:flutter/material.dart';

AppBar createMyAppBar(BuildContext context) {
  double width = MediaQuery.of(context).size.width;
  double height = MediaQuery.of(context).size.height;
  return AppBar(
    toolbarHeight: height * 0.1,
    elevation: 0,
    leading: getLeading(context, height),
    title: Row(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        getImage(height),
        SizedBox(
          width: height * 0.075,
        )
      ],
    ),
  );
}

AppBar createMyAppBarForProducts(BuildContext context) {
  double width = MediaQuery.of(context).size.width;
  double height = MediaQuery.of(context).size.height;
  return AppBar(
    toolbarHeight: height * 0.1,
    elevation: 0,
    leading: getLeading(context, height),
    title: Row(
      children: [
        SizedBox(
          width: width * 0.26,
        ),
        getImage(height),
        SizedBox(
          width: width * 0.27,
        ),
        IconButton(
          icon: Icon(
            Icons.add,
            size: height * 0.04,
          ),
          onPressed: () => Navigator.of(context).push(createRoute(const AddProduct())),
        )
      ],
    ),
  );
}

Widget? getLeading(BuildContext context, double height) =>
    ModalRoute.of(context)?.canPop == true
        ? IconButton(
            icon: Icon(
              Icons.arrow_back,
              size: height * 0.04,
            ),
            onPressed: () => Navigator.of(context).pop(),
          )
        : null;

Image getImage(double height) => Image(
      image: const AssetImage('images/collew-logo.png'),
      fit: BoxFit.cover,
      height: height * 0.06,
    );

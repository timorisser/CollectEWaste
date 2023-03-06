import 'package:flutter/material.dart';

import 'myColors.dart';
import 'myRoute.dart';
import '../pages/products.dart';

floatingActionButton(BuildContext context, double width, bool onSite) {
  return Container(
    decoration: BoxDecoration(
        shape: BoxShape.circle,
          border: Border.all(
              color: MyColors.getBDD9A0(), width: 5, style: BorderStyle.solid)),
    child: FloatingActionButton(
      backgroundColor: MyColors.getF2F2F2(),
      onPressed: () => onSite
          ? print("already on site")
          : Navigator.of(context).push(createRoute(Products())),
      child: Icon(
        Icons.computer,
        color: onSite ? MyColors.get6CA632() : Colors.black,
        size: width * 0.08,
      ),
    ),
  );
}

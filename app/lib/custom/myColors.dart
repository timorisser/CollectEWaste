import 'package:flutter/material.dart';

class MyColors {
  static final _000000 = createMaterialColor(const Color(0xFF000000));
  static final _F2F2F2 = createMaterialColor(const Color(0xFFF2F2F2));
  static final _BDD9A0 = createMaterialColor(const Color(0xFFBDD9A0));
  static final _6CA632 = createMaterialColor(const Color(0xFF6CA632));

  static MaterialColor get000000() => _000000;
  static MaterialColor getF2F2F2() => _F2F2F2;
  static MaterialColor getBDD9A0() => _BDD9A0;
  static MaterialColor get6CA632() => _6CA632;
}

MaterialColor createMaterialColor(Color color) {
  List strengths = <double>[.05];
  Map<int, Color> swatch = {};
  final int r = color.red, g = color.green, b = color.blue;

  for (int i = 1; i < 10; i++) {
    strengths.add(0.1 * i);
  }
  for (var strength in strengths) {
    final double ds = 0.5 - strength;
    swatch[(strength * 1000).round()] = Color.fromRGBO(
      r + ((ds < 0 ? r : (255 - r)) * ds).round(),
      g + ((ds < 0 ? g : (255 - g)) * ds).round(),
      b + ((ds < 0 ? b : (255 - b)) * ds).round(),
      1,
    );
  }
  return MaterialColor(color.value, swatch);
}

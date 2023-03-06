import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class MyTextFormField {
  final String labelText;
  final String hintText;
  final TextInputType textInputType;
  final TextEditingController controller;
  final bool obscureText;
  final double width;
  final double height;

  MyTextFormField(this.labelText, this.hintText, this.textInputType,
      this.controller, this.obscureText, this.width, this.height);

  Widget getTextFormFieldWithSizedBox() {
    return Column(
      children: [
        SizedBox(
          width: width * 0.65,
          height: height * 0.08,
          child: TextFormField(
            decoration: InputDecoration(
              labelText: labelText,
              hintText: hintText,
            ),
            keyboardType: textInputType,
            controller: controller,
            obscureText: obscureText,
            maxLengthEnforcement: MaxLengthEnforcement.enforced,
          ),
        ),
        SizedBox(height: height * 0.014),
      ],
    );
  }

  Widget getTextFormField() {
    return SizedBox(
      width: width * 0.65,
      height: height * 0.08,
      child: TextFormField(
        decoration: InputDecoration(
          labelText: labelText,
          hintText: hintText,
        ),
        keyboardType: textInputType,
        controller: controller,
        obscureText: obscureText,
        maxLengthEnforcement: MaxLengthEnforcement.enforced,
      ),
    );
  }
}

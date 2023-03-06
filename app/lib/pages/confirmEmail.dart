import 'package:auto_size_text/auto_size_text.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import '../custom/myButton.dart';
import '../custom/myColors.dart';
import '../custom/myAppBar.dart';
import '../custom/myTextFormField.dart';

class ConfirmEmail extends StatelessWidget {
  const ConfirmEmail({super.key, required this.destination});
  final StatelessWidget destination;
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBar(context),
      body: ConfirmEmailPage(destination: destination),
    );
  }
}

class ConfirmEmailPage extends StatefulWidget {
  const ConfirmEmailPage({super.key, required this.destination});
  final StatelessWidget destination;

  @override
  State<ConfirmEmailPage> createState() => _ConfirmEmailPageState(destination);
}

class _ConfirmEmailPageState extends State<ConfirmEmailPage> {
  _ConfirmEmailPageState(StatelessWidget this.destination);
  final StatelessWidget destination;

  TextEditingController controller = TextEditingController();

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Column(
      children: [
        SizedBox(
          height: height * 0.16,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [materialCard(width, height, destination)],
        ),
      ],
    );
  }

  materialCard(
      double pageWidth, double pageHeight, StatelessWidget destination) {
    double cardWidth = pageWidth * 0.8;
    double cardHeight = pageHeight * 0.5;
    String text = "Ein Bestätigungs-Code wurde an example@gmail.com gesendet";
    MyTextFormField codeEingeben = MyTextFormField(
        "Code eingeben",
        "Code",
        TextInputType.number,
        controller,
        false,
        pageWidth,
        pageHeight);
    MyButton continueButton =
        MyButton("weiter", destination, [], false, "", context);
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
              SizedBox(
                height: cardHeight * 0.4,
                child: Container(
                  alignment: Alignment.center,
                  child: AutoSizeText(
                    text,
                    textAlign: TextAlign.center,
                    style: TextStyle(
                      color: Colors.black,
                      fontSize: cardHeight * 0.06,
                    ),
                  ),
                ),
              ),
              codeEingeben.getTextFormFieldWithSizedBox(),
              SizedBox(
                height: cardHeight * 0.18,
              ),
              continueButton.getButtonWithSizedBox(),
            ],
          ),
        ),
      ),
    );
  }
}

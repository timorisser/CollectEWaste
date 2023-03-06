import 'package:flutter/material.dart';

Route<Object?> createRoute(StatelessWidget destination) {
  return PageRouteBuilder(
      pageBuilder: ((context, animation, secondaryAnimation) => destination),
      transitionsBuilder: ((context, animation, secondaryAnimation, child) {
        Animatable<Offset> animatable =
            Tween(begin: Offset(0.0, 1.0), end: Offset.zero).chain(CurveTween(
          curve: Curves.ease,
        ));
        return SlideTransition(
          position: animation.drive(animatable),
          child: child,
        );
      }));
}

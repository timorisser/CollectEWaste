import 'dart:convert';

import 'package:flutter/material.dart';

class Product {
  final String? description,
      type,
      brand,
      model,
      status,
      reservee,
      collect_appointment;
  final int? owner, location, distance;

  Product(
      {this.description,
      this.type,
      this.brand,
      this.model,
      this.status,
      this.reservee,
      this.collect_appointment,
      this.owner,
      this.location,
      this.distance});

  factory Product.fromJson(Map<String, dynamic> jsonData) {
    return Product(
        description: jsonData["description"],
        status: jsonData["status"],
        reservee: jsonData["reservee"],
        collect_appointment: jsonData["collect_appointment"],
        type: jsonData["product"]["type"],
        brand: jsonData["product"]["brand"],
        model: jsonData["product"]["model"],
        owner: jsonData["owner"],
        location: jsonData["location"],
        distance: jsonData["distance"]);
  }

  static Map<String, dynamic> toMap(Product product) => {
        "description": product.description,
        "status": product.status,
        "reservee": product.reservee,
        "collect_appointment": product.collect_appointment,
        "type": product.type,
        "brand": product.brand,
        "model": product.model,
        "owner": product.owner,
        "location": product.location,
        "distance": product.distance,
      };

  static String encode(List<Product> products) => json.encode(
        products
            .map<Map<String, dynamic>>((music) => Product.toMap(music))
            .toList(),
      );

  static List<Product> decode(String products) =>
      (json.decode(products) as List<dynamic>)
          .map<Product>((item) => Product.fromJson(item))
          .toList();
}

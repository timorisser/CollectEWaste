import 'dart:async';
import 'dart:convert';

import 'package:collew/api/product.dart';
import 'package:collew/custom/myButton.dart';
import 'package:collew/pages/changePassword.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

import '../pages/login.dart';
import 'location.dart';

class ApiCommunication {
  late MyButton buttonForApi;
  ApiCommunication(this.buttonForApi);

  sendRequest(List<String> data, bool is_company, String request,
      BuildContext context) {
    debugPrint("API request sent");
    createRequest(data, is_company, request);
    //showLoaderDialog(context);
  }

  createRequest(List<String> data, bool is_company, String request) {
    switch (request) {
      case "register":
        register(data, is_company);
        break;
      case "login":
        login(data);
        break;
      case "changePassword":
        changePassword(data);
        break;
      case "addLocation":
        addLocation(data);
        break;
      case "addProduct":
        addProduct(data);
        break;
      default:
        break;
    }
  }

  register(List<String> data, bool is_company) async {
    data = [
      "firstname",
      "lastname",
      "jojo1.bauer@gmail.com",
      "123456789",
      "strongpassword",
      "strongpassword"
    ];
    final response = await http.post(
      Uri.parse('http://10.0.2.2:8080/api/v1/account/register'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode(<String, Object>{
        "firstname": data.elementAt(0),
        "lastname": data.elementAt(1),
        "email": data.elementAt(2),
        "phonenumber": data.elementAt(3),
        "password": data.elementAt(4),
        "is_company": is_company,
        "companyName": data.elementAt(5),
      }),
    );
    buttonForApi.apiRequestFinished(response.statusCode == 200);
  }

  login(List<String> data) async {
    data = ["firstnamelastname@email.ru", "strongpassword"];
    final response = await http.post(
      Uri.parse('http://10.0.2.2:8080/api/v1/account/login'),
      headers: {"Content-Type": "application/json"},
      body: jsonEncode(<String, Object>{
        "email": data.elementAt(0),
        "password": data.elementAt(1),
      }),
    );
    if (response.statusCode == 200) {
      String token = json.decode(response.body)["token"];
      final prefs = await SharedPreferences.getInstance();
      prefs.setString("token", token);
      debugPrint(token);
    }
    buttonForApi.apiRequestFinished(response.statusCode == 200);
  }

  changePassword(List<String> data) async {
    data = [
      "newpassword",
      "newpassword",
      "strongpassword",
    ];
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString("token");
    final response = await http.patch(
      Uri.parse("http://10.0.2.2:8080/api/v1/account/changePassword"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer ${token!}"
      },
      body: jsonEncode(<String, Object>{
        "newpassword": data.elementAt(0),
        "matchingpassword": data.elementAt(1),
        "oldpassword": data.elementAt(2),
      }),
    );
    buttonForApi.apiRequestFinished(response.statusCode == 200);
  }

  static Future<List<dynamic>> getAllLocationsFromApi() async {
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString("token");
    final response = await http.get(
      Uri.parse("http://10.0.2.2:8080/api/v1/account/location"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer ${token!}"
      },
    );
    return json.decode(utf8.decode(response.bodyBytes));
  }

  static Future<List<Location>> getAllLocations() async {
    List<Location> locations = [];
    List<dynamic> responseJson = await getAllLocationsFromApi();
    for (dynamic elementJson in responseJson) {
      locations.add(Location.fromJson(elementJson));
    }
    return locations;
  }

  static Future<int> getLocationId(Location location) async {
    List<dynamic> responseJson = await getAllLocationsFromApi();
    for (dynamic elementJson in responseJson) {
      if (Location.compareTwoLocations(
          Location.fromJson(elementJson), location)) {
        return elementJson["id"];
      }
    }
    return -1;
  }

  addLocation(List<String> data) async {
    data = [
      "Wien",
      "Jägerstraße",
      data.elementAt(2),
      "1",
      "23",
      "Wien",
      "1220"
    ];
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString("token");
    final response = await http.post(
      Uri.parse("http://10.0.2.2:8080/api/v1/account/location"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer ${token!}"
      },
      body: jsonEncode(<String, Object>{
        "location": data.elementAt(0),
        "street": data.elementAt(1),
        "streetNumber": data.elementAt(2),
        "door": data.elementAt(3),
        "stair": data.elementAt(4),
        "state": data.elementAt(5),
        "postcode": data.elementAt(6),
      }),
    );
    debugPrint("${response.statusCode}");
    buttonForApi.apiRequestFinished(response.statusCode == 200);
  }

  static removeLocation(Location location) async {
    int locationId = await getLocationId(location);
    debugPrint("$locationId");
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString("token");
    final response = await http.delete(
      Uri.parse("http://10.0.2.2:8080/api/v1/account/location/$locationId"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer ${token!}"
      },
    );
  }

  static Future<List<dynamic>> getAllProductsFromApi() async {
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString("token");
    final response = await http.get(
      Uri.parse("http://10.0.2.2:8080/api/v1/product"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer ${token!}"
      },
    );
    return json.decode(utf8.decode(response.bodyBytes));
  }

  static Future<List<Product>> getAllProducts() async {
    List<Product> products = [];
    List<dynamic> responseJson = await getAllProductsFromApi();
    for (dynamic elementJson in responseJson) {
      products.add(Product.fromJson(elementJson));
    }
    return products;
  }

  addProduct(List<String> data) async {
    List<Location> locations = await getAllLocations();
    data = [
      "Mein tolles Handy",
      "Smartphone",
      "Apple",
      "iPhone 14",
      "${locations.elementAt(0).id}"
    ];
    Map<String, String> productDetails = {
      "type": data.elementAt(1),
      "brand": data.elementAt(2),
      "model": data.elementAt(3)
    };
    final prefs = await SharedPreferences.getInstance();
    String? token = prefs.getString("token");
    final response = await http.post(
      Uri.parse("http://10.0.2.2:8080/api/v1/product"),
      headers: {
        "Content-Type": "application/json",
        "Authorization": "Bearer ${token!}"
      },
      body: jsonEncode(<String, Object>{
        "description": data.elementAt(0),
        "product": productDetails,
        "location": int.parse(data.elementAt(4))
      }),
    );
    debugPrint("${response.statusCode}");
    buttonForApi.apiRequestFinished(response.statusCode == 200);
  }

  showLoaderDialog(BuildContext context) {
    AlertDialog alert = AlertDialog(
      content: Row(
        children: [
          const CircularProgressIndicator(),
          Container(
              margin: const EdgeInsets.only(left: 7),
              child: const Text("Loading...")),
        ],
      ),
    );
    showDialog(
      barrierDismissible: false,
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }
}

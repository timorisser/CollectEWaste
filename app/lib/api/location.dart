import 'dart:convert';

import 'package:collew/pages/addresses.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Location {
  final int? id;
  final String location, street, state;
  final int streetNumber, door, stair, postcode;

  Location({
    this.id,
    required this.location,
    required this.street,
    required this.streetNumber,
    required this.door,
    required this.stair,
    required this.state,
    required this.postcode,
  });

  factory Location.fromJson(Map<String, dynamic> jsonData) {
    return Location(
      id: jsonData["id"],
      location: jsonData["location"],
      street: jsonData["street"],
      streetNumber: jsonData["streetNumber"],
      door: jsonData["door"],
      stair: jsonData["stair"],
      state: jsonData["state"],
      postcode: jsonData["postcode"],
    );
  }

  static Map<String, dynamic> toMap(Location location) => {
        "id": location.id,
        "location": location.location,
        "street": location.street,
        "streetNumber": location.streetNumber,
        "door": location.door,
        "stair": location.stair,
        "state": location.state,
        "postcode": location.postcode,
      };

  static String encode(List<Location> locations) => json.encode(
        locations
            .map<Map<String, dynamic>>((music) => Location.toMap(music))
            .toList(),
      );

  static List<Location> decode(String locations) =>
      (json.decode(locations) as List<dynamic>)
          .map<Location>((item) => Location.fromJson(item))
          .toList();

  static bool compareTwoLocations(Location location1, Location location2) {
    return location1.location == location2.location &&
        location1.street == location2.street &&
        location1.streetNumber == location2.streetNumber &&
        location1.door == location2.door &&
        location1.stair == location2.stair &&
        location1.state == location2.state &&
        location1.postcode == location2.postcode;
  }

  String getStreet() {
    return street;
  }

  int getStreetNumber() {
    return streetNumber;
  }
}

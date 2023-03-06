import 'package:collew/custom/myAppBar.dart';
import 'package:flutter/material.dart';

import '../api/apiCommunication.dart';
import '../api/product.dart';
import '../custom/myColors.dart';
import '../custom/myBottomNavBar.dart';
import '../custom/myFloatingActionButton.dart';

class Products extends StatelessWidget {
  const Products({super.key});

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    return Scaffold(
      backgroundColor: MyColors.getBDD9A0(),
      appBar: createMyAppBarForProducts(context),
      body: const ProductsPage(),
      bottomNavigationBar: MyBottomNavBar(0),
      floatingActionButton: floatingActionButton(context, width, true),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerDocked,
    );
  }
}

class ProductsPage extends StatefulWidget {
  const ProductsPage({super.key});

  @override
  State<ProductsPage> createState() => _ProductsPageState();
}

class _ProductsPageState extends State<ProductsPage> {
  Widget productsView = CircularProgressIndicator();
  late double width, height, cardWidth, cardHeight;
  setSizes(double widthPar, double heightPar, double cardWidthPar,
      double cardHeightPar) {
    width = widthPar;
    height = heightPar;
    cardWidth = cardWidthPar;
    cardHeight = cardHeightPar;
  }

  @override
  Widget build(BuildContext context) {
    double width = MediaQuery.of(context).size.width;
    double height = MediaQuery.of(context).size.height;
    double cardWidth = width * 0.41;
    double cardHeight = height * 0.25;
    setSizes(width, height, width * 0.8, height * 0.6);
    return getProducts(true);
  }

  getProducts(bool initialize) {
    return FutureBuilder(
        future: ApiCommunication.getAllProducts(),
        builder: (context, snapshot) {
          if (snapshot.hasData) {
            createProductsView(snapshot.data!);
            return productsView;
          }
          return const CircularProgressIndicator();
        });
  }

  createProductsView(List<Product> products) {
    List<Widget> productsWidgetList = [];
    for (int i = 0; i < products.length; i++) {
      productsWidgetList.add(
        Row(
          children: [
            SizedBox(
              width: width * 0.06,
            ),
            materialCard(products.elementAt(i)),
            SizedBox(
              width: width * 0.06,
            ),
            materialCard(products.elementAt(i + 1)),
            SizedBox(
              width: width * 0.06,
            ),
          ],
        ),
      );
      i++;
    }
    productsView = SingleChildScrollView(
      child: Column(
        children: [
          for (Widget productWidget in productsWidgetList) productWidget,
          SizedBox(
            height: cardHeight * 0.12,
          ),
        ],
      ),
    );
  }

  Widget materialCard(Product product) {
    cardWidth = width * 0.41;
    cardHeight = height * 0.25;
    return Column(
      children: [
        SizedBox(
          height: cardHeight * 0.12,
        ),
        SizedBox(
          width: cardWidth,
          height: cardHeight,
          child: Container(
            decoration: const BoxDecoration(
              boxShadow: [
                BoxShadow(
                  color: Color.fromRGBO(0, 0, 0, 0.05),
                  blurRadius: 5.0,
                ),
              ],
            ),
            child: Card(
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(15.0),
              ),
              child: Center(
                child: Column(
                  children: [
                    SizedBox(
                      height: cardHeight * 0.07,
                    ),
                    Image(
                      image: const AssetImage('images/collew-logo.png'),
                      fit: BoxFit.fitHeight,
                      width: cardWidth * 0.8,
                      height: cardHeight * 0.4,
                    ),
                    SizedBox(
                      height: cardHeight * 0.07,
                    ),
                    SizedBox(
                      width: cardWidth * 0.8,
                      height: cardHeight * 0.2,
                      child: Text(
                        product.type!,
                        style: const TextStyle(
                          color: Colors.black,
                        ),
                      ),
                    ),
                    SizedBox(
                      width: cardWidth * 0.8,
                      height: cardHeight * 0.2,
                      child: Text(
                        "${product.distance}m",
                        style: const TextStyle(
                          color: Colors.black,
                        ),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ),
        ),
      ],
    );
  }
}

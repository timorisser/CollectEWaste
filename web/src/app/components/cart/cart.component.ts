import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from 'src/app/interfaces/product';
import { ProductService } from 'src/app/services/product.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss']
})
export class CartComponent implements OnInit {

  // categories: string[] = [];
  reservedProducts: Product[] = [];
  offeredProducts: Product[] = [];

  constructor(private productService: ProductService, private router: Router, public userService: UserService, private location: Location) {
    // this.categories = this.productService.categories;
    // this.reservedProducts = this.productService.reservedProducts;
    // this.offeredProducts = this.productService.products.filter((product) => {
    //   return product.owner == this.userService.user.email;
    // }); 
    this.productService.getProducts("available", "owner", "time").subscribe((data) => {
      console.log("owner");
      
      console.log(data);
      
      this.offeredProducts = data;
    });
  }

  ngOnInit(): void {
    this.productService.getProducts("available", "owner", "time").subscribe((data) => {
      this.offeredProducts = data;
    });
    localStorage.setItem('lastLocation', this.location.path());
    window.scrollTo(0, 0)
  }

}

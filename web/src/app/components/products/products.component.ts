import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from 'src/app/interfaces/product';
import { ProductService } from 'src/app/services/product.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss']
})
export class ProductsComponent implements OnInit {

  // productLink: Object = {
  //   product: {},
  //   link: ""
  // }
  products: Product[] = [];
  // categories: string[] = [];

  constructor(public productService: ProductService, private router: Router, private userService: UserService, private location: Location) {
    // this.products = this.productService.products.filter((product) => {
    //   return product.owner != this.userService.user.email;
    // });   
    // this.categories = this.productService.categories;
    this.productService.getProducts("available", "search", "time").subscribe((data) => {
      // this.products = data;
      this.productService.setImgForProduct(data);
    });
  }

  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
    window.scrollTo(0, 0)
  }

  toAddProducts() {
    this.router.navigate(['/add-product']);
  }

  toProductDetail(id: number) {
    localStorage.setItem('pid', `${id}`);
    this.router.navigate([`/product-detail/${id}`]);
  }


  // reserve(id: number) {
  //   let pro: Product;
  //   if (id) {
  //     this.productService.reservedProducts.push(
  //       pro = this.products.find(product => {
  //         return product.id == id;
  //       })!
  //     );
  //     console.log(pro);

  //     this.products = this.products.filter(product => {
  //       return product != pro;
  //     });
  //     this.productService.products = this.productService.products.filter(product => {
  //       return product != pro;
  //     });

  //   }
  // }

  resizeImage(event: Event): void {
    const img = event.target as HTMLImageElement;
    if (img.width > 500) { // Setze hier die gewünschte maximale Breite des Bildes ein
      img.width = 500;
      img.height = (500 / img.width) * img.height;
    }

      if (img.width < 350) {
        const card = img.closest('.card') as HTMLElement;
        card.style.minWidth = img.width + 'px';
      }
  }


}

import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { Address } from 'src/app/interfaces/address';
import { Appointment } from 'src/app/interfaces/appointment';
import { Product } from 'src/app/interfaces/product';
import { User } from 'src/app/interfaces/user';
import { AddressService } from 'src/app/services/address.service';
import { AppointmentService } from 'src/app/services/appointment.service';
import { ProductService } from 'src/app/services/product.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss']
})
export class ProductDetailComponent implements OnInit {

  product: Product = {
    description: '',
    product: {
      type: '',
      brand: '',
      model: ''
    },
    location: 0
  };

  owner: User = {};
  ownerAddress: Address = {
    street: '',
    streetNumber: '',
    state: '',
    postcode: 0,
    location: ''
  };
  appointments: Appointment[] = [];
  selectedAppointmentId: number = 0;

  url: string = "";

  slides: string[] = [];

  constructor(private productService: ProductService, public userService: UserService, private addressService: AddressService, private appointmentService: AppointmentService, private location: Location, private router: Router) {
    // console.log(this.productService.selectedProductId);

  }

  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
    const lastLocation = localStorage.getItem('lastLocation');
    let pid = Number(localStorage.getItem('pid'));
    const lastLocationPid = Number(lastLocation?.slice(lastLocation.lastIndexOf('/') + 1));

    if (lastLocationPid != pid) {
      localStorage.setItem('pid', `${lastLocationPid}`);
      pid = lastLocationPid;
    }
    this.getDetailProductData(pid);
  }

  reserveProduct() {
    this.productService.setReservation(this.product.id!, this.selectedAppointmentId).subscribe((data) => location.reload());
  }

  undoReservation() {
    this.productService.undoReservation(this.product.id!).subscribe((data) => {
      this.router.navigate(['/products']);
      this.showSnackbar();
    });
  }

  getDetailProductData(pid: number) {
    this.productService.getProduct(pid).subscribe((data) => {
      console.log(data);

      this.product = data;
      this.productService.getProductImages(this.product.id!).subscribe((data) => {
        console.log(data);

        this.slides = data;
      });
      this.userService.getUserWithId(Number(this.product.owner)).subscribe((data) => {
        console.log(data);
        this.owner = data
      });
      this.addressService.getOneLocation(this.product.location).subscribe((data) => {
        console.log(data);
        this.ownerAddress = data;

        this.appointmentService.getAppointmentsFromLocation(this.ownerAddress.id!).subscribe((data) => {
          console.log(data);
          this.appointments = data;
        });
      });
    });
  }

  showSnackbar() {
    this.productService.openSnackBar('Reservierung wurde rückgängig gemacht!', 'Close');
  }

  goBack() {
    this.location.back();
  }
}

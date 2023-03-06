import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Product } from '../../interfaces/product';
import { ProductService } from 'src/app/services/product.service';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { Address } from 'src/app/interfaces/address';
import { AddressService } from 'src/app/services/address.service';
import { AppointmentService } from 'src/app/services/appointment.service';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.scss']
})
export class AddProductComponent implements OnInit {

  productValid: boolean = true;

  files: File[] = [];

  types: string[] = [];
  brands: string[] = [];
  models: string[] = [];

  addresses: Address[] = [];
  address: Address = {
    street: '',
    streetNumber: '',
    state: '',
    postcode: 0,
    location: ''
  }

  appointmentLength: number = undefined!;

  checked: boolean = false;

  constructor(public productService: ProductService, private router: Router, private userService: UserService, private location: Location, private addressService: AddressService, private appointmentService: AppointmentService) {
    // this.types = this.productService.types;
    // this.brands = this.productService.brands;
    // this.models = this.productService.models;
  }

  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
    this.productService.getTypes().subscribe((data) => {
      this.types = data;
    });
    this.productService.getBrandsNo().subscribe((data) => {
      this.brands = data;
    });
    this.productService.getModelsNo().subscribe((data) => {
      this.models = data;
    });
    this.getAddresses();
  }

  goBack() {
    this.productService.product.description = "";
    this.location.back();
  }

  // toChooseLocationAndAppointments() {
  //   console.log(this.productService.product);

  //   this.router.navigate(['/chooseLocationAndAppointment']);
  // }

  onFilesSelect(event: any) {
    this.files = event.target.files
    if (this.files.length > 10) {
      alert('Man kann nur 10 Bilder hochladen');
      this.files = [];
    } else {
      this.productService.files = this.files;
      console.log(this.productService.files);
    }
  }

  getBrands(event: any) {
    this.productService.getBrands(event.value).subscribe((data) => {
      this.brands = data;
    });
  }

  getModels(event: any) {
    this.productService.getModels(this.productService.product.product.type, event.value).subscribe((data) => {
      this.models = data;
    });
  }

  getAddresses() {
    this.addressService.getLocation().subscribe((data) => {
      this.addresses = data;
    });
  }

  isSchedulerEmpty() {
    if (this.address) {
      this.appointmentService.getAppointmentsFromLocation(this.address.id!).subscribe((data) => {
        this.appointmentLength = data.length;
      });
    }
  }

  toScheduler() {
    this.router.navigate(['/scheduler']);
  }

  toProfile() {
    this.router.navigate([`/profile/${this.userService.user.id}`]);
  }

  addProduct() {
    this.productService.product.location = this.address.id!;
    this.productService.addProduct().subscribe((data) => {
      for (let i = 0; i < this.productService.files.length; i++) {
        const file = this.productService.files[i];
        console.log(data.id);
        this.productService.uploadProductImg(data.id, file, `${i}`).subscribe((data) => {
          console.log("Link:" + data);
        });
      }
    });
  }
}

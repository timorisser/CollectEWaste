import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Address } from 'src/app/interfaces/address';
import { Appointment } from 'src/app/interfaces/appointment';
import { AddressService } from 'src/app/services/address.service';
import { AppointmentService } from 'src/app/services/appointment.service';
import { ProductService } from 'src/app/services/product.service';
import { AppointmentDialogComponent } from '../appointment-dialog/appointment-dialog.component';

@Component({
  selector: 'app-choose-location-and-appointments',
  templateUrl: './choose-location-and-appointments.component.html',
  styleUrls: ['./choose-location-and-appointments.component.scss']
})
export class ChooseLocationAndAppointmentsComponent implements OnInit {

  productValid: boolean = true;

  

  appointments: Appointment[] = [];
  

  constructor(private addressService: AddressService, private router: Router, private dialog: MatDialog, private location: Location, private appointmentService: AppointmentService, private productService: ProductService) {
  }

  ngOnInit(): void {
  }

  

  // addAppointment()

  goBack() {
    this.location.back();
  }

  toProducts() {
    this.router.navigate(['/products']);
  }

  
  // addProduct() {
  //   this.productService.product.location = this.address.id!;
  //   this.productService.addProduct().subscribe((data) => {
  //     for (let i = 0; i<this.productService.files.length; i++) {
  //       const file = this.productService.files[i];
  //       console.log(data.id);

        
  //       this.productService.uploadProductImg(data.id, file, `${i}`).subscribe((data) => {
  //         console.log("Link:" + data);
  //       });
  //     }
  //   });
  // }  

}

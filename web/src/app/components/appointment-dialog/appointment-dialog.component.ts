import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Address } from 'src/app/interfaces/address';
import { AddressService } from 'src/app/services/address.service';
import { AppointmentService } from 'src/app/services/appointment.service';


@Component({
  selector: 'app-appointment-dialog',
  templateUrl: './appointment-dialog.component.html',
  styleUrls: ['./appointment-dialog.component.scss']
})
export class AppointmentDialogComponent implements OnInit {

  startTime: string = "";
  endTime: string = "";
  day: string = "";
  days: string[] = [];

  schedulerValid: boolean = true;
  errorText: string = "";

  addresses: Address[] = [];

  address: Address = {
    street: '',
    streetNumber: '',
    state: '',
    postcode: 0,
    location: ''
  }

  constructor(public dialogRef: MatDialogRef<AppointmentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, private appointmentService: AppointmentService, private addressService: AddressService) {
    dialogRef.disableClose = true;
    this.days = this.appointmentService.days;
    this.getAddresses();
  }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  getAddresses() {
    this.addressService.getLocation().subscribe((data) => {
      this.addresses = data;
    });
  }

  // checkTime(event: any) {
  //   if (event.target.validity.patternMismatch) {
  //     this.errorText = "Das ist keine Uhrzeit";
  //   }
  // }
}

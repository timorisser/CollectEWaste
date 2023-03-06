import { Component, OnInit } from '@angular/core';
import { Address } from 'src/app/interfaces/address';
import { Appointment } from 'src/app/interfaces/appointment';
import { AddressService } from 'src/app/services/address.service';
import { AppointmentService } from 'src/app/services/appointment.service';
import { forkJoin } from 'rxjs';
import { AppointmentDialogComponent } from '../appointment-dialog/appointment-dialog.component';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-scheduler',
  templateUrl: './scheduler.component.html',
  styleUrls: ['./scheduler.component.scss']
})
export class SchedulerComponent implements OnInit {

  addresses: Address[] = [];
  addressWithAppointments: Map<Address, Appointment[]> = new Map();

  appointment: Appointment = {
    locationid: 0,
    day: '',
    startTime: '',
    endTime: ''
  }



  constructor(private addressService: AddressService, private appointmentService: AppointmentService, private dialog: MatDialog, private router: Router, private location: Location) { }

  ngOnInit(): void {
    this.addressService.getLocation().subscribe((data) => {
      this.addresses = data;
      const requests = this.addresses.map((address) => {
        this.appointmentService.addAppointmentObservable.subscribe((data) => {})
        return this.appointmentService.getAppointmentsFromLocation(address.id!);
      });
      forkJoin(requests).subscribe((results) => {
        results.forEach((appointments, index) => {
          this.addressWithAppointments.set(this.addresses[index], appointments);
        });
      });
    });
    this.appointmentService.addAppointmentObservable.subscribe((data) => {
      console.log('Appointment added:', data);
      const requests = this.addresses.map((address) => {
        return this.appointmentService.getAppointmentsFromLocation(address.id!);
      });
      forkJoin(requests).subscribe((results) => {
        results.forEach((appointments, index) => {
          this.addressWithAppointments.set(this.addresses[index], appointments);
        });
      });
    });
  }

  deleteAppointment(address: Address, appointmentId: number) {
    if (address && appointmentId) {
      let appointments = this.addressWithAppointments.get(address);
      this.addressWithAppointments.set(address, appointments!.filter((appointment) => {
        return appointment.id != appointmentId;
      }));
      this.appointmentService.deleteAppointment(appointmentId).subscribe();
    }
  }

  openAppointmentDialog(): void {
    // this.currentImageField = event;
    const dialogRef = this.dialog.open(AppointmentDialogComponent, {
      width: '300px',
      height: '500px',
      data: {}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log("TIME: " + result.day + " von " + result.startTime + " bis " + result.endTime + " " + result.address.id);
        this.appointment = {
          locationid: result.address.id!,
          day: result.day,
          startTime: result.startTime + ":00",
          endTime: result.endTime + ":00"
        };
        // this.appointments.push(this.appointment);
        this.addAppointment(result.address);
      } 
    });
  }

  addAppointment(address: Address) {
    this.appointmentService.addAppointment(this.appointment).subscribe((data) => {
      console.log(data);
      let appointments = this.addressWithAppointments.get(address);
      appointments?.push(data.slice(-1)[0]);
      this.addressWithAppointments.set(address, appointments!);
    });
  }

  toAddAddress() {
    this.router.navigate(['/add-address']);
  }

  goBack() {
    this.location.back();
  }

}

import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Address } from 'src/app/interfaces/address';
import { AddressService } from 'src/app/services/address.service';
import { UserService } from 'src/app/services/user.service';
import { ProfilComponent } from '../profil/profil.component';

@Component({
  selector: 'app-add-address',
  templateUrl: './add-address.component.html',
  styleUrls: ['./add-address.component.scss']
})
export class AddAddressComponent implements OnInit {

  addressValid: boolean = true;
  address: Address = {
    location: '',
    street: '',
    streetNumber: '',
    door: '',
    stair: '',
    state: '',
    postcode: null!,

  };

  states: String[] = [
    "Burgenland",
    "Kärnten",
    "Niederösterreich",
    "Oberösterreich",
    "Salzburg",
    "Steiermark",
    "Tirol",
    "Vorarlberg",
    "Wien"
  ];

  constructor(private router: Router, private userService: UserService, private addressSerivce: AddressService, private location: Location) { }

  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
  }

  toProfile() {
    this.router.navigate([`/profile/${this.userService.user.id}`])
  }

  addAddress() {
    console.log(this.address.postcode); 
    this.addressSerivce.addLocation(this.address).subscribe((data) => {
      console.log("ADRESSE: " + data);
    });
  }

}

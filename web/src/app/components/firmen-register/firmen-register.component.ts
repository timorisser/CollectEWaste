import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-firmen-register',
  templateUrl: './firmen-register.component.html',
  styleUrls: ['./firmen-register.component.scss']
})
export class FirmenRegisterComponent implements OnInit {

  registerValid: boolean = true;
  firstname: string = "";
  lastname: string = "";
  email: string = "";
  phonenumber: string = "";
  password: string = "";
  company: string = "";

  hide1: boolean = true;
  hide2: boolean = true;

  confirmPassword: string = "";
  confirmedPassword: boolean = false;
  validEmail: boolean = true;
  validPhonenumber: boolean = true;

  constructor(public authService: AuthService, private router: Router, private location: Location) {
    this.authService.emailForm.reset();
  }

  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
  }

  toLogin() {
    this.router.navigate(['/login']);
  }

  register() {
    if (this.authService.passwordConfirmation(this.password, this.confirmPassword) && !this.authService.getErrorMessage() && this.checkPhonenumber()) {
      this.authService.register({ firstname: this.firstname, lastname: this.lastname, email: this.email, phonenumber: this.phonenumber, password: this.password, isCompany: true, companyName: this.company }).subscribe((data) => {
        console.log(data);
      })
      this.router.navigate(['/login']);
    }
  }

  // checkEmail() {
  //   const regexExp = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/gi;
  //   if (regexExp.test(this.email)) {
  //     return this.validEmail = true;
  //   }
  //   this.email = "";
  //   return this.validEmail = false;

  // }

  // passwordConfirmation() {
  //   if (this.confirmPassword == this.password) {
  //     return this.confirmedPassword = true;
  //   } else {
  //     this.confirmPassword = "";
  //     return this.confirmedPassword = false;
  //   }
  // }

  checkPhonenumber() {
    const phonenumberPattern = /^[+|0]?[1-9][0-9]{7,14}$/;
    if (phonenumberPattern.test(this.phonenumber)) {
      return this.validPhonenumber = true;
    }
    this.phonenumber = "";
    return this.validPhonenumber = false;
  }


}



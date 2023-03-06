import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {


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
      this.authService.register({
        firstname: this.firstname,
        lastname: this.lastname,
        email: this.email,
        phonenumber: this.phonenumber,
        password: this.password,
        isCompany: false,
        companyName: ""
      }).subscribe((data) => {
        console.log(data);
      });
      // this.authService.
      this.router.navigate(['/login']);
    }
  }

  // checkEmail() {
  //   if (this.authService.checkEmail(this.email)) {
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
    if (this.authService.checkPhonenumber(this.phonenumber)) {
      return this.validPhonenumber = true;
    }
    this.phonenumber = "";
    return this.validPhonenumber = false;
  }


}


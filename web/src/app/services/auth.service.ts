import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  readonly URL: string = "http://localhost:8080/api/v1/account";

  token: string = undefined!;
  isAuth: boolean = false;
  emailForm = new FormControl('', [Validators.required, Validators.email]);

  confirmedPassword: boolean = false;

  constructor(private http: HttpClient) { }

  getErrorMessage() {
    if (this.emailForm.hasError('required')) {
      return 'Bitte Email eingeben!';
    }

    return this.emailForm.hasError('email') ? 'Keine gültige Email!' : '';
  }

  login(user: User): Observable<any> {
    return this.http.post(`${this.URL}/login`, user);
  }

  register(user: User): Observable<any> {
    return this.http.post(`${this.URL}/register`, user);
  }


  // checkEmail(email: string) {
  //   const emailPattern = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/gi;
  //   if (emailPattern.test(email)) {
  //     return true;
  //   }
  //   // this.email = "";
  //   return false;
  // }

  checkPhonenumber(phonenumber: string) {
    const phonenumberPattern = /^[+|0]?[1-9][0-9]{7,14}$/;
    if (phonenumberPattern.test(phonenumber)) {
      return true;
    }
    // this.phonenumber = "";
    return false;
  }

  passwordConfirmation(password: string, confirmPassword: string) {
    if (password == confirmPassword) {
      return this.confirmedPassword = true;
    } else {
      // this.confirmPassword = "";
      return this.confirmedPassword = false;
    }
  }
}

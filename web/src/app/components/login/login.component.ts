import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginValid: boolean = true;
  email: string = '';
  password: string = '';
  clicked: number = 0;

  hide: boolean = true;

  constructor(public authService: AuthService, private router: Router, private userService: UserService, private snackBar: MatSnackBar, private location: Location) {
    this.authService.emailForm.reset();
  }

  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
  }

  login(mail: string, pwd: string) {
    if (!this.authService.getErrorMessage()) {
      this.authService.login({ email: mail, password: pwd }).subscribe((data) => {
        console.log(data);

        if (data.token) {
          // this.userService.setEmail(mail);
          localStorage.setItem('token', data.token);
          localStorage.setItem('id', data.id);
          localStorage.setItem('email', data.email);
          localStorage.setItem('is_company', data.is_company);
          this.authService.isAuth = true;
          this.userService.getUser().subscribe((data) => {
            this.userService.user = data;
          });

          // this.userService.getUserObj();
          this.router.navigate(['/homepage']);
        } else {
          this.email = "";
          this.password = "";
          this.clicked++;
        }
      }, error => {
        if (error.status == 401) {
          this.authService.isAuth = false;
          localStorage.clear();
          this.snackBar.open("Email-Adresse ist nicht authentifiziert", "Error", {
            duration: 5000,
            // panelClass: ['colorstyle']
          });
        }
      })
    }

  }

  toCompanyRegister() {
    this.router.navigate(['/company-register']);
  }

  toRegister() {
    this.router.navigate(['/register']);
  }


}

import { Location } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';
import { UserService } from './services/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'collew-web';

  addressesLength: number = 0;

  constructor(public authService: AuthService, private router: Router, private userService: UserService, private location: Location) {
    if (localStorage.length !== 0) {
      localStorage.removeItem('lastLocation');
      const token = localStorage.getItem('token');
      const email = localStorage.getItem('email');
      const id = localStorage.getItem('id');      
      if (token && email && id) {
        this.authService.isAuth = true;
        this.userService.getUser().subscribe(data => {
          console.log(data);
          this.userService.user = data;
        }, error => {
          console.log(error)
          if (error.status == 401) {
            this.authService.isAuth = false;
            localStorage.clear();
            this.router.navigate(['/homepage']);
          }
        });
      }
    }
  }

  ngOnInit() {
    this.redirectToLastLocation();
  }
  
  toLogin() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.authService.isAuth = false;
    localStorage.clear();
    this.router.navigate(['/homepage']);
  }

  toHomepage() {
    this.router.navigate(['/homepage']);
  }

  toProfil() {
    this.router.navigate([`/profile/${this.userService.user.id}`]);
  }

  toProductsOrAddProducts() {
    if (this.userService.is_company === 'true') {
      this.router.navigate(['/products']);
    } else {
      this.router.navigate(['/add-product']);
    }
  }

  toCart() {
    this.router.navigate(['/cart']);
  }

  toScheduler() {
    this.router.navigate(['/scheduler']);
  }

  redirectToLastLocation() {
    const lastLocation = localStorage.getItem('lastLocation');
    if (lastLocation) {
      this.location.go(lastLocation);
    }
  }
}

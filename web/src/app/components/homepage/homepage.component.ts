import { Location } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.scss']
})
export class HomepageComponent implements OnInit {

  constructor(public authService: AuthService, private userService: UserService, private router: Router, private location: Location) {
    
  }

  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
  }
}

import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';


@Component({
  selector: 'app-password-change-dialog',
  templateUrl: './password-change-dialog.component.html',
  styleUrls: ['./password-change-dialog.component.scss']
})
export class PasswordChangeDialogComponent implements OnInit {

  oldpassword: string = "";
  newpassword: string = "";
  matchingpassword: string = "";

  passwordValid: boolean = true;
  hide1: boolean = true;
  hide2: boolean = true;
  hide3: boolean = true;

  constructor(public dialogRef: MatDialogRef<PasswordChangeDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any, private authService: AuthService, private userService: UserService) { }

  onNoClick(): void {
    this.dialogRef.close();
  }

  ngOnInit(): void {
  }



}

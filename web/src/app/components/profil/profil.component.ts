import { Location } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ImageCroppedEvent } from 'ngx-image-cropper';
import { Address } from 'src/app/interfaces/address';
import { AddressService } from 'src/app/services/address.service';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/user.service';
import { CropImageDialogComponent } from '../crop-image-dialog/crop-image-dialog.component';
import { PasswordChangeDialogComponent } from '../password-change-dialog/password-change-dialog.component';

@Component({
  selector: 'app-profil',
  templateUrl: './profil.component.html',
  styleUrls: ['./profil.component.scss']
})
export class ProfilComponent implements OnInit {

  newFirstname: string = "";
  newLastname: string = "";
  newEmail: string = "";
  newPhonenumber: string = "";
  newCompany: string = "";
  addresses: Address[] = [];

  imgSrc: string = "";

  imgChangeEvt: any = '';
  cropImgPreview: any = '';

  edit: boolean = false;

  url: string = "";

  constructor(public userService: UserService, public authService: AuthService, private http: HttpClient, private dialog: MatDialog, private snackBar: MatSnackBar, private router: Router, private addressService: AddressService, private location: Location) {
    this.newFirstname = this.userService.user.firstname!;
    this.newLastname = this.userService.user.lastname!;
    this.newEmail = this.userService.user.email!;
    this.newPhonenumber = this.userService.user.phonenumber!;
    this.newCompany = this.userService.user.companyName!;
    // this.addresses = this.addressService.adressen;
    console.log(this.userService.is_company);
    this.userService.userChangedObservable.subscribe((user) => {
      this.getUser();
    });
    this.addressService.addAddressObservable.subscribe((data) => {
      this.getLocation();
    });

    // this.userService.getProfileImg(this.userService.user.user_id!).subscribe((url) => {
    //   console.log(url);
    //   this.url = url;
    // });
    // this.userService.getUser().subscribe((data) => {
    //   console.log("GET USER: " + data);

    // })
  }


  ngOnInit(): void {
    localStorage.setItem('lastLocation', this.location.path());
    this.getLocation();
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    // let reader = new FileReader();
    // reader.readAsDataURL(file);
    // reader.onload = (event: any) => {
    //   this.url = event.target.result;
    //   console.log("EVENT: " + JSON.stringify(event.target.result));

    // }

    if (file) {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('user_id', this.userService.user.id!.toString());
      formData.append('url', this.url.toString());
      this.userService.upload(formData).subscribe((data) => {
        console.log("Response: " + JSON.stringify(data));
      });
    }
  }

  getUser() {
    this.userService.getUser().subscribe((data) => {
      this.userService.user = data;
    });
  }

  editProfile() {
    this.edit = true;
  }

  cancel() {
    this.edit = false;
  }

  saveChanges() {
    // TODO Verbindung zu userService - im userService ein http.post
    this.userService.updateFirstnameLastnamePhonenumber(this.newFirstname, this.newLastname, this.newPhonenumber).subscribe((data) => {
      console.log(data);

    })
    this.edit = false;
    // location.reload();
    // this.router.navigate([`/profil/${localStorage.getItem('id')}`]);
  }

  checkChanges() {
    if (// this.newCompany == this.userService.user.companyName &&
      this.newFirstname == this.userService.user.firstname &&
      this.newLastname == this.userService.user.lastname &&
      // this.newEmail == this.userService.user.email &&
      this.newPhonenumber == this.userService.user.phonenumber) {
      return false;
    }
    return true;
  }

  openDialogPasswordChange(): void {
    const dialogRef = this.dialog.open(PasswordChangeDialogComponent, {
      width: '400px',
      height: '400px',
      data: {},

    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.changePassword(result.oldpassword, result.newpassword, result.matchingpassword);
      }
    });
  }

  openDialog(event: any): void {
    // this.currentImageField = event;
    const dialogRef = this.dialog.open(CropImageDialogComponent, {
      width: '500px',
      height: '400px',
      data: { name: 'Cropping Tool', instruction: 'Please select an image to crop', dataField: event }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The cropping tool dialog was closed');
      console.log(result);
      // const reader = new FileReader();
      // this.demographicForm.get(this.currentImageField).setValue(result);
      // reader.onload = () => {
      // };
    });
  }

  changePassword(oldpassword: string, newpassword: string, matchingpassword: string) {
    /*if (oldPassword != this.userService.user.password) {
      this._snackBar.open("Altes Passwort nicht richtig!", "", {
        duration: 5000,
        panelClass: ['colorstyle']
      });
    } else if (oldPassword == newPassword) {
      this._snackBar.open("Das alte Passwort darf nicht das neue Passwort sein", "", {
        duration: 5000,
        panelClass: ['colorstyle']
      });
    } else*/
    if (!this.authService.passwordConfirmation(newpassword, matchingpassword)) {
      this.snackBar.open("Neues Passwort nicht bestätigt!", "error", {
        duration: 5000,
        // panelClass: ['colorstyle']
      });
    } else {
      // TODO Verbindung zu userService - http.post
      this.userService.changePassword(oldpassword, newpassword, matchingpassword).subscribe((pwd) => {
        console.log(pwd);

      });
    }

    console.log(oldpassword + " " + newpassword + " " + matchingpassword);

  }

  deleteUser() {
    this.userService.deleteUser().subscribe();
    this.authService.isAuth = false;
    this.router.navigate(['/homepage']);
  }

  deleteAddress(id: number) {
    if (id) {
      this.addresses = this.addresses.filter((adr) => {
        return adr.id != id
      });
      this.addressService.deleteLocation(id).subscribe((data) => {
        console.log(data);
        
      });
    }
  }

  getLocation() {
    this.addressService.getLocation().subscribe((data) => {
      console.log(data);
      // this.addressService.id = data[0].id;
      this.addresses = data;
    })
  }

  toAddAddress() {
    this.router.navigate(['/add-address']);
  }


  onFileChange(event: any): void {
    this.imgChangeEvt = event;
  }
  cropImg(e: ImageCroppedEvent) {
    this.cropImgPreview = e.base64;
  }
  imgLoad() {
    // display cropper tool
  }
  initCropper() {
    // init cropper
  }

  imgFailed() {
    // error msg
  }
}

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';

import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatListModule } from '@angular/material/list';
import { MatDividerModule } from '@angular/material/divider';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';
import { MatRadioModule } from '@angular/material/radio';
import {MatCheckboxModule} from '@angular/material/checkbox'; 

import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { FirmenRegisterComponent } from './components/firmen-register/firmen-register.component';
import { ProfilComponent } from './components/profil/profil.component';
import { PasswordChangeDialogComponent } from './components/password-change-dialog/password-change-dialog.component';
import { AddAddressComponent } from './components/add-address/add-address.component';
import { ProductsComponent } from './components/products/products.component';
import { CropImageDialogComponent } from './components/crop-image-dialog/crop-image-dialog.component';
import { AddProductComponent } from './components/add-product/add-product.component';
import { CartComponent } from './components/cart/cart.component';
import { ChooseLocationAndAppointmentsComponent } from './components/choose-location-and-appointments/choose-location-and-appointments.component';
import { AppointmentDialogComponent } from './components/appointment-dialog/appointment-dialog.component';
import { ProductDetailComponent } from './components/product-detail/product-detail.component';


import { AvatarModule } from 'ngx-avatar';
import { ImageCropperModule } from 'ngx-image-cropper';
// import { ImageSliderComponent } from './components/image-slider/image-slider.component';
import { ImageSliderModule } from './modules/image-slider/image-slider.module';
import { SchedulerComponent } from './components/scheduler/scheduler.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomepageComponent,
    FirmenRegisterComponent,
    ProfilComponent,
    PasswordChangeDialogComponent,
    AddAddressComponent,
    ProductsComponent,
    CropImageDialogComponent,
    AddProductComponent,
    CartComponent,
    ChooseLocationAndAppointmentsComponent,
    AppointmentDialogComponent,
    ProductDetailComponent,
    SchedulerComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatToolbarModule,
    MatIconModule,
    MatMenuModule,
    HttpClientModule,
    MatTooltipModule,
    ReactiveFormsModule,
    MatListModule,
    MatDividerModule,
    MatGridListModule,
    AvatarModule,
    MatDialogModule,
    MatSnackBarModule,
    MatSelectModule,
    MatTabsModule,
    ImageCropperModule,
    ImageSliderModule,
    MatRadioModule,
    MatCheckboxModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

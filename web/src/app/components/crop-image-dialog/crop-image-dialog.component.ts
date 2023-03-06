import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ImageCroppedEvent } from 'ngx-image-cropper';

@Component({
  selector: 'app-crop-image-dialog',
  templateUrl: './crop-image-dialog.component.html',
  styleUrls: ['./crop-image-dialog.component.scss']
})
export class CropImageDialogComponent implements OnInit {

  constructor(
    public dialogRef: MatDialogRef<CropImageDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) {
      dialogRef.disableClose = true;
     }

  ngOnInit(): void {
  }

  imgChangeEvt: any = '';
  croppedImage: any = '';
  cropCompleted = false;

  onNoClick(): void {
    this.dialogRef.close();
  }
  // Image cropper functions
  fileChangeEvent(event: any): void {
    this.imgChangeEvt = event;
  }
  cropImg(event: ImageCroppedEvent) {
    this.croppedImage = event.base64;
    console.warn('Image Crop: Image is cropped');
    this.cropCompleted = true;
  }
  imgLoad() {
    console.warn('Image Crop: Image is loaded');
    // show cropper
  }
  initCropper() {
    console.warn('Image Crop: Ready to crop');
    // cropper ready
  }
  imgFailed() {
    console.error('Image Crop: Image failed to load');
    // show message
  }

}

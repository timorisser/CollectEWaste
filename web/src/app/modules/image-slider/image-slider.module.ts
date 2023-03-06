import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ImageSliderComponent } from 'src/app/components/image-slider/image-slider.component';



@NgModule({
  declarations: [ImageSliderComponent],
  imports: [
    CommonModule
  ],
  exports: [ImageSliderComponent]
})
export class ImageSliderModule { }

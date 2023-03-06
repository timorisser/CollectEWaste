import { Component, Input, OnInit } from '@angular/core';
import { Slide } from 'src/app/interfaces/slide';

@Component({
  selector: 'app-image-slider',
  templateUrl: './image-slider.component.html',
  styleUrls: ['./image-slider.component.scss']
})
export class ImageSliderComponent implements OnInit {
  @Input() slides: string[] = [];

  currentIndex: number = 0;

  constructor() { }

  ngOnInit(): void {
  }

  prevImg() {
    const isFirstSlide = this.currentIndex === 0;
    this.currentIndex = isFirstSlide ? this.slides.length - 1 : this.currentIndex - 1
  }

  nextImg() {
    const isLastSlide = this.currentIndex === this.slides.length - 1;
    this.currentIndex = isLastSlide ? 0 : this.currentIndex + 1;
  }

  changeSlide(slideIndex: number) {
    this.currentIndex = slideIndex;
  }

}

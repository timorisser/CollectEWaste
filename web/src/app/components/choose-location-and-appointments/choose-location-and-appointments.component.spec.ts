import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseLocationAndAppointmentsComponent } from './choose-location-and-appointments.component';

describe('ChooseLocationAndAppointmentsComponent', () => {
  let component: ChooseLocationAndAppointmentsComponent;
  let fixture: ComponentFixture<ChooseLocationAndAppointmentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChooseLocationAndAppointmentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChooseLocationAndAppointmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

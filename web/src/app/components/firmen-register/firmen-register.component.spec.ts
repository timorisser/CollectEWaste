import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FirmenRegisterComponent } from './firmen-register.component';

describe('FirmenRegisterComponent', () => {
  let component: FirmenRegisterComponent;
  let fixture: ComponentFixture<FirmenRegisterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FirmenRegisterComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FirmenRegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

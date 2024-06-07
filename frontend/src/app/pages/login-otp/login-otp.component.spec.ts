import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginOTPComponent } from './login-otp.component';

describe('LoginOTPComponent', () => {
  let component: LoginOTPComponent;
  let fixture: ComponentFixture<LoginOTPComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginOTPComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoginOTPComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

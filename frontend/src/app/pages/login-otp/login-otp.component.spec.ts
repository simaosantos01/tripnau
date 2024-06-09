import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginOTPComponent } from './login-otp.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { routes } from '../../app.routes';

describe('LoginOTPComponent', () => {
  let component: LoginOTPComponent;
  let fixture: ComponentFixture<LoginOTPComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginOTPComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter(routes),
      ]
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

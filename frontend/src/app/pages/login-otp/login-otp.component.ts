import { Component } from '@angular/core';

@Component({
  selector: 'app-login-otp',
  standalone: true,
  imports: [],
  templateUrl: './login-otp.component.html',
  styleUrl: './login-otp.component.css'
})
export class LoginOTPComponent {
  twoFactorCode: string = '';

  onSubmit() {
    // Handle form submission
    console.log('2FA Code:', this.twoFactorCode);
  }
}

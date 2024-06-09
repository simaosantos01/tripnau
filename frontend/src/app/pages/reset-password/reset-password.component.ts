import { Component } from '@angular/core';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css'
})
export class ResetPasswordComponent {
  email: string = '';

  onSubmit() {
    // Handle form submission
    console.log('Email:', this.email);
    // Add logic to send password reset link to the provided email
  }
}

import { Component, Inject, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { GenerateOTPRequest } from '../../model/generate-otp.request';
import { LoginRequest } from '../../model/login-request';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginResponse } from '../../model/login-response';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { MessagesService } from '../../services/messages.service';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

@Component({
  selector: 'app-login-otp',
  standalone: true,
  imports: [ButtonModule, InputTextModule, ReactiveFormsModule],
  templateUrl: './login-otp.component.html',
  styleUrl: './login-otp.component.css'
})
export class LoginOTPComponent {
  fb = inject(FormBuilder);
  router = inject(Router);
  messagesService = inject(MessagesService);

  credentials: GenerateOTPRequest | undefined;

  authService = inject(AuthService);

  form = this.fb.group({
    twoFactorCode: ['', Validators.required],
  });

  ngOnInit(): void {
    this.authService.credentials.subscribe((credentials: GenerateOTPRequest | undefined) => {
      this.credentials = credentials;
      console.log(this.credentials)
    });
  }

  onSubmit() {

    var credentialsWithCode: LoginRequest = {
      email: this.credentials!.email,
      password: this.credentials!.password,
      phoneNumber: '918589899', // * It's the only one that works welelellele
      code: this.form.controls.twoFactorCode.value!,
    }

    this.authService.login(credentialsWithCode).subscribe({
      next: (response: LoginResponse) => this.handleLoginResponse(response),
      error: (error: HttpErrorResponse | HttpResponse<LoginResponse>) => this.handleError(error)
    });
  }

  handleLoginResponse(response: LoginResponse) {
    if (response) {
      console.log(this.authService.isAuthenticated())
      this.router.navigateByUrl('/home');
    }
  }

  handleError(error: any) {
    if (error.name) {
      this.messagesService.error('There was a problem sending the request', 'Oopsie Daisy')
    } else {
      this.messagesService.error('Take a breather', 'Rate Limit Exceeded')
    }
  }
}
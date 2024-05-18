import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { RegisterRequest } from '../../model/register-request';
import { RegisterResponse } from '../../model/register-response';
import { AuthService } from '../../services/auth.service';
import { MessagesService } from '../../services/messages.service';
import { ROUTE } from '../../enum/routes';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {

  authService = inject(AuthService);
  router = inject(Router);
  messagesService = inject(MessagesService);

  signupForm = new FormGroup({
    name: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  })

  tosForm = new FormGroup({
    tos: new FormControl(null, [Validators.required])
  })

  onSubmit() {
    if (this.tosForm.controls.tos.value && this.signupForm.valid) {
      const body: RegisterRequest = {
        name: this.signupForm.controls.name.value!,
        email: this.signupForm.controls.email.value!,
        password: this.signupForm.controls.password.value!
      }
      this.authService.register(body)
        .subscribe({
          next: (data: RegisterResponse) => this.handleRegisterResponse(data),
          error: (err) => this.handleError(err)
        });
    }
  }

  handleRegisterResponse(response: RegisterResponse) {
    console.debug(response)
    // emit some toastr event or something herer
    this.messagesService.success('Registered with success! You can now login!', 'Registered')
    this.router.navigate([ROUTE.LOGIN]);
  }

  handleError(error: any) {
    if (error.name) {
      this.messagesService.error('There was a problem sending the request', 'Oopsie Daisy')
    } else {
      this.messagesService.error('Take a breather', 'Rate Limit Exceeded')
    }
  }
}

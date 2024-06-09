import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ResetForgottenPasswordRequest } from '../../model/reset-forgotten-password.request';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-reset-forgotten-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './reset-forgotten-password.component.html',
  styleUrl: './reset-forgotten-password.component.css',
})
export class ResetForgottenPasswordComponent {
  fb = inject(FormBuilder);
  authService = inject(AuthService);
  router = inject(Router);

  changePasswordForm: FormGroup;
  token: string | null = null;
  passwordMismatch: boolean = false;

  constructor(private route: ActivatedRoute) {
    this.changePasswordForm = this.fb.group({
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
  }

  onSubmit(): void {
    if (this.changePasswordForm.invalid) {
      return;
    }

    if (
      this.changePasswordForm.value.password !==
      this.changePasswordForm.value.confirmPassword
    ) {
      this.passwordMismatch = true;
      return;
    }

    const changePasswordData: ResetForgottenPasswordRequest = {
      newPassword: this.changePasswordForm.value.password,
      token: this.token!,
    };

    this.authService
      .resetForgottenPassword(changePasswordData)
      .subscribe(() => {
        this.router.navigateByUrl('/login');
      });
  }
}

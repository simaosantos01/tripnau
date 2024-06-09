import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';
import { UpdatePasswordRequest } from '../../model/update-password-request';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './user.component.html',
  styleUrl: './user.component.css',
})
export class UserComponent {
  authService = inject(AuthService);
  userService = inject(UserService);
  router = inject(Router);

  currentPassword: string = '';
  confirmCurrentPassword: string = '';
  newPassword: string = '';
  confirmNewPassword: string = '';

  user!: User;

  ngOnInit(): void {
    this.user = {
      id: '',
      name: '',
      email: '',
      phoneNumber: '',
      role: '',
      isBanned: false,
    };

    this.getData();
  }

  getData() {
    this.userService
      .getUserInfoByEmail(this.authService.getEmail())
      .subscribe((data) => {
        // If this service fails the displayed info will be a bunch of nulls but honestly
        this.user = data; // it's not worth it to deal with all the possibilities right now  ¯\_(ツ)_/¯
      });
  }

  changePassword() {
    if (this.currentPassword !== this.confirmCurrentPassword) {
      alert('Current passwords do not match!');
    } else if (this.newPassword !== this.confirmNewPassword) {
      alert('New passwords do not match!');
    } else {
      let updatePasswordRequest: UpdatePasswordRequest = {
        oldPassword: this.currentPassword,
        newPassword: this.newPassword,
      }
      this.authService.updatePassword(updatePasswordRequest).subscribe((response: any) => {
        if(response) {
          this.router.navigateByUrl('/user');
        }
      });
    }
  }

  logout() {
    this.authService.logout().subscribe();
  }
}
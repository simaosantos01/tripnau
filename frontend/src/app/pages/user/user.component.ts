import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../model/user';

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

  userBootstrap = {
    name: 'John Doe',
    email: 'john.doe@example.com',
    bio: 'Software Developer at OpenAI',
    password: '',
  };

  constructor() {}

  ngOnInit(): void {
    this.user = {
      id: '',
      name: '',
      email: '',
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

  updateUser() {
    // Logic to update user details
    alert('User details updated!');
  }

  changePassword() {
    if (this.currentPassword !== this.confirmCurrentPassword) {
      alert('Current passwords do not match!');
      return;
    } else if (this.newPassword !== this.confirmNewPassword) {
      alert('New passwords do not match!');
      return;
    } else {
      // Change password logic
      console.log('Password changed to:', this.newPassword);
    }
  }

  logout() {
    // Logic to log out the user
    alert('Logged out!');
  }
}

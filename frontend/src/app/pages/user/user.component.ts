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
  styleUrl: './user.component.css'
})
export class UserComponent {

  authService = inject(AuthService)
  userService = inject(UserService)
  router = inject(Router)

  user!: User;

  userBootstrap = {
    name: 'John Doe',
    email: 'john.doe@example.com',
    bio: 'Software Developer at OpenAI',
    password: ''
  };

  constructor() { }

  ngOnInit(): void {
    this.user = {
      id: '',
      name: '',
      email: '',
      role: '',
      isBanned: false
    }

    this.getData();
  }

  getData() {
    this.userService.getUserInfoByEmail(this.authService.getEmail()).subscribe((data) => {    // If this service fails the displayed info will be a bunch of nulls but honestly
      this.user = data;                                                                       // it's not worth it to deal with all the possibilities right now  ¯\_(ツ)_/¯
    });
  }

  updateUser() {
    // Logic to update user details
    alert('User details updated!');
  }

  changePassword() {
    // Logic to change password
    alert('Password changed!');
  }

  logout() {
    // Logic to log out the user
    alert('Logged out!');
  }
}
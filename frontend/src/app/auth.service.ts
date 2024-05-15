import { Injectable } from '@angular/core';
import { LoginRequest } from './model/login-request';
import { HttpClient } from '@angular/common/http';
import { ROUTE } from './enum/routes';
import { LoginResponse } from './model/login-response';
import { tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  readonly BASE_URL = "";

  user: string | undefined;
  token: string | undefined;
  role: string | undefined;
  authenticated = false;

  constructor(private http: HttpClient) {

  }

  login(credentials: LoginRequest) {
    this.http.post<LoginResponse>(this.BASE_URL + ROUTE.LOGIN, credentials).pipe(
      tap((response) => {
        if (response.token) {
          this.setToken(response.token);
        }
      })
    )
  }

  getUser() {
    return this.user;
  }
  setUser(user: string) {
    this.user = user;
  }
  getToken() {
    return this.token
  }
  setToken(token: string) {
    try {
      this.parseToken(token);
      localStorage.setItem('token', token);
      this.token = token;
    } catch (e) {
      this.token = '';
    }
  }
  parseToken(token: string) {

  }
  getRole() {
    return this.role;
  }
  setRole(role: string) {
    this.role = role;
  }
  isAuthenticated(): boolean {
    return this.authenticated;
  }
  logout() {

  }
}

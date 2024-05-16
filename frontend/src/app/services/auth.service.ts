import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { ROUTE } from '../enum/routes';
import { LoginRequest } from '../model/login-request';
import { LoginResponse } from '../model/login-response';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  readonly BASE_URL = "";

  user: string | undefined;
  token: string | undefined;
  role: string | undefined;
  authenticated = false;

  http = inject(HttpClient)

  constructor() { }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.BASE_URL + ROUTE.LOGIN, credentials).pipe(
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
      this.token = token;
      localStorage.setItem('token', token);
    } catch (e) {
      this.token = '';
    }
  }
  parseToken(token: string) {
    // parse the token and instantiate the values of user + roles
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

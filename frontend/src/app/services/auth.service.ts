import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { ROUTE } from '../enum/routes';
import { LoginRequest } from '../model/login-request';
import { LoginResponse } from '../model/login-response';
import { RegisterRequest } from '../model/register-request';
import { RegisterResponse } from '../model/register-response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  user: string | undefined;
  token: string | undefined;
  role: string | undefined;
  authenticated = false;

  http = inject(HttpClient)

  constructor() { }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    // this.authenticated = true;
    return new Observable<LoginResponse>(observer => {
      observer.next({
        token: ''
      })
    })
    return this.http.post<LoginResponse>(environment.apiUrl + ROUTE.LOGIN, credentials).pipe(
      tap((response) => {
        if (response.token) {
          this.setToken(response.token);
        }
      })
    )
  }
  register(user: RegisterRequest): Observable<RegisterResponse> {
    return new Observable<RegisterResponse>(observer => {
      observer.next({
        success: true
      })
    })
    return this.http.post<RegisterResponse>(environment.apiUrl + ROUTE.REGISTER, user);
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
      if (this.getRole() != 'ADMIN') {
        localStorage.setItem('token', token);
      }
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

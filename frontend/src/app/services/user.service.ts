import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { ROUTE } from '../enum/routes';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  http = inject(HttpClient)

  constructor() { }

  getUserInfoByEmail(email: string): Observable<User> {
    return this.http.get<User>(environment.apiUrl + ROUTE.USERBYEMAIL + "/" + email);
  }
}
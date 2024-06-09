import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { environment } from '../../environments/environment';
import { ROUTE } from '../enum/routes';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  http = inject(HttpClient)

  constructor() { }

  createReview(formData: FormData) {
    return this.http.post<void>(environment.apiUrl + ROUTE.CREATEREVIEW, { formData});
  }
}
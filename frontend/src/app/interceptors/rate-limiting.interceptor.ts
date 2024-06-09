import { HttpErrorResponse, HttpHandlerFn, HttpInterceptorFn, HttpRequest, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RateLimitingService } from '../services/rate-limiting.service';
import { createErrorPayload } from '../utils';

export const rateLimitingInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  const service = inject(RateLimitingService)
  if (service.tryConsume()) {
    return next(req);
  } else {
    return new Observable<HttpResponse<any>>(observer => {
      const errorPayload = createErrorPayload('Potentially Dangerous Input Detected', HttpStatusCode.TooManyRequests, 'Too Many Requests', req.url);
      observer.error(new HttpErrorResponse({
        status: HttpStatusCode.TooManyRequests,
        statusText: 'Potentially Dangerous Input Detected',
        error: errorPayload
      }));
    });
  }
};
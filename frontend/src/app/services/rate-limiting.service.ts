import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RateLimitingService {
  // number of requests allowed per minute
  readonly MAX_CAPACITY = 60;
  private capacity = this.MAX_CAPACITY;

  constructor() {
    const timeout = 1000 * 60 * 1;
    setTimeout(() => {
      this.refill();
    }, timeout)
  }

  tryConsume(): boolean {
    if (this.capacity > 0) {
      this.capacity--;
      return true;
    }
    return false;
  }

  refill(): void {
    this.capacity = this.MAX_CAPACITY;
  }
}

import { TestBed } from '@angular/core/testing';

import { RateLimitingService } from './rate-limiting.service';

describe('RateLimitingService', () => {
  let service: RateLimitingService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RateLimitingService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';
import { RentalPropertyService } from './rental-property.service';

describe('RentalPropertyService', () => {
  let service: RentalPropertyService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RentalPropertyService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

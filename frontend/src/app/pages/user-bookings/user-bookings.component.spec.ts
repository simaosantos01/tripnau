import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserBookingsComponent } from './user-bookings.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { routes } from '../../app.routes';

describe('UserBookingsComponent', () => {
  let component: UserBookingsComponent;
  let fixture: ComponentFixture<UserBookingsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserBookingsComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter(routes),
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UserBookingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

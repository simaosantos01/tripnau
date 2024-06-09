import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';
import { BookingService } from '../../services/booking.service';
import { BookedPropertyResponse } from '../../model/booked-property-response';
import { RentalProperty } from '../../model/rental-property';
import { RentalPropertyService } from '../../services/rental-property.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-bookings',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-bookings.component.html',
  styleUrl: './user-bookings.component.css',
})
export class UserBookingsComponent {
  authService = inject(AuthService);
  userService = inject(UserService);
  router = inject(Router);
  rentalPropertyService = inject(RentalPropertyService);
  bookingService = inject(BookingService);

  bookingsByUser: BookedPropertyResponse[] = [];
  bookingsByUserProperties: RentalProperty[] = [];

  ngOnInit() {
    let id: string = '';
    this.userService
      .getUserInfoByEmail(this.authService.getEmail())
      .subscribe((data) => {
        id = data.id;
        this.bookingService.getBookingsByUser(id).subscribe({
          next: (response) => {
            this.bookingsByUser = response;
            this.bookingsByUser.forEach((booking) => {
              this.rentalPropertyService
                .getById(booking.propertyId)
                .subscribe((property) => {
                  this.bookingsByUserProperties.push(property);
                });
            });
          },
          error: (error) => console.log(error),
        });
      });
  }

  getPropertyDetails(propertyId: string): RentalProperty | undefined {
    return this.bookingsByUserProperties.find(
      (property) => property.id === propertyId
    );
  }

  isBeforeCurrentDate(date: Date): boolean {
    const currentDate = new Date();
    return date < currentDate;
  }

  cancelBooking(bookingId: string) {
    this.bookingService.cancelBooking(bookingId).subscribe(() => {
      this.router.navigateByUrl('/user-bookings');
    });
  }

  leaveReview(propertyId: string) {
    this.router.navigateByUrl(`/review/${propertyId}`);
  }
}

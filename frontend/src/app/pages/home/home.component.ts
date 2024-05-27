import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { RentalPropertyService } from '../../services/rental-property.service';
import { RentalProperty } from '../../model/rental-property';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BookingService } from '../../services/booking.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  propertyService = inject(RentalPropertyService)
  bookingService = inject(BookingService);
  router: Router = inject(Router);
  properties: RentalProperty[] = [];
  
  constructor(private cdref: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.initData();
  }

  initData() {
    this.propertyService.getAll().subscribe((data) => {
      this.properties = data;
    }); 

    this.properties.forEach((property) => {
      property.imageIndex = this.getRandomNumber();
    });
  }

  ngAfterContentChecked() {
    this.cdref.detectChanges();
 }

  rentProperty(propertyId: any) {
    this.bookingService.currentPropertyToBookIndex.next(propertyId);
    this.bookingService.currentPropertyToBook.next(this.properties[propertyId]);
    this.router.navigateByUrl(`/rent/${propertyId}`);
  }

  getRandomNumber(): number {
    return Math.floor(Math.random() * 5) + 1;
  }

  getImagePath(index: number): string {
    if(index) {
      return `../../assets/images/RentalProperty${index}.jpg`;
    } else {
      var randomIndex = this.getRandomNumber();
      return `../../assets/images/RentalProperty${randomIndex}.jpg`;
    }
  }
}
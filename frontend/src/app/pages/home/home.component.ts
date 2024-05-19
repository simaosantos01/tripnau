import { Component, OnInit, inject } from '@angular/core';
import { RentalPropertyService } from '../../services/rental-property.service';
import { RentalProperty } from '../../model/rental-property';
import { Router } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

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
  router: Router = inject(Router);
  properties: RentalProperty[] = [];
  
  ngOnInit(): void {
    this.initData();
  }

  initData() {
    console.log('initData')
    this.propertyService.getAll().subscribe((data) => {
      console.log(data);
      this.properties = data;
    }); 
  }

  rentProperty() {
    var propertyId = 1;
    this.router.navigate(['/rent', 'propertyId']);
  }
}

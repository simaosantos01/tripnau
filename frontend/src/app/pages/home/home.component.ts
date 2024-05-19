import { Component, OnInit, inject } from '@angular/core';
import { RentalPropertyService } from '../../services/rental-property.service';
import { RentalProperty } from '../../model/rental-property';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  propertyService = inject(RentalPropertyService)
  data: RentalProperty[] = [];

  ngOnInit(): void {

  }

  initData() {
    this.propertyService.getAll().subscribe((data) => {
      console.log(data);
      this.data = data;
    }); 
  }
}

import { Component, OnInit, inject } from '@angular/core';
//import { RentalPropertyService } from '../../services/rental-property.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {

  propertyService = inject(RentalPropertyService)

  ngOnInit(): void {

  }

  initData() {

  }
}

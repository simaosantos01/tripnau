import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payment-sucess',
  standalone: true,
  imports: [],
  templateUrl: './payment-sucess.component.html',
  styleUrl: './payment-sucess.component.css'
})
export class PaymentSucessComponent {

  router = inject(Router)

  onOkay(): void {
    this.router.navigateByUrl('/userBookings');
  }
}

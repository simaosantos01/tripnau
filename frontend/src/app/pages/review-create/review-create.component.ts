import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ReviewService } from '../../services/review.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-review-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './review-create.component.html',
  styleUrl: './review-create.component.css'
})
export class ReviewCreateComponent {
  reviewService = inject(ReviewService);
  router = inject(Router);
  reviewForm: FormGroup;
  selectedFiles: File[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.reviewForm = this.fb.group({
      rating: [null, Validators.required],
      text: ['', Validators.required],
      images: [null]
    });
  }

  onFileChange(event: any): void {
    if (event.target.files.length > 0) {
      this.selectedFiles = Array.from(event.target.files);
    }
  }

  submitReview(): void {
    if (this.reviewForm.valid) {
      const formData = new FormData();
      formData.append('rating', this.reviewForm.get('rating')?.value);
      formData.append('textReview', this.reviewForm.get('textReview')?.value);
      this.selectedFiles.forEach((file, index) => {
        formData.append('images', file, file.name);
      });

      this.reviewService.createReview(formData).subscribe(response => {
        this.router.navigateByUrl('/userBookings');
      }, error => {
        console.error('Error submitting review', error);
      });
    }
  }
}
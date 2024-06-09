import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewCreateComponent } from './review-create.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { routes } from '../../app.routes';

describe('ReviewCreateComponent', () => {
  let component: ReviewCreateComponent;
  let fixture: ComponentFixture<ReviewCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewCreateComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter(routes),
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReviewCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

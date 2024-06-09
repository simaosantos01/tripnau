import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResetForgottenPasswordComponent } from './reset-forgotten-password.component';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { routes } from '../../app.routes';

describe('ResetForgottenPasswordComponent', () => {
  let component: ResetForgottenPasswordComponent;
  let fixture: ComponentFixture<ResetForgottenPasswordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResetForgottenPasswordComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter(routes),
      ]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ResetForgottenPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

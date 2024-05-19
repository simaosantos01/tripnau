import { TestBed } from '@angular/core/testing';

import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';
import { LoginRequest } from '../model/login-request';
import { LoginResponse } from '../model/login-response';
import { RegisterRequest } from '../model/register-request';
import { RegisterResponse } from '../model/register-response';
import { AuthService } from './auth.service';

describe('AuthService', () => {
  let service: AuthService;
  let httpTesting: HttpTestingController;

  const mockToken = 'Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InRlc3RAdGVzdC5jb20iLCJyb2xlcyI6WyJhZG1pbiIsInVzZXIiLCJwcm9kdWN0YWxvZyJdLCJpYXQiOjE2MzUwNjM3NjcsImV4cCI6MTYzNTA2Mzc5Nn0.LS4veWUrT1SlBK9ey5A0nRn0jccCmRW2ZVdDhYNgIrM'

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(AuthService);
    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set and get email', () => {
    service.setEmail('CR7@gmail.com')
    expect(service.getEmail()).toEqual('CR7@gmail.com')
  })

  it('should set and get user', () => {
    service.setUser('testUser');
    expect(service.getUser()).toEqual('testUser');
  });

  it('should set and get role', () => {
    service.setRoles(['BUSINESSADMIN']);
    expect(service.getRoles()).toEqual(['BUSINESSADMIN']);
  });

  it('should check authentication status', () => {
    expect(service.isAuthenticated()).toBeFalse();
  });

  // BEGIN-NOSCAN
  it('should register', () => {
    const registerRequest: RegisterRequest = { name: 'Cristiano Reinaldo', email: 'rei@naldo.pt', password: 'somethingsomething' };
    const registerResponse: RegisterResponse = { success: true };

    service.register(registerRequest).subscribe((response) => {
      expect(response).toEqual(registerResponse);
    });

    const req = httpTesting.expectOne(`${environment.apiUrl}/register`);
    expect(req.request.method).toBe('POST');
    req.flush(registerResponse);
  });
  // END-NOSCAN

  it('should parse token', () => {
    service.parseToken(mockToken);

    expect(service.getToken()).toEqual(mockToken);
    expect(service.getEmail()).toEqual('test@test.com');
    expect(service.getRoles()).toEqual(["admin", "user", "productalog"])
  });

  // BEGIN-NOSCAN
  it('should login and set token', () => {
    const loginRequest: LoginRequest = { email: 'test@example.com', password: 'somethingsomething' };
    const loginResponse: LoginResponse = { token: mockToken };

    service.login(loginRequest).subscribe((response) => {
      expect(response).toEqual(loginResponse);
      expect(service.getToken()).toEqual(mockToken);
    });
    // END-NOSCAN

    const req = httpTesting.expectOne(`${environment.apiUrl}/login`);
    expect(req.request.method).toBe('POST');
    req.flush(loginResponse, { headers: { Authorization: mockToken } });
  });
});

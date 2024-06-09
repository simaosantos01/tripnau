import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { HomeComponent } from './pages/home/home.component';
import { BookingComponent } from './pages/booking/booking.component';
import { UserComponent } from './pages/user/user.component';
import { LoginOTPComponent } from './pages/login-otp/login-otp.component';
import { ResetPasswordComponent } from './pages/reset-password/reset-password.component';

export const routes: Routes = [
    { path: '', component: HomeComponent, pathMatch: 'full', canActivate: [authGuard([])] },
    //{ path: '/', component: HomeComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'login', component: LoginComponent },
    { path: 'loginOTP', component: LoginOTPComponent },
    { path: 'resetPassword', component: ResetPasswordComponent },
    { path: 'home', component: HomeComponent, canActivate: [authGuard([])] },
    { path: 'user', component: UserComponent, canActivate: [authGuard([])] },
    { path: 'admin', component: HomeComponent, canActivate: [authGuard(['ADMIN'])] },
    { path: 'rent/:id', component: BookingComponent, canActivate: [authGuard([])] },
];
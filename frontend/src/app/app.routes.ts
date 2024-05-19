import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { authGuard } from './guards/auth.guard';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';

export const routes: Routes = [
    { path: '', component: AppComponent, pathMatch: 'full', canActivate: [authGuard([])] },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'admin', component: AppComponent, canActivate: [authGuard(['BUSINESSADMIN'])] },
    { path: 'home', component: AppComponent, canActivate: [authGuard([])] }
];

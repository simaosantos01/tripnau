import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { authGuard } from './guards/auth.guard';
import { LoginComponent } from './pages/login/login.component';

export const routes: Routes = [
    { path: '', component: AppComponent, pathMatch: 'full', canActivate: [authGuard(['USER', 'ADMIN'])] },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: LoginComponent },
    { path: 'admin', component: AppComponent, canActivate: [authGuard(['ADMIN'])] },
    { path: 'home', component: AppComponent, canActivate: [authGuard(['USER', 'ADMIN'])] }
];

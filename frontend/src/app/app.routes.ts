import { Routes } from '@angular/router';
import { AppComponent } from './app.component';
import { authGuard } from './guards/auth.guard';
import { LoginPageComponent } from './pages/login-page/login-page.component';

export const routes: Routes = [
    { path: '', component: AppComponent, pathMatch: 'full', canActivate: [authGuard(['USER', 'ADMIN'])] },
    { path: 'login', component: LoginPageComponent },
    { path: 'register', component: LoginPageComponent },
    { path: 'admin', component: AppComponent, canActivate: [authGuard(['ADMIN'])] },
    { path: 'home', component: AppComponent, canActivate: [authGuard(['USER', 'ADMIN'])] }
];

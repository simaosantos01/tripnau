import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';


export function authGuard(requiredRoles: Array<string>) {
  const guard: CanActivateFn = () => {
    const authService = inject(AuthService);
    const router = inject(Router)

    if (!authService.isAuthenticated()) {
      router.navigateByUrl('/login')
      return false;
    }

    if (requiredRoles.length == 0) return true;
    const roles = authService.getRoles();
    let authorizide = false;
    
    requiredRoles.forEach((role) => {
      if (roles.includes(role)) authorizide = true
    })

    return authorizide;
  };
  return guard;
}

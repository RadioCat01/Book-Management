import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../Tokens/token.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (): boolean => {
  const tokenService: TokenService = inject(TokenService);
  const router :Router = inject(Router);
  if(tokenService.isTokenNotValid()){
    router.navigate(['login']);
    return false;
  }
  return true;
};

import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string){
    localStorage.setItem('token', token);
  }

  get token(){
    return localStorage.getItem('token') as string;
  }

  isTokenNotValid():boolean {
    return !this.isTokenValid();
  }

  isTokenValid():boolean{
    const token: string= this.token;
    if(!token){
      return false;
    }
    const jwtHelper:JwtHelperService =new JwtHelperService();
    const isTokenExpired:boolean = jwtHelper.isTokenExpired(token);
    if(isTokenExpired){
      localStorage.clear();
      return false;
    }
    return true;
  }
}

import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../Services/services';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TokenService } from '../../Services/Tokens/token.service';
import { CodeInputModule } from 'angular-code-input';
import { confirm } from '../../Services/fn/authentication/confirm';

@Component({
  selector: 'app-activate-account',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    CodeInputModule
  ],
  providers:[
    AuthenticationService,
    HttpClient,
    TokenService
   ],
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {

  message: string='';
  isOkay: boolean = true;
  submitted: boolean = false;

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ){}

  redirectToLogin(): void {
    this.router.navigate(['login']);
  }
  onCodeCompleted(token: string): void {

    this.confirmAccount(token);

  }
  
  confirmAccount(token : string):void {
    this.authService.confirm({
      token
    }).subscribe({
      next:():void=>{
        this.message = 'Account Activated !';
        this.submitted=true;
        this.isOkay=true;
      },
      error:():void =>{
        this.message='Token Expired';
        this.isOkay = false;
      }
    });
  }

}

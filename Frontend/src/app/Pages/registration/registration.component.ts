import { Component } from '@angular/core';
import { RegistrationRequest } from '../../Services/models/registration-request';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { register } from '../../Services/fn/authentication/register';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../Services/services';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    AuthenticationService,
    HttpClient,
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.scss'
})
export class RegistrationComponent {

  registerRequest: RegistrationRequest = {email:'', firstName:'', lastName:'', password:''};
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService
  ){
  
  }

  register():void{

    this.errorMsg = [];

    this.authService.register({

      body: this.registerRequest

    }).subscribe({

      next:():void=>{
       
      },
      error: (err):void =>{
        this.errorMsg = err.error.validationErrors;
      }
    })
  }


  login():void{
    this.router.navigate(["login"]);
  }
}

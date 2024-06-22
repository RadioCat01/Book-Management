import { Component} from '@angular/core';
import { AuthenticationRequest, AuthenticationResponse } from '../../Services/models';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../Services/services/authentication.service';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { TokenService } from '../../Services/Tokens/token.service';



@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
  ],
  providers:[
   AuthenticationService,
   HttpClient,
   TokenService
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  authRequest: AuthenticationRequest ={email:'', password:''};
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationService,
    private tokenService: TokenService
  ){}

  login(): void {
    this.errorMsg = []; // Clear previous error messages

    this.authService.authenticate({ body: this.authRequest }).subscribe(
      {  
        next: (res: AuthenticationResponse): void => 
          {
            this.tokenService.token = res.token as string;
            this.router.navigate(['books']);
          },
      error:(err): void=>{

        if(err.error.validationErrors){

          this.errorMsg=err.error.validationErrors;

        }else{

          this.errorMsg.push(err.error.errorMsg);

        }
      }
    });
  }

  register():void{
    this.router.navigate(['register']);
  }

}

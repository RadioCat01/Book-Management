import { Routes } from '@angular/router';
import { LoginComponent } from './Pages/login/login.component';
import { RegistrationComponent } from './Pages/registration/registration.component';
import { ActivateAccountComponent } from './Pages/activate-account/activate-account.component';
import { authGuard } from './Services/Guard/auth.guard';

export const routes: Routes = [
    {
        path:"login", component : LoginComponent
    },
    {
        path:"register", component : RegistrationComponent
    },
    {
        path:"activate-account", component : ActivateAccountComponent
    },
    {
        path:"books", 
        loadChildren : ()=> import('./Module/book/book.module').then(m => m.BookModule),
        canActivate: [authGuard]
    }
];

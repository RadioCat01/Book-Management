import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainComponent } from './pages/main/main.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { MyBooksComponent } from './pages/my-books/my-books.component';
import { ManageBookComponent } from './pages/manage-book/manage-book.component';
import { BorrowedBooksComponent } from './pages/borrowed-books/borrowed-books.component';
import { ReturnedBookComponent } from './pages/returned-book/returned-book.component';
import { audit } from 'rxjs';
import { authGuard } from '../../Services/Guard/auth.guard';
const routes: Routes = [
  {
    path: '' , 
    component: MainComponent,
    canActivate: [authGuard],
  },
      {
        path: '', 
        component: BookListComponent,
        canActivate: [authGuard],

      },
      {
        path: 'my-books', 
        component: MyBooksComponent,
        canActivate: [authGuard],

      },
      {
        path: 'manage', 
        component: ManageBookComponent,
        canActivate: [authGuard],

      },
      {
        path: 'manage/:bookId', 
        component: ManageBookComponent,
        canActivate: [authGuard],

      },
      {
        path: 'my-borrowed-books', 
        component: BorrowedBooksComponent,
        canActivate: [authGuard],

      },
      {
        path: 'my-returned-books', 
        component: ReturnedBookComponent,
        canActivate: [authGuard],
      }  
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BookRoutingModule { }

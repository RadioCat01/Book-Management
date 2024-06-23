import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BookRoutingModule } from './book-routing.module';
import { MainComponent } from './pages/main/main.component';
import { RouterLink, RouterOutlet } from '@angular/router';
import { MenuComponent } from './components/menu/menu.component';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
import { HttpTokenInterceptor } from '../../Services/interceptor/http-token.interceptor';
import { BookCardComponent } from './components/book-card/book-card.component';
import { RatingComponent } from './components/rating/rating.component';
import { BookListComponent } from './pages/book-list/book-list.component';
import { MyBooksComponent } from './pages/my-books/my-books.component';


@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    BookRoutingModule,
    MainComponent,
    RouterOutlet,
    MenuComponent,
    HttpClientModule,
    RatingComponent,
    BookCardComponent,
    BookListComponent,
    MyBooksComponent,
    RouterLink
  ],
  providers:[
    HttpClient,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpTokenInterceptor,
      multi: true
    }
  ]
})
export class BookModule { }

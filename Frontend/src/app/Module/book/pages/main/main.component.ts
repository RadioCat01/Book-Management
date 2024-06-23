import { Component, provideZoneChangeDetection } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenuComponent } from '../../components/menu/menu.component';
import { MyBooksComponent } from '../my-books/my-books.component';
import { BorrowedBooksComponent } from '../borrowed-books/borrowed-books.component';
import { BookListComponent } from '../book-list/book-list.component';
import { ReturnedBookComponent } from '../returned-book/returned-book.component';
import { ManageBookComponent } from '../manage-book/manage-book.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    RouterOutlet,
    MenuComponent,
    MyBooksComponent,
    BorrowedBooksComponent,
    BookListComponent,
    ReturnedBookComponent,
    ManageBookComponent
  ],
  providers:[
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

}

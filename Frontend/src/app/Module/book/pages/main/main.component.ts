import { Component, provideZoneChangeDetection } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MenuComponent } from '../../components/menu/menu.component';
import { MyBooksComponent } from '../my-books/my-books.component';
import { BorrowedBooksComponent } from '../borrowed-books/borrowed-books.component';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [
    RouterOutlet,
    MenuComponent,
    MyBooksComponent,
    BorrowedBooksComponent
  ],
  providers:[
  ],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss'
})
export class MainComponent {

}

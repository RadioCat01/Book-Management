import { Component, OnInit } from '@angular/core';
import { MyBooksComponent } from '../../pages/my-books/my-books.component';
import { Router, RouterLink } from '@angular/router';
import { BorrowedBooksComponent } from '../../pages/borrowed-books/borrowed-books.component';
import { ReturnedBookComponent } from '../../pages/returned-book/returned-book.component';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    RouterLink,
    MyBooksComponent,
    BorrowedBooksComponent,
    ReturnedBookComponent
  ],
  providers:[],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent implements OnInit {

  ngOnInit(): void {
    const linkColor = document.querySelectorAll('.nav-link');
    linkColor.forEach(link => {
      if (window.location.href.endsWith(link.getAttribute('href') || '')) {
        link.classList.add();
      }
      link.addEventListener('click', () => {
        linkColor.forEach(l => l.classList.remove('active'));
        link.classList.add('active');
      });
    });
  }

  logout():void{
    localStorage.removeItem('token');
    window.location.reload();
  }

}

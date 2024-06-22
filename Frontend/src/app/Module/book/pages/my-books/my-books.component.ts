import { Component, OnInit } from '@angular/core';
import { BookResponse, PageResponseBookResponse } from '../../../../Services/models';
import { BookService } from '../../../../Services/services';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BookCardComponent } from '../../components/book-card/book-card.component';

@Component({
  selector: 'app-my-books',
  standalone: true,
  imports: [
    CommonModule,
    BookCardComponent,
    RouterLink
  ],
  providers: [
    BookService,
  ],
  templateUrl: './my-books.component.html',
  styleUrl: './my-books.component.scss'
})
export class MyBooksComponent implements OnInit {
  bookResponse: PageResponseBookResponse = {};
  page = 0;
  size = 5;
  pages: any = [];

  constructor(
    private bookService: BookService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.findAllBooks();
  }

  archiveBook(book: BookResponse):void{
    this.bookService.updateArchiveStatus({
      'book-id': book.id as number
    }).subscribe({
      next: (): void => {
        book.archived = !book.archived;
      }
    })
  }
  
  shareBook(book: BookResponse):void{
    this.bookService.updateShareableStatus({
      'book-id': book.id as number
    }).subscribe({
      next: (): void => {
        book.shareable = !book.shareable;
      }
    })
  }
  
  editBook(book: BookResponse):void{
    this.router.navigate(['books', 'manage', book.id]);
  }



  private findAllBooks() {
    this.bookService.findAllBooksByOwner({
      page: this.page,
      size: this.size
    })
      .subscribe({
        next: (books) => {
          this.bookResponse = books;
          this.pages = Array(this.bookResponse.totalPages)
            .fill(0)
            .map((x, i) => i);
        }
      });
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllBooks();
  }

  goToLastPage() {
    this.page = this.bookResponse.totalPages as number - 1;
    this.findAllBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBooks();
  }

  get isLastPage() {
    return this.page === this.bookResponse.totalPages as number - 1;
  }

  displayBookDetails(book: BookResponse) {
    this.router.navigate(['books', 'details', book.id]);
  }
}

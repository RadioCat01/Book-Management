import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookResponse, BorrowedBookResponse, FeedbackRequest, PageResponseBorrowedBookResponse } from '../../../../Services/models';
import { BookService } from '../../../../Services/services/book.service';
import { RatingComponent } from '../../components/rating/rating.component';
import { FeedbackService } from '../../../../Services/services';

@Component({
  selector: 'app-borrowed-books',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RatingComponent
  ],
  providers:[
    FeedbackService,
    BookService
  ],
  templateUrl: './borrowed-books.component.html',
  styleUrl: './borrowed-books.component.scss'
})
export class BorrowedBooksComponent implements OnInit {

  feedbackRequest: FeedbackRequest = {bookId:0, comment: ''};
  page: number =0;
  size: number =5;
  pages: any = [];
  selectedBook: BookResponse | undefined = undefined;

  constructor(
    private bookService: BookService,
    private feedbackService: FeedbackService
  ){}

  ngOnInit(): void {
      this.findAllBorrowedBooks();
  }

  findAllBorrowedBooks(): void{
    this.bookService.findAllBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next:(res: PageResponseBorrowedBookResponse):void=>{
        this.borrowedBooks = res;
      }
    })
  }

  returnBook(withFeedback: boolean){
    this.bookService.returnBorrowedBook({
      'book-id': this.selectedBook?.id as number
    }).subscribe({
      next: (): void => {
        if(withFeedback){
          this.giveFeedback();
        }
        this.selectedBook = undefined;
        this.findAllBorrowedBooks();
      }
    })
  }
  giveFeedback():void{
    this.feedbackService.saveFeedback({
      body: this.feedbackRequest
    }).subscribe({
      next: (): void => {}
    });
  }

  borrowedBooks: PageResponseBorrowedBookResponse = {};

  returnBorrowedBook(book: BorrowedBookResponse): void{
    this.selectedBook = book;
    this.feedbackRequest.bookId = book.id as number;

  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllBorrowedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllBorrowedBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllBorrowedBooks();
  }

  goToLastPage() {
    this.page = this.borrowedBooks.totalPages as number - 1;
    this.findAllBorrowedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllBorrowedBooks();
  }

  get isLastPage() {
    return this.page === this.borrowedBooks.totalPages as number - 1;
  }


}

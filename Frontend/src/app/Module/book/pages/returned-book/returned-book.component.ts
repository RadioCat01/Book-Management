import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookResponse, BorrowedBookResponse, FeedbackRequest, PageResponseBorrowedBookResponse } from '../../../../Services/models';
import { BookService, FeedbackService } from '../../../../Services/services';

@Component({
  selector: 'app-returned-book',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  providers:[
    BookService
  ],
  templateUrl: './returned-book.component.html',
  styleUrl: './returned-book.component.scss'
})
export class ReturnedBookComponent implements OnInit{
  returnedBooks: PageResponseBorrowedBookResponse = {};
  page: number =0;
  size: number =5;
  pages: any = [];
  level:string = 'success';
  message:String = '';


  constructor(
    private bookService: BookService
  ){}

  ngOnInit(): void {
      this.findAllReturnedBooks();
  }

  findAllReturnedBooks(): void{
    this.bookService.findAllReturnedBooks({
      page: this.page,
      size: this.size
    }).subscribe({
      next:(res: PageResponseBorrowedBookResponse):void=>{
        this.returnedBooks = res;
      }
    })
  }

  approveBookReturn(book: BorrowedBookResponse): void{
    if(!book.returned){
      this.level='error';
      this.message = 'book is not returned';
      return;
    }
    this.bookService.approveReturnedBorrowedBook({
      'book-id': book.id as number
    }).subscribe({
      next: (): void =>{
        this.level='success';
        this.message = 'book return approved';
        this.findAllReturnedBooks();
      }
    })
  }

  gotToPage(page: number) {
    this.page = page;
    this.findAllReturnedBooks();
  }

  goToFirstPage() {
    this.page = 0;
    this.findAllReturnedBooks();
  }

  goToPreviousPage() {
    this.page --;
    this.findAllReturnedBooks();
  }

  goToLastPage() {
    this.page = this.returnedBooks.totalPages as number - 1;
    this.findAllReturnedBooks();
  }

  goToNextPage() {
    this.page++;
    this.findAllReturnedBooks();
  }

  get isLastPage() {
    return this.page === this.returnedBooks.totalPages as number - 1;
  }

}

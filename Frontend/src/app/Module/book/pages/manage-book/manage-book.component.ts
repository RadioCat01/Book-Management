import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BookRequest } from '../../../../Services/models/book-request';
import { BookService } from '../../../../Services/services/book.service';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, RouterLink } from '@angular/router';
import { BookResponse } from '../../../../Services/models';


@Component({
  selector: 'app-manage-book',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],
  providers:[
    BookService
    
  ],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit {


  constructor(
    private bookService: BookService,
    private router :Router,
    private activatedRout : ActivatedRoute
  ){}

  bookRequest: BookRequest = {authorName: '', isbn: '', synopsis: '', title: ''};
  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;

  onFileSelected(event : any): void{
    this.selectedBookCover = event.target.files[0];
    console.log(this.selectedBookCover);
    if(this.selectedBookCover){
      const reader: FileReader = new FileReader();
      reader.onload = ():void => {
        this.selectedPicture = reader.result as string;
      }
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook():void{
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next:(bookId:number):void =>{
        this.bookService.uploadBookCoverPicture({
          'book-id': bookId,
          body:{
            file: this.selectedBookCover
          }
        }).subscribe({
          next: (): void => {
            this.router.navigate(['/books']);
          }
        })
      },
      error: (err): void => {
        this.errorMsg = err.error.validationErrors;
      }
    })
  }

  ngOnInit(): void {
      const bookId= this.activatedRout.snapshot.params['bookId'];
      if(bookId){
        this.bookService.findBookById({
          'book-id': bookId
        }).subscribe({
          next:(book: BookResponse): void => {
            this.bookRequest ={
              id: book.id,
              title: book.title as string,
              authorName: book.authorName as string,
              isbn: book.isbn as string,
              synopsis: book.synopsis as string,
              shareable: book.shareable
            }
            if(book.cover){
              this.selectedPicture = 'data:image/jpg;base64, ' + book.cover;
            }
          }
        })
      }
  }

}

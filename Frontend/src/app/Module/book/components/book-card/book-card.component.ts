import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookResponse } from '../../../../Services/models';
import { CommonModule } from '@angular/common';
import { RatingComponent } from '../rating/rating.component';

@Component({
  selector: 'app-book-card',
  standalone: true,
  imports: [
    CommonModule,
    RatingComponent,  
  ],
  providers:[
    
  ],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {

  private _book: BookResponse = {};
  private _manage: boolean =false;
  private _bookCover: string | undefined;

  get manage():boolean {
    return this._manage;
  }

  @Input()
  set manage(value: boolean){
    this._manage = value;
  }

  get bookCover(): string | undefined {
    if(this._book.cover){
      return 'data:image/jpg;base64, ' + this._book.cover;
    }
    return '';
  }

  get book(): BookResponse{
    return this._book;
  }

  @Input()
  set book(value: BookResponse){
    this._book=value;
  }


  @Output() private share: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private archive: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private addToWaitingList: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private borrow: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private edit: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();
  @Output() private details: EventEmitter<BookResponse> = new EventEmitter<BookResponse>();


  onShowDetails():void{
    this.details.emit(this._book);
  }
  onBorrow():void{
    this.borrow.emit(this._book);
  }
  onEdit():void{
    this.edit.emit(this._book);
  }

  onAddToWaitingList():void{
    this.addToWaitingList.emit(this._book);
  }
  onShare():void{
    this.share.emit(this._book);
  }
  onArchive():void{
    this.archive.emit(this._book);
  }

}

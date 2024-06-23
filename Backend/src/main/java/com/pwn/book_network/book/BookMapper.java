package com.pwn.book_network.book;

import com.pwn.book_network.file.FileUtils;
import com.pwn.book_network.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BookMapper {

    public Book toBook(BookRequest request){
        return Book.builder()     // extract information in request and build book and return it
                .id(request.id())
                .title(request.title())
                .authorName(request.authorName())
                .isbn(request.isbn())
                .synopsis(request.synopsis())
                .archived(request.shareable())
                .shareable(request.shareable())
                .build();

    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()     // extract information from Book object, map them to a BookResponse Object and return
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .isbn(book.getIsbn())
                .synopsis(book.getSynopsis())
                .rate(book.getRate())
                .archived(book.isArchived())
                .shareable(book.isShareable())
                .owner(book.getOwner().fullName())
                .cover(FileUtils.readFilesFromLocation(book.getBookCover()))
                /*
                new the file location to the cover is saved in bookCover String in book Objects
                therefore program can read the file from location
                --> FileUtils class
                 */
                .build();
    }

    public BorrowedBookResponse toBorrowedBookResponse(BookTransactionHistory history) {
        return BorrowedBookResponse.builder()
                .id(history.getBook().getId())
                .title(history.getBook().getTitle())
                .authorName(history.getBook().getAuthorName())
                .isbn(history.getBook().getIsbn())
                .rate(history.getBook().getRate())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .build();
    }
}

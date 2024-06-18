package com.pwn.book_network.book;

import com.pwn.book_network.common.PageResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
@Tag(name = "Book")
public class BookController {

    private final BookService service;

    // Gets object type of BookRequest and save it in repository
    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @Valid @RequestBody BookRequest request,
            Authentication connectedUser
               /*
               now who is saving the book is the one who connected to the system
               therefore Ask spring to return an object of connected user with this,
               this can be used to identify the user
                */
    ){
        return ResponseEntity.ok(service.save(request,connectedUser)); // passes the request --> type of BookRequest and user
    }

    // Find book by id
    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(
            @PathVariable("book-id") Integer bookId  // gets a pathVariable book-id
    ){
        return ResponseEntity.ok(service.findById(bookId));  // findById method returns a Object of type BookResponse, return it
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> findAllBooks( // A custom PageResponse class is created --> common.PageResponse
            /*
             here a Paging functionality is implemented
             if there is a large number of books the response will be too heavy and time-consuming
             Therefore this method returns an object of type PageResponse --> A generic type

             */
            @RequestParam(name = "page",defaultValue = "0", required = false) int page, //get pages by number
            @RequestParam(name = "size",defaultValue = "10", required = false) int size,
            Authentication connectedUsers //get all books except the connected user
    ){
        return ResponseEntity.ok(service.findAllBooks(page,size,connectedUsers)); // inner method returns set of BookResponse object of type PageResponse, return it
    }

    //Same as above get books by owner
    @GetMapping("/owner")
    public ResponseEntity<PageResponse<BookResponse>> findAllBooksByOwner(
            @RequestParam(name = "page",defaultValue = "0", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size,
            Authentication connectedUsers
    ){
        return ResponseEntity.ok(service.findAllBooksByOwner(page,size,connectedUsers));
    }


    @GetMapping("/borrowed")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllBorrowedBooks(
            @RequestParam(name = "page",defaultValue = "0", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size,
            Authentication connectedUsers
    ){
        return ResponseEntity.ok(service.findAllBorrowedBooks(page,size,connectedUsers));
    }

    @GetMapping("/returned")
    public ResponseEntity<PageResponse<BorrowedBookResponse>> findAllReturnedBooks(
            @RequestParam(name = "page",defaultValue = "0", required = false) int page,
            @RequestParam(name = "size",defaultValue = "10", required = false) int size,
            Authentication connectedUsers
    ){
        return ResponseEntity.ok(service.findAllReturnedBooks(page,size,connectedUsers));
    }


    @PatchMapping("/shareable/{book-id}") // @PatchMapping is used to update a resource partially
    public ResponseEntity<Integer> updateShareableStatus(
            @PathVariable("book-id") Integer bookId, //get the bookId
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.updateShareableStatus(bookId, connectedUser)); //returns the bookId and is returned
    }


    //same as above
    @PatchMapping("/archived/{book-id}")
    public ResponseEntity<Integer> updateArchiveStatus(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUser
    ){
        return ResponseEntity.ok(service.updateArchiveStatus(bookId, connectedUser));
    }



    //Actual functions

    @PostMapping("/borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook( // get book id, call borrowBook method in service
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUsers
            ){
        return ResponseEntity.ok(service.borrowBook(bookId, connectedUsers)); // returns bookId
    }

    //same as borrow book
    @PatchMapping("/borrow/return/{book-id}")
    public ResponseEntity<Integer> returnBorrowedBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUsers
    ){
        return ResponseEntity.ok(service.returnBorrowedBook(bookId, connectedUsers));
    }


    //same as borrow book
    @PatchMapping("/borrow/return/approve/{book-id}")
    public ResponseEntity<Integer> approveReturnedBorrowedBook(
            @PathVariable("book-id") Integer bookId,
            Authentication connectedUsers
    ){
        return ResponseEntity.ok(service.approveReturnBorrowedBook(bookId, connectedUsers));
    }




                 //the end point with var    // specify that this method only handles requests with content type this,
    @PostMapping(value = "/cover/{book-id}", consumes = "multipart/form-data") //apply cover to book-id
    // consumes - narrows the primary mapping by media type that can be consumed by the map handler
    public ResponseEntity<?> uploadBookCoverPicture(
            @PathVariable("book-id") Integer bookId, // get the path variable
            @RequestPart("file") MultipartFile file,
            /*
             used to associate a part of a request named file with a method argument file
             here the annotation binds the " file " part of the multipart request to the "file" parameter
             */
            @Parameter() //from swagger
            Authentication connectedUsers
    ){
        service.uploadBookCoverPicture(file, connectedUsers, bookId);
        return ResponseEntity.accepted().build();
    }


}

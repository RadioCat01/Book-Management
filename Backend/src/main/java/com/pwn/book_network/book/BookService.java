package com.pwn.book_network.book;

import com.pwn.book_network.common.PageResponse;
import com.pwn.book_network.exception.OperationProhibitted;
import com.pwn.book_network.file.FileStorageService;
import com.pwn.book_network.history.BookTransactionHistory;
import com.pwn.book_network.history.BookTransactionHistoryRepository;
import com.pwn.book_network.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookTransactionHistoryRepository bookTransactionHistoryRepository;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest request, Authentication connectedUser){
        User user = ((User) connectedUser.getPrincipal()); // User connected user to extract userId
        Book book = bookMapper.toBook(request); // this mapper is used to create a new Book object and map data in the request with the book details
        book.setOwner(user); // set the book owner
        return bookRepository.save(book).getId(); // save the book in BookRepository
    }

    public BookResponse findById(Integer bookId) {                    // here call the bookRepository to find the book and then map it into a new bookResponse object and return it
        return bookRepository.findById(bookId)         // or else throw an exception
                .map(bookMapper::toBookResponse)
                .orElseThrow(()-> new EntityNotFoundException("No book found"));
    }


    //find all books
    public PageResponse<BookResponse> findAllBooks(int page, int size, Authentication connectedUsers) {
        User user= ((User) connectedUsers.getPrincipal()); // get user by Connected user

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());
        /*
           Pageable interface from spring framework.domain
           create a new pageable object by giving parameters, sort by and sorting
         */

        Page<Book> books = bookRepository.findAllDiaplayableBooks(pageable,user.getId()); //call the repository and the custom query
        /*
           this returns a
         */
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();  // create List of bookResponses in same old way and save it to a list
        return new PageResponse<>( // return all the displayable books
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }


    public PageResponse<BookResponse> findAllBooksByOwner(int page, int size, Authentication connectedUsers) {
        User user= ((User) connectedUsers.getPrincipal()); //get user

        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending()); // same as above

        Page<Book> books = bookRepository.findAll(BookSpecification.withOwnerId(user.getId()), pageable); // here a specification is passed to filter the books --> BookSpecification
        /*
             findAll method gets all the books in the repository and then by the criteria builder in book specification class will filter them to get all the books by id
             to do this the BookRepository need to extend ** JpaSpecificationExecutor **
         */
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .toList();
        return new PageResponse<>( // same as above ***
                bookResponses,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BorrowedBookResponse> findAllBorrowedBooks(int page, int size, Authentication connectedUsers) {
        User user= ((User) connectedUsers.getPrincipal()); //same
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending()); //same

        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllBorrowedBooks(pageable, user.getId());
        /*
        this time get all books by a custom query from bookTransactionHistoryRepository
         */

        List<BorrowedBookResponse> bookResponses= allBorrowedBooks.stream() // same from here **
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }

    // same as FindAAllBorrowedBooks
    public PageResponse<BorrowedBookResponse> findAllReturnedBooks(int page, int size, Authentication connectedUsers) {
        User user= ((User) connectedUsers.getPrincipal());
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdDate").descending());

        Page<BookTransactionHistory> allBorrowedBooks = bookTransactionHistoryRepository.findAllReturnedBooks(pageable, user.getId());

        List<BorrowedBookResponse> bookResponses= allBorrowedBooks.stream()
                .map(bookMapper::toBorrowedBookResponse)
                .toList();
        return new PageResponse<>(
                bookResponses,
                allBorrowedBooks.getNumber(),
                allBorrowedBooks.getSize(),
                allBorrowedBooks.getTotalElements(),
                allBorrowedBooks.getTotalPages(),
                allBorrowedBooks.isFirst(),
                allBorrowedBooks.isLast()
        );
    }


    public Integer updateShareableStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book of the id"+bookId)); //get the book by id

        User user= ((User) connectedUser.getPrincipal()); // get the user

        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationProhibitted("Cannot Update Shareable Status");
            /*
             if not the book owner can't change the status
             OperationProhibited is a custom exception --> exception.OperationProhibited
             */
        }

        book.setShareable(!book.isShareable()); //change the status
        bookRepository.save(book); // save the book in repository
        return bookId;
    }

    // same as updateShareableStatus
    public Integer updateArchiveStatus(Integer bookId, Authentication connectedUser) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new EntityNotFoundException("No book of the id"+bookId));

        User user= ((User) connectedUser.getPrincipal());

        if(!Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationProhibitted("Cannot Update Archive Status");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return bookId;
    }


    //Actual functions

    public Integer borrowBook(Integer bookId, Authentication connectedUsers) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("Book not Found" + bookId)); //find the book

        if(book.isArchived() || !book.isShareable()){ // check if is available to borrow
            throw new OperationProhibitted("Book can't be borrowed ");
        }

        User user= ((User) connectedUsers.getPrincipal()); // get user
        if(Objects.equals(book.getOwner().getId(), user.getId())){ // check if the book is borrowed by the same user
            throw new OperationProhibitted("You cannot borrow your own book");
        }

        final boolean isAlreadyBorrowed = bookTransactionHistoryRepository.isAlreadyBorrowed(bookId, user.getId()); //get a list of already borrowed books by a custom query

        if(isAlreadyBorrowed){
            throw new OperationProhibitted("Book is already borrowed"); // check if borrowed or not
        }

        BookTransactionHistory bookTransactionHistory = BookTransactionHistory.builder()
                /*
                here create a new object of bookTransactionHistory to update the owner of the book
                 */
                .user(user)
                .book(book)
                .returned(false)
                .returnApproved(false)
                .build();
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId(); //save it and return the id
    }



    public Integer returnBorrowedBook(Integer bookId, Authentication connectedUsers) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("Book not Found" + bookId)); //get book by id

        if(book.isArchived() || !book.isShareable()){
            throw new OperationProhibitted("Book can't be borrowed "); //check if is borrowed
        }

        User user= ((User) connectedUsers.getPrincipal()); // get user

        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationProhibitted("You cannot borrow or return your own book");
        }

        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndUserId(bookId,user.getId())
                /*
                here pass bookId and userId to query to find if the current user bought the book
                 */
                .orElseThrow(()-> new OperationProhibitted("You did not borrow this book"));

        bookTransactionHistory.setReturned(true); // set new value returned true
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId(); // save it to repository and return the bookId
    }


    public Integer approveReturnBorrowedBook(Integer bookId, Authentication connectedUsers) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("Book not Found" + bookId));

        if(book.isArchived() || !book.isShareable()){
            throw new OperationProhibitted("Book can't be borrowed ");
        }

        User user= ((User) connectedUsers.getPrincipal());
        if(Objects.equals(book.getOwner().getId(), user.getId())){
            throw new OperationProhibitted("You cannot borrow your own book");
        }
        BookTransactionHistory bookTransactionHistory = bookTransactionHistoryRepository.findByBookIdAndOwnerId(bookId,user.getId())
                /*
                here use any book with bookId and ownerId , returned = true and not yet approved
                 */
                .orElseThrow(()-> new OperationProhibitted("You did not returned yet"));

        bookTransactionHistory.setReturnApproved(true); // change and save
        return bookTransactionHistoryRepository.save(bookTransactionHistory).getId(); //return bookId
    }





    public void uploadBookCoverPicture(MultipartFile file, Authentication connectedUsers, Integer bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new EntityNotFoundException("Book not Found" + bookId)); //get book by id

        User user= ((User) connectedUsers.getPrincipal()); //get user by id

        var bookCover = fileStorageService.saveFile(file, user.getId()); // pass the file to the method

        book.setBookCover(bookCover); //save the string to book object
        bookRepository.save(book); // save to repository
    }
}







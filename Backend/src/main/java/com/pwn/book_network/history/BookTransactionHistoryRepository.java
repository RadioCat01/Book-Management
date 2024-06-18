package com.pwn.book_network.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookTransactionHistoryRepository extends JpaRepository<BookTransactionHistory, Integer> {

    //find all borrowed books and return
    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.user.id = :userId
            """)
    Page<BookTransactionHistory> findAllBorrowedBooks(Pageable pageable, Integer userId);


    @Query("""
            SELECT history
            FROM BookTransactionHistory history
            WHERE history.book.owner.id = :userId
            """)
    Page<BookTransactionHistory> findAllReturnedBooks(Pageable pageable, Integer userId);


    //check if the book is already borrowed
    @Query(
            """
                    SELECT CASE WHEN COUNT(bookTransactionHistory) > 0 THEN true ELSE false END
                    FROM BookTransactionHistory bookTransactionHistory
                    WHERE bookTransactionHistory.user.id = :userId
                    AND bookTransactionHistory.book.id = :bookId
                    AND bookTransactionHistory.returnApproved=false
                    """
    )
    boolean isAlreadyBorrowed(Integer bookId, Integer userId);
    

    @Query(
            """
                    SELECT transaction
                    FROM BookTransactionHistory transaction
                    WHERE transaction.user.id = :userId
                    AND transaction.book.id = :bookId
                    AND transaction.returned = false
                    AND transaction.returnApproved = false
                    
                    """
    )
    Optional<BookTransactionHistory> findByBookIdAndUserId(Integer bookId, Integer userId); // find a book by bookId and userId


    @Query(
            """
                    SELECT transaction
                    FROM BookTransactionHistory transaction
                    WHERE transaction.book.owner.id = :userId
                    AND transaction.book.id = :bookId
                    AND transaction.returned = true
                    AND transaction.returnApproved = false
                    
                    """
    )
    Optional<BookTransactionHistory> findByBookIdAndOwnerId(Integer bookId, Integer userId);
}






















package com.pwn.book_network.history;

import com.pwn.book_network.book.Book;
import com.pwn.book_network.common.BaseEntity;
import com.pwn.book_network.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BookTransactionHistory extends BaseEntity {

    // One transaction history is related to one user
    @ManyToOne
    @JoinColumn(name = "user_id") // User id as foreign key
    private User user;

    // One transaction history is related to one book
    @ManyToOne
    @JoinColumn(name = "book_id") // Book id as foreign key
    private Book book;

    private boolean returned;
    private boolean returnApproved;
}

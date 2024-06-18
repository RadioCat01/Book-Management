package com.pwn.book_network.feedback;

import com.pwn.book_network.book.Book;
import com.pwn.book_network.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class FeedBack extends BaseEntity {
    /* Feedback entity extends BaseEntity where all common auditing Variables,
       Id and AuditingEntityListener is at
    */


    private Double note;
    private String comment;

    //Many books have one feedback
    @ManyToOne
    @JoinColumn(name = "Book_Id")
    private Book book;


}

package com.pwn.book_network.book;

import com.pwn.book_network.common.BaseEntity;
import com.pwn.book_network.feedback.FeedBack;
import com.pwn.book_network.history.BookTransactionHistory;
import com.pwn.book_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Entity
@Getter
@Setter
@SuperBuilder // for creating hierarchy - " book extends BaseEntity "
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {
    /* book entity extends BaseEntity where all common auditing Variables,
       Id and AuditingEntityListener is at
    */
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String bookCover;
    private boolean archived;
    private boolean shareable;

    //Many books have One owner
    @ManyToOne
    @JoinColumn(name="Owner_Id") //User id as foreign key
    private User owner;

    //One book has Many feedbacks
    @OneToMany(mappedBy = "book")
    private List<FeedBack> feedbacks;

    //One book has many transaction histories
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;



    // implementation to get rate --> custom logic
    @Transient
    public double getRate(){
        if(feedbacks==null || feedbacks.isEmpty()){
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(FeedBack::getNote)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate*10.0)/10.0;
        return roundedRate;
    }


}

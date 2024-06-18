package com.pwn.book_network.feedback;

import com.pwn.book_network.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

    public FeedBack toFeedback(FeedbackRequest request) {
        return FeedBack.builder()
                .note(request.note())
                .comment(request.comment())
                .book(Book.builder()
                        .id(request.bookId())
                        .archived(false) // has no impact
                        .shareable(false)
                        .build()
                )
                .build();
        //classic mapping
    }

    public FeedbackResponse toFeedbackResponse(FeedBack feedBack, Integer id) {

        return FeedbackResponse.builder()
                .note(feedBack.getNote())
                .comment(feedBack.getComment())
                .ownFeedback(Objects.equals(feedBack.getCreatedBy(), id))
                .build();
        //classic mapping
    }
}

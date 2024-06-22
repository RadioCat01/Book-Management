package com.pwn.book_network.book;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.PrivateKey;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse { // this is the structure of book response

    private Integer id;
    private String title;
    private String authorName;
    private String isbn;
    private String synopsis;
    private String owner;
    private String cover; //implemented
    private double rate; // average of all the feedbacks multiplied by number of feedbacks --> @Transient annotated method in Book class
    private boolean archived;
    private boolean shareable;

}

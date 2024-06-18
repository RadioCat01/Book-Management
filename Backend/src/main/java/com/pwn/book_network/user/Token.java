package com.pwn.book_network.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Token {

    @Id
    @GeneratedValue
    private Integer id;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    //User can have many tokens - UNI Directional Relationship
    @ManyToOne
    @JoinColumn(name = "userID",nullable = false) // creates a new column in Token table which takes user table's Id as the foreign key
    private User user;


}

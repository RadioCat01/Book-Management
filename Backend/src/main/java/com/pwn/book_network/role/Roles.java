package com.pwn.book_network.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pwn.book_network.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Roles {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;


    //A single role can have many users
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore //Prevent serialization when fetched as the related User class fetches data Eagerly
    private List<User> users;

    //Auditing
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @CreatedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

}

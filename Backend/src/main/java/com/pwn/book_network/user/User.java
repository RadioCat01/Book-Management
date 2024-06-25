package com.pwn.book_network.user;

import com.pwn.book_network.book.Book;
import com.pwn.book_network.history.BookTransactionHistory;
import com.pwn.book_network.role.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "_User")
@EntityListeners(AuditingEntityListener.class) //Auditing is enabled - audit logging / tracking changes in DB entity
public class User implements UserDetails, Principal {

    //implement UserDetails to load specific user data during authentication below mentioned @Override methods except getAuthorities
    //implement Principal to implement classes with possible future updates, and to extend the behaviour class itself don't need to be changed
    @Id
    @GeneratedValue
    private Integer id;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    private String password;
    private LocalDateTime dateOfBirth;
    private boolean accountLocked;
    private boolean enabled;


    //Relations
    // User can have many roles
    @ManyToMany(fetch = FetchType.EAGER) // All related data will be fetched
    private List<Roles> roles;


    // One User has many books
    @OneToMany(mappedBy = "owner")// necessary
    private List<Book> books;


    // One user can have many transaction histories
    @OneToMany(mappedBy = "user")
    private List<BookTransactionHistory> histories;






    //Editing entities
    @CreatedDate // set as needs to Audited Automatically
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @CreatedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;




    //Security
    /* retrieves a collection of SimpleGrantedAuthority objects that represent user's authorities by mapping each role
     in the user's roles collection to SimpleGrantedAuthority instance */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream() //stream of roles collection - access to elements without iterating
                .map(r ->new SimpleGrantedAuthority(r.getName())) //creates SimpleGrantedAuthority object for each role
                .collect(Collectors.toList()); //terminates the stream and convert the processed element to a collection(list)
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public String getName() {
        return email;
    }

    public String fullName(){
        return firstName+ " " + lastName;
    }


}

package com.pwn.book_network.security;

import com.pwn.book_network.user.userRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceIMPL implements UserDetailsService {

    private final userRepository repository; // load users from database

    @Override
    @Transactional // load roles along with users
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        return repository.findByEmail(userEmail)
                .orElseThrow(()->new UsernameNotFoundException("User not found !")); //find the user and return by email
    }

}

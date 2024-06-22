package com.pwn.book_network.auth;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse { //type of authentication response
    private String token;
}

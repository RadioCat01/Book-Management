package com.pwn.book_network.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse { //type of authentication response
    private String token;
}

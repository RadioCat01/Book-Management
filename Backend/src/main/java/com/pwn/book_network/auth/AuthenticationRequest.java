package com.pwn.book_network.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest { //type of authentication / login request

    @NotEmpty(message = "Firstname is required")
    @NotBlank(message = "Fill this")
    @Email(message = "invalid")
    private String email;
    @NotEmpty(message = "Firstname is required")
    @NotBlank(message = "Fill this")
    @Size(min=8, message = "Use 8 char")
    private String password;

}

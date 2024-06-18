package com.pwn.book_network.book;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookRequest(

        /*
          this is a record*
          which is used to define the structure of book request
          there are the required information to create a book
         */

        Integer id,
        @NotNull(message = "100")
        @NotEmpty(message = "100") // A dictionary is implemented in frontend to define the messages by number
        String title,

        @NotNull(message = "101")
        @NotEmpty(message = "102")
        String authorName,

        @NotNull(message = "102")
        @NotEmpty(message = "102")
        String isbn,

        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String synopsis,

        boolean shareable
) {
}

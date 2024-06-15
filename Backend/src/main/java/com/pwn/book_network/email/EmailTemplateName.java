package com.pwn.book_network.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName { //enum allows to define a fixed set of named constants

    ACTIVATE_ACCOUNT("activate_account"); // defined enum constant ' ACTIVATE_ACCOUNT ' with string value
    // this will look into resource/templates folder to get activate_account template if this is called later on

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}

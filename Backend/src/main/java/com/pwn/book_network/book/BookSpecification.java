package com.pwn.book_network.book;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {

    public static Specification<Book> withOwnerId(Integer ownerId){ //Specification of type book --> by owner Id
        /*
        here below expression builds a criteria to filter the books by owner Id
         */
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"),ownerId);
    }

}

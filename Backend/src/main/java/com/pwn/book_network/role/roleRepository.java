package com.pwn.book_network.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface roleRepository extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByName(String role);
}

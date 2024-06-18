package com.pwn.book_network.config;

import com.pwn.book_network.user.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class ApplicationAuditAware implements AuditorAware<Integer> {
    /*
    here AuditorAware gets a generic type,
    Application requires to audit " who did what " by auditing the user
    users has Integer id , therefore implement <Integer>
    */

    //Need to create a bean of this type to inject into other classes --> BeansConfig
    @Override
    public Optional<Integer> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); //Get authentication object from security context holder
        if(authentication==null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken){
            return Optional.empty(); // if user not found return empty
        }
        User userPrinciple = (User) authentication.getPrincipal(); // cast authentication object to user to get user
        return Optional.ofNullable(userPrinciple.getId()); //return user Id
    }
}

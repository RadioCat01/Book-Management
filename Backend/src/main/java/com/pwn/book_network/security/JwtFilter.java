package com.pwn.book_network.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // make the class a filter

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal( // implemented method
          @NotNull HttpServletRequest request,
          @NotNull HttpServletResponse response,
          @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        if(request.getServletPath().contains("/api/v1/auth")){
            filterChain.doFilter(request, response); // skip this for above path
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        /*
        GET /resource HTTP/1.1
        Host: www.example.com
        Content-Type: application/json
        Authorization: Bearer 1234567890abcdef   - this is the JWT token which is extracted to authHeader String
        */
        final String jwt;
        final String userEmail;
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response); //if the header doesn't start with that ignore ( not related to JWT )
            return;
        }

        jwt= authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt); // extract email from the JWT token

        //there's an user and is not already authenticated
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            //Load the details of the given user by email
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            //validate token by comparing the extracted jwt and userDetails taken from database
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}

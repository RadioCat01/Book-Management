Personalized Book management system built with Spring Boot

Security

  - securityFilterChain - (bean) accepts object of type HttpSecurity
    
    - CORS ( Cross-Origin Resource Sharing)
              - Security feature implemented by web browseor that restricts web pages from making requests to a different origin ( domain/ protocol/ port )
              - " .cors(withDefaults()) " This method configures CORS support with default settings provided by Spring Security.
              - Defaults allows CORS requests from any origin (*), with any method, and with certain standard headers like Content-Type, Authorization, etc.

    - CSRF ( Cross-site request forgery)
              - CSRF is an attack where unauthorized commands are transmitted from a user that the web application trusts.
              - It occurs when a malicious website, email, or other forms of request force a user's web browser to perform an unwanted action on a trusted site.
              - " .csrf(AbstractHttpConfigurer::disable) " this method disables CSRF protection
              - When using an JWT token in a single page application, CSRF protection may not be necessary

    - authorizeHttpRequest
              - Allowing you to define which endpoints are accessible to which users or roles
              - " req.requestMatchers(...) " Defines a list of URL patterns to match against incoming requests which are allowed for public access ( .permitAll )
              - " .anyRequest().authenticate() " ensures any other request which is not specifically matched, requires authentication

    - sessionManagement
              - configures how sessions are managed for authenticated users
              - sessions are used to maintain stateful interactions with users across multiple requests
              - When using JWT session management is not necessary
              - " sessionCreationPolicy(STATELESS) " specifies that Spring Security should not create or manage sessions for authenticated users

    - authenticationProvider ( login )
              - an interface in Spring Security responsible for authenticating users based on provided credentials
              - a bean is created to inject this
      
         - Authentication provider bean
                  - DaoAuthenticationProvider : A concrete implementation of AuthenticationProvider provided by Spring Security.
                  - It validates credentials using a " UserDetailsService " and optionally checks password validity using a " PasswordEncoder ".
         - userDetailService
                  - this loads user details from repository

              - .authenticationProvider(authenticationProvider)
                    - When a user attempts to authenticate (e.g., via login form or API), Spring Security intercepts the request.
                    - It uses the configured " AuthenticationProvider " (DaoAuthenticationProvider in this case) to authenticate the user.
                    - The provider retrieves user details from userDetailsService, compares the provided credentials with the stored credentials (after optional encoding),
                    - and constructs an Authentication object if successful.
                    - Upon successful authentication, Spring Security stores the Authentication object in SecurityContextHolder,
                    - indicating the user is authenticated for the duration of the request.

    - .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) ( user validity )
              - Injects a custom filter (jwtAuthFilter) into the Spring Security filter chain before the standard UsernamePasswordAuthenticationFilter.

         - JwtFilter class extends OncePerRequestFilter
                    - logic to extract JWT tokens from incoming requests and validate
         - JwtService class
                    - logic to Generate, Decode, Extract information, validate the token

    - .authenticationProvider + .addfilterBefore
         - Authentication Provider: Handles traditional username-password authentication using AuthenticationProvider configured with UserDetailsService.
         - JWT Filter: Intercepts requests to validate JWT tokens, set authentication context, and ensure secure access to protected resources.
         - Session Management: Configured to use STATELESS session management, ensuring Spring Security does not create or store session information, relying entirely on JWT tokens for authentication.
               -  The JwtFilter intercepts requests to extract and validate JWT tokens.
               - If valid, it sets up the authentication context using SecurityContextHolder.
               - If not valid or absent, authentication falls back to the configured AuthenticationProvider (DaoAuthenticationProvider), which handles traditional username-password authentication.






















      

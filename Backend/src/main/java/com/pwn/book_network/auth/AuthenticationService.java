package com.pwn.book_network.auth;

import com.pwn.book_network.email.EmailTemplateName;
import com.pwn.book_network.security.JwtService;
import com.pwn.book_network.user.Token;
import com.pwn.book_network.user.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final com.pwn.book_network.role.roleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.pwn.book_network.user.userRepository userRepository;
    private final com.pwn.book_network.user.tokenRepository tokenRepository;
    private final com.pwn.book_network.email.EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.mailing.frontend.activation-url}") // A routing url which is implemented with frontend
    private String activationUrl;                            // is mentioned in .yml file


    // Register user
    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER") // fetch role by name and assign to user
                .orElseThrow(()->new IllegalStateException("Role was not found"));

        var user= User.builder() // create a user object and save data
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false) //account is initially disabled until user activate it using the code
                .roles(List.of(userRole)) // assign role to the user
                .build();

        userRepository.save(user); // save new user
        sendValidationEmail(user);
    }

    //Send validation email
    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user); //call method

        // implement the email service, send emil with configured user detais template and generated token
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken.toString(),
                "Account activation"
        );
    }

    // create a new Activation token and save
    private Object generateAndSaveActivationToken(User user) {
        String generatedToken = generateAndSaveActivationCode(6); // call method

        // create an object type of token and save to repository
        var token= Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        //return the Activation token code
        return generatedToken;
    }

    //generate token based on length
    private String generateAndSaveActivationCode(int length) {

        // logic for creating token
        String character = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i=0; i< length; i++){
            int randomIndex = secureRandom.nextInt(character.length());
            codeBuilder.append(character.charAt(randomIndex));
        }
        //return the generate Activating code
        return codeBuilder.toString();
    }




    // Authenticate ( Login user )
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate( // call authenticate method
                // this method will check and authenticate the request
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        // after authentication is successful
        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal()); // case authentication to user that's why the User class implemented " Principal "
        claims.put("fullname", user.fullName());

        // Now create a new JWT token
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }



    // Activate account - gets String token parameter
    // @Transactional
    /*  this annotation ensures all database operations happen under this method treated as a single unit
        which means if any operation fails, the entire transaction rolls back, therefore if the given token is
        invalid and sent exception, method rolls back without sending an email again. - that's why a new validation email
        did not send
    * */
    public void activateAccount(String token) throws MessagingException {
        Token savedtoken = tokenRepository.findByToken(token) //get the token from database by the given parameter
                .orElseThrow(()->new RuntimeException("Invalid token"));

        //check if the token is expired
        if(LocalDateTime.now().isAfter(savedtoken.getExpiresAt())){
            sendValidationEmail((savedtoken.getUser()));
            throw new RuntimeException("Activation token is expired New one sent");
        }

        //if not expired
        //get the user from token, or to double-check get user from repository by the id in token
        var user= userRepository.findById(savedtoken.getUser().getId())
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        //enable user
        user.setEnabled(true);
        //persist the new user values to database
        userRepository.save(user);
        savedtoken.setValidatedAt(LocalDateTime.now());
        //update the token repository that the token is validated
        tokenRepository.save(savedtoken);
    }
}















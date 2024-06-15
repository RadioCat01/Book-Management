package com.pwn.book_network.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;


    @Async //this takes time - need to @EnableAsync at main class
    public void sendEmail( // structure of sending email
            String to,
            String username,
            EmailTemplateName emailTemplate, // specifies the type of email template to send - implemented
            String confirmationURL,
            String activationCode,
            String subject
    ) throws MessagingException {
        String templateName;
        if(emailTemplate ==null){
            templateName="confirm-email"; // pass template name if emailTemplate is null
        }else {
            templateName= emailTemplate.name();
        }

        //configure mail sender
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                mimeMessage,
                MULTIPART_MODE_MIXED,
                UTF_8.name()
        );

        // Pass parameters to email template
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("confirmationURL", confirmationURL);
        properties.put("activationCode", activationCode);

        //thymeleaf context
        Context context = new Context();
        context.setVariables(properties); // set properties to the template

        //send to and from
        helper.setFrom("pwnkanishka@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);

        // process the template passing template name and context
        String template = templateEngine.process(templateName, context);
        /* spring will look into resource folder to get any html template
        and there's a html template with thymeleaf that has spaces for dynamic updates
        for the variables that are set in above properties Mapping.
        this method will process and create the html page with given variables
        */

        helper.setText(template, true); // set if a html or not = true
        mailSender.send(mimeMessage);
    }
}

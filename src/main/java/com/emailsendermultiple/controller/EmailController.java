package com.emailsendermultiple.controller;

import com.emailsendermultiple.dto.EmailRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class EmailController {

    private final JavaMailSender javaMailSender;

    private static final String HTML_BODY = "<html><body><h1>Hello!</h1><p>This is a test HTML email.</p></body></html>";
    private static final String NORMAL_BODY = "Hello";


    @PostMapping("send")
    public ResponseEntity<String> sendEmail(@RequestBody List<EmailRequest> emailRequests) {
        try {
            for (EmailRequest emailRequest : emailRequests) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("service@paypal <lagos@gmail.com>");
                message.setTo(emailRequest.getRecipient());
                message.setSubject(emailRequest.getSubject());
                message.setText(NORMAL_BODY);

                javaMailSender.send(message);
            }

            return ResponseEntity.ok("Email sent successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("send-html")
    public ResponseEntity<String> sendHtmlEmail(@RequestBody List<EmailRequest> emailRequests) {
        try {
            for (EmailRequest emailRequest : emailRequests) {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(emailRequest.getRecipient());
                helper.setSubject(emailRequest.getSubject());
                helper.setText(HTML_BODY, true);

                javaMailSender.send(message);
            }
            return ResponseEntity.ok("HTML Email sent successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send HTML email: " + e.getMessage());
        }
    }
}

package com.ojass.sathelper.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * Created by Chandana on 9/16/2016.
 */
public class Mailer {

    @Autowired
    private MailSender mailSender;

    public Mailer() {

    }

    public void sendMails(String emailId, String content, String subject) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setText(content);
            message.setSubject(subject);
            message.setTo(emailId);
            message.setFrom("admin@ojass.com");
            mailSender.send(message);
        } catch (Exception ex) {

            System.out.println("Sending Mail to " + emailId);
        }

    }
}

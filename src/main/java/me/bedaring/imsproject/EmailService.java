package me.bedaring.imsproject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * This is an email service class to send emails
 */
@Service
public class EmailService{

    private JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(final Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mail.getSubject());
        message.setText(mail.getContent());

        // check if 'to' or 'toMultiple' was set (ie. sending to a single address or multiple addresses) and pass the
        // appropriate field to 'setTo'
        if (mail.getTo() == null) {
            message.setTo(mail.getToMultiple());
        } else {
            message.setTo(mail.getTo());
        }

        message.setFrom(mail.getFrom());

        javaMailSender.send(message);
    }
}

package me.bedaring.imsproject.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration to implement the JavaMailSender bean
 */
@Configuration
public class MailConfiguration {
    @Value("smtp")
    private String protocol;
    @Value("smtp.gmail.com")
    private String host;
    @Value("587")
    private int port;
    @Value("true")
    private boolean auth;
    @Value("true")
    private boolean starttls;
    @Value("bedaring.me@gmail.com")
    private String username;
    @Value("Syd*Eli2017")
    private String password;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        Properties mailProperties = new Properties();
        mailProperties.put("mail.smtp.auth", auth);
        mailProperties.put("mail.smtp.starttls.enable", starttls);

        javaMailSender.setJavaMailProperties(mailProperties);
        javaMailSender.setProtocol(protocol);
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);

        return javaMailSender;
    }
}

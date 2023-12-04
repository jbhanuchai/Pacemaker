package comm.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com"); // Update this to your SMTP server address if not using Gmail
        mailSender.setPort(587); // Use the appropriate port for your email server
        mailSender.setUsername("mail@gmail.com"); // Replace with your email address
        mailSender.setPassword("app_password"); // Replace with your email password or app password if using 2FA

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Set to true for debugging purposes

        return mailSender;
    }

}

package com.example.Nofication.Service;
import com.example.Nofication.Model.EmailDetail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    private TokenUtil tokenUtil;

    public void sendEmail(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("name", emailDetail.getReceiver());
            context.setVariable("button", "Go to login page");
            context.setVariable("link", emailDetail.getLink());

            String template = templateEngine.process("forgot-password", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("nghialncse170125@fpt.edu.vn");
            mimeMessageHelper.setTo(emailDetail.getReceiver());
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            mimeMessageHelper.setText(template, true);

            javaMailSender.send(mimeMessage);
        } catch (MailAuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Messaging error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
        }
    }

    public void sendEmailNotiSubmitApplication(EmailDetail emailDetail) {
        try {
            Context context = new Context();
            context.setVariable("fullname", emailDetail.getFullname());
            context.setVariable("phone", emailDetail.getPhone());
            context.setVariable("specialization", emailDetail.getSpecialization());
            context.setVariable("campus", emailDetail.getCampus());

            String template = templateEngine.process("submit-application", context);
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom("nghialncse170125@fpt.edu.vn");
            mimeMessageHelper.setTo(emailDetail.getReceiver());
            mimeMessageHelper.setSubject(emailDetail.getSubject());
            mimeMessageHelper.setText(template, true);

            javaMailSender.send(mimeMessage);
        } catch (MailAuthenticationException e) {
            System.err.println("Authentication failed: " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("Messaging error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
        }
    }
}
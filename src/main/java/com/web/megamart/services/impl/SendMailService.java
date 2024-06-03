package com.web.megamart.services.impl;

import com.web.megamart.exception.SendMailException;
import com.web.megamart.payload.MailData;
import com.web.megamart.services.MailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class SendMailService implements MailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public SendMailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public boolean sendMail(MailData mailData) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(mailData.getSendTo());
            helper.setSubject(mailData.getSubject());
            helper.setText(mailData.getMessage(), true);
            javaMailSender.send(message);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SendMailException("failed to send email to: "+mailData.getSendTo(), HttpStatus.BAD_REQUEST);
        }
        return true;
    }
}

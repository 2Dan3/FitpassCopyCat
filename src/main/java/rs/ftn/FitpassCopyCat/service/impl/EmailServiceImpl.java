package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendEmailWithPassword(final String to, final String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@fitpassclone.com");
        message.setTo(to);
        message.setSubject("FitpassClone Password Setting");
        message.setText(String.format("Your password used to log into FitpassClone account %s is now %s.\nMake sure to keep it safe.\nYou may always choose a different password later.\n\n\nHappy training,\nFitpassClone Team.", to, password));
        emailSender.send(message);
    }
}
package rs.ftn.FitpassCopyCat.service;

public interface EmailService {

    void sendEmailWithPassword(String recipientEmail, String initiallyGeneratedPassword);
}

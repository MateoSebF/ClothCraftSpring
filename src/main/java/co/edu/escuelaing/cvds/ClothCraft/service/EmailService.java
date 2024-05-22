package co.edu.escuelaing.cvds.ClothCraft.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class EmailService {

    public void sendVerificationEmail(String to, String token) {
        String from = "notificaciones.cloth.craft@gmail.com"; // Cambiar por tu correo electrónico
        String password = "Ciclosdevida2024#"; // Cambiar por tu contraseña
        String host = "smtp.gmail.com"; // Servidor SMTP de Gmail

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587"); // Puerto TLS
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Activate your account");
            message.setText("Click the link to activate your account: https://clothcraft.azurewebsites.net/user/verify?token=" + token);

            Transport.send(message);
            System.out.println("Verification email sent successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
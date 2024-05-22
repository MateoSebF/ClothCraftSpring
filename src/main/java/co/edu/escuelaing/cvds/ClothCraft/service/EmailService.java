package co.edu.escuelaing.cvds.ClothCraft.service;

import org.springframework.stereotype.Service;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

@Service
public class EmailService {

    public void sendVerificationEmail(String to, String token) {
        String from = "notificaciones.cloth.craft@gmail.com"; // Cambiar por tu correo electrónico
        String password = "yrvp ffpv zhca mxwe"; // Cambiar por tu contraseña
        String host = "smtp.gmail.com"; // Servidor SMTP de Gmail

        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", "587"); // Puerto TLS
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        System.out.println("Creating session...");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        System.out.println("Session created");
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
            throw new RuntimeException("Error sending verification email");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error sending verification email");
        }
    }

    public void sendOutfitNotificationEmail(String to, Outfit outfit, Day day) {
        String from = "notificaciones.cloth.craft@gmail.com"; // Cambiar por tu correo electrónico
        String password = "yrvp ffpv zhca mxwe"; // Cambiar por tu contraseña de aplicación
        String host = "smtp.gmail.com"; // Servidor SMTP de Gmail

        Properties properties = new Properties();
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
            message.setSubject("Prepare your outfit!");

            StringBuilder emailContent = new StringBuilder();
            emailContent.append("Don't forget to wear your outfit for ").append(day.getDate()).append("\n\n");
            emailContent.append("Outfit name: ").append(outfit.getName()).append("\n\n");
            emailContent.append("Clothes:\n");

            for (Clothing clothing : outfit.getClothes()) {
                emailContent.append(clothing.getName()).append("\n");
            }

            message.setText(emailContent.toString());

            Transport.send(message);
            System.out.println("Email sent successfully...");
        } catch (MessagingException mex) {
            mex.printStackTrace();
            throw new RuntimeException("Error sending email", mex);
        }
    }
}
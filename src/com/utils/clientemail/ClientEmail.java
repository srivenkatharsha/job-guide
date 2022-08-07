package com.utils.clientemail;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ClientEmail {
    private final String fromAddress,toAddress,password,subject,textHTML;
    private final File attachment;
    public ClientEmail(String fromAddress,String password,String toAddress,String subject,String textHTML,File attachment){
        this.toAddress = toAddress;
        this.fromAddress = fromAddress;
        this.password = password;
        this.attachment = attachment;
        this.subject = subject;
        this.textHTML = textHTML;
    }
    public void sendMail(){
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromAddress, password);
            }

        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.fromAddress));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(this.toAddress));
            message.setSubject(this.subject);
            Multipart multipart = new MimeMultipart();
            MimeBodyPart attachmentPart = new MimeBodyPart();
            MimeBodyPart textPart = new MimeBodyPart();
            try {
                File f = this.attachment;
                attachmentPart.attachFile(f);
                textPart.setContent( this.textHTML, "text/html; charset=utf-8" );
                multipart.addBodyPart(textPart);
                multipart.addBodyPart(attachmentPart);

            } catch (IOException e) {
                e.printStackTrace();
            }
            message.setContent(multipart);
            message.saveChanges();
            System.out.println("Sending the EMAIL to " + this.toAddress);
            Transport.send(message);
            System.out.println("Sent message successfully...");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}


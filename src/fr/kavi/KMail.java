package fr.kavi;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import fr.kavi.KLog.LogSeverity;
import fr.kavi.KLog.LogType;

class KMail {

	
	/*
	 * 	Information about EMail.class
	 * 	Created by Kalvin VILLA
	 * 	Created 05/07/2021
	 * 
	 * 	Description :
	 *	This class is use for send mail on the application
	 *
	 *	Last edit : 09/07/2021
	 */

	
	private String from;
	private String host;
	private String port;
	private String ssl_actived;
	
	private String auth_actived;
	private String auth_user;
	private String auth_password;
	
	private Properties properties;
	private Session session;
	
	public KMail() {
		from = SMPI.getConfig().getString("mail_user");
		host = SMPI.getConfig().getString("mail_host");
		port = SMPI.getConfig().getString("mail_port");
		ssl_actived = SMPI.getConfig().getString("mail_ssl");
		auth_actived = SMPI.getConfig().getString("mail_auth");
		auth_user = SMPI.getConfig().getString("mail_user");
		auth_password = SMPI.getConfig().getString("mail_password");
		
        properties = System.getProperties();
        
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", ssl_actived);
        properties.put("mail.smtp.auth", auth_actived);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        
        session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(auth_user, auth_password);

            }

        });
       
        SMPI.getLog().send(LogSeverity.INFO, LogType.MAIL, "Mail is configured", false);
	}
	
	public void sendMail(String rcv, String subject ,String content) {
		
	    try {
            InternetAddress internetAddress = new InternetAddress(rcv);
            internetAddress.validate();
	    } catch (AddressException e) {
	    	SMPI.getLog().send(LogSeverity.ERROR, LogType.MAIL, "Exception Occurred for: " + rcv, false);
	    	return;
        }
		
		try {
			
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(rcv));
            message.setSubject(subject);
            
            MimeMultipart multipart = new MimeMultipart("related");
            
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(content, "text/html");
            multipart.addBodyPart(messageBodyPart);
            
            message.setContent(content, "text/html");

            SMPI.getLog().send(LogSeverity.INFO, LogType.MAIL, "Sending mail to " + rcv + " ...", false);
            Transport.send(message);
            SMPI.getLog().send(LogSeverity.INFO, LogType.MAIL, "Sent message successfully", false);
        } catch (MessagingException mex) {
        	SMPI.getLog().send(LogSeverity.ERROR, LogType.MAIL, "Sent message error : " + mex.toString(), false);
        }
	}

	
}


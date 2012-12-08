package com.attack.android;

import javax.activation.DataHandler;   
import javax.activation.DataSource;   
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;   
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;   
import javax.mail.internet.MimeMultipart;

import android.os.Environment;

import java.io.ByteArrayInputStream;   
import java.io.File;
import java.io.IOException;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.security.Security;   
import java.util.Properties;   

public class GmailSender extends javax.mail.Authenticator {   
    private String mailhost = "smtp.gmail.com";   
    private String user;   
    private String password;   
    private Session session;
    private Multipart _multipart = new MimeMultipart(); 

    static {   
        Security.addProvider(new JSSEProvider());   
    }  

    public GmailSender(String address, String password) {   
        this.user = address;   
        this.password = password;   

        Properties props = new Properties();   
        props.setProperty("mail.transport.protocol", "smtp");   
        props.setProperty("mail.host", mailhost);   
        props.put("mail.smtp.auth", "true");   
        props.put("mail.smtp.port", "465");   
        props.put("mail.smtp.socketFactory.port", "465");   
        props.put("mail.smtp.socketFactory.class",   
                "javax.net.ssl.SSLSocketFactory");   
        props.put("mail.smtp.socketFactory.fallback", "false");   
        props.setProperty("mail.smtp.quitwait", "false");   

        session = Session.getDefaultInstance(props, this);   
    }   

    protected PasswordAuthentication getPasswordAuthentication() {   
        return new PasswordAuthentication(user, password);   
    }   

    public synchronized void sendMail(String loc, String sender, String recipients) throws Exception {   
        try{
        MimeMessage message = new MimeMessage(session);   
        loc = loc.replace(" ", "");
        String mes = "This person needs your help. They are at: "+"http://maps.google.com/?q="+loc;
        DataHandler handler = new DataHandler(new ByteArrayDataSource(mes.getBytes(), "text/plain"));   
        message.setSender(new InternetAddress(sender));   
        message.setSubject("Help alert!");   
        message.setDataHandler(handler); 
        addAttachment(mes);
        message.setContent(_multipart);
        if (recipients.indexOf(',') > 0)   
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));   
        else  
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));   
        Transport.send(message);   
        }catch(Exception e){

        }
    }  
    
    public void addAttachment(String message) throws Exception { 
        BodyPart messageBodyPart = new MimeBodyPart(); 
        DataSource source = new FileDataSource(Environment.getExternalStorageDirectory() +
                "" + File.separatorChar + "videocapture_example0.mp4"); 
        messageBodyPart.setDataHandler(new DataHandler(source)); 
        messageBodyPart.setFileName("video.mp4"); 
        _multipart.addBodyPart(messageBodyPart);

        BodyPart messageBodyPart2 = new MimeBodyPart(); 
        messageBodyPart2.setText(message); 

        _multipart.addBodyPart(messageBodyPart2); 
    } 
    public class ByteArrayDataSource implements DataSource {   
        private byte[] data;   
        private String type;   

        public ByteArrayDataSource(byte[] data, String type) {   
            super();   
            this.data = data;   
            this.type = type;   
        }   

        public ByteArrayDataSource(byte[] data) {   
            super();   
            this.data = data;   
        }   

        public void setType(String type) {   
            this.type = type;   
        }   

        public String getContentType() {   
            if (type == null)   
                return "application/octet-stream";   
            else  
                return type;   
        }   

        public InputStream getInputStream() throws IOException {   
            return new ByteArrayInputStream(data);   
        }   

        public String getName() {   
            return "ByteArrayDataSource";   
        }   

        public OutputStream getOutputStream() throws IOException {   
            throw new IOException("Not Supported");   
        }   
    }   
}  

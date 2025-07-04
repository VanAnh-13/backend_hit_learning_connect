package com.example.projectbase.util;

import com.example.projectbase.domain.dto.common.DataMailDto;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import static jakarta.mail.Message.RecipientType.TO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Properties;

@Service
public class GmailUtil {
    @Autowired
    private Gmail gmail;

    @Autowired
    private TemplateEngine templateEngine; // Thymeleaf

    // Chuyển MimeMessage sang Gmail Message
    private Message createMessage(MimeMessage email) throws IOException, MessagingException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);
        String encoded = Base64.getUrlEncoder().encodeToString(baos.toByteArray());
        Message message = new Message();
        message.setRaw(encoded);
        return message;
    }

    // Tạo MimeMessage từ HTML
    private MimeMessage createEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties(), null));
        mimeMessage.setFrom(new InternetAddress("me"));
        mimeMessage.addRecipient(TO, new InternetAddress(to));
        mimeMessage.setSubject(subject, "UTF-8");
        mimeMessage.setContent(htmlBody, "text/html; charset=UTF-8");
        return mimeMessage;
    }

    // Gửi email sử dụng template
    public void sendTemplateEmail(DataMailDto mail, String template) throws Exception {
        // Render HTML
        Context ctx = new Context();
        ctx.setVariables(mail.getProperties());
        String html = templateEngine.process(template, ctx);

        // Tạo và gửi
        MimeMessage mime = createEmail(mail.getTo(), mail.getSubject(), html);
        Message message = createMessage(mime);
        gmail.users().messages().send("me", message).execute();
    }
}
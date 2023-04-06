package cf.vsing.community.util;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailClientUtil {
    private static final Logger logger= LoggerFactory.getLogger(MailClientUtil.class);
    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;
    public void sendMail(String to,String subject,String text){
        try {
            MimeMessage mimeMessage=mailSender.createMimeMessage();
            mimeMessage.setFrom(new InternetAddress(sender));
            mimeMessage.setRecipients(Message.RecipientType.TO,to);
            mimeMessage.setSubject(subject);
            mimeMessage.setContent(text,"text/html;charset=UTF-8");
            mailSender.send(mimeMessage);
        }catch (MessagingException e){
            logger.error("邮件发送出错: "+e.getMessage());
        }
    }
}

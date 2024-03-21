package bigsanghyuk.four_uni.config.mail.service;

import bigsanghyuk.four_uni.config.RedisUtil;
import bigsanghyuk.four_uni.config.mail.domain.SendMailInfo;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${mail.username}")
    private String username;

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    private MimeMessage createMessage(String code, String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("FourUni 인증 번호입니다.");
        message.setText("이메일 인증코드: " + code);
        message.setFrom(username);
        return message;
    }

    public void sendMail(String code, String email) throws Exception{
        try {
            MimeMessage mimeMessage = createMessage(code, email);
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    public Boolean sendCertificationMail(SendMailInfo sendMailInfo) throws Exception {
        String email = sendMailInfo.getEmail();
        try {
            String code = UUID.randomUUID().toString().substring(0, 6);
            sendMail(code, email);
            redisUtil.setDataWithExpiration(email, code);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public Boolean validate(String email, String code) {
        return redisUtil.getData(email).equals(code);
    }
}

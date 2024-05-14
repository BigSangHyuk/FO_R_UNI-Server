package bigsanghyuk.four_uni.config.mail.service;

import bigsanghyuk.four_uni.config.RedisUtil;
import bigsanghyuk.four_uni.config.mail.domain.InquireMailInfo;
import bigsanghyuk.four_uni.config.mail.domain.SendMailInfo;
import bigsanghyuk.four_uni.exception.mail.SendMailFailureException;
import bigsanghyuk.four_uni.exception.mail.VerificationCodeMismatchException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
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
    @Value("${mail.admin}")
    private String admin;

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    private MimeMessage createMessage(String code, String email) throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("FourUni 인증 번호입니다.");
        message.setText("이메일 인증코드: " + code);
        message.setFrom(username);
        return message;
    }

    private MimeMessage createTempPwMessage(String email, String tempPw) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("FourUni 임시 비밀번호입니다.");
        message.setText("안녕하세요.\n" +
                "임시 비밀번호 발송 메일 입니다.\n" +
                "[" + email + "]" + " 님의 임시 비밀번호는\n\n" +
                tempPw + "\n\n" +
                "입니다.");
        message.setFrom(username);
        return message;
    }

    private MimeMessage createInquiry(Long userId, String inquiry) throws MessagingException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, admin);
        message.setSubject("FO_R UNI[문의] " + user.getEmail());
        message.setText(user.getDepartmentType().getValue() +
                "의 " + user.getNickName() +
                " (" + user.getEmail() +
                ") 님께서 문의를 보냈습니다.\n\n" +
                "문의내용\n\n" + inquiry);
        message.setFrom(username);
        return message;
    }

    public void sendMail(Long userId, InquireMailInfo inquireMailInfo) throws MessagingException {
        try {
            MimeMessage mimeMessage = createInquiry(userId, inquireMailInfo.getInquiry());
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new SendMailFailureException();
        }
    }

    public void sendMail(String code, String email) throws Exception {
        try {
            MimeMessage mimeMessage = createMessage(code, email);
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new SendMailFailureException();
        }
    }

    public Boolean sendCertificationMail(SendMailInfo sendMailInfo) throws Exception {
        String email = sendMailInfo.getEmail();
        try {
            String code = UUID.randomUUID().toString().substring(0, 6);
            sendMail(code, email);
            redisUtil.setDataWithExpiration(email, code);
            return true;
        } catch (MailException e) {
            throw new SendMailFailureException();
        }
    }

    public String sendTempPwMail(SendMailInfo sendMailInfo) {
        String email = sendMailInfo.getEmail();
        String tempPw = getTempPw();
        try {
            MimeMessage mimeMessage = createTempPwMessage(email, tempPw);
            javaMailSender.send(mimeMessage);
            return tempPw;
        } catch (Exception e) {
            throw new SendMailFailureException();
        }
    }

    public Boolean validate(String email, String code) {
        if (redisUtil.getData(email).equals(code)) {
            return true;
        } else {
            throw new VerificationCodeMismatchException();
        }
    }

    public String getTempPw() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        char[] specialSet = new char[]{'!', '@', '#', '$', '%', '^', '+', '*', '=', '-'};
        StringBuilder str = new StringBuilder();
        int idx;
        for (int i = 0; i < 9; i++) {
            idx = (int) (charSet.length * Math.random());
            str.append(charSet[idx]);
        }
        str.append(specialSet[(int) (specialSet.length * Math.random())]);
        return str.toString();
    }
}

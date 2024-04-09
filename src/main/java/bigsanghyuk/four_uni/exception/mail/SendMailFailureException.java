package bigsanghyuk.four_uni.exception.mail;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;
import org.springframework.mail.MailException;

@Getter
public class SendMailFailureException extends MailException {

    private final StatusEnum status;

    private static final String message = "메일 전송에 실패했습니다.";

    public SendMailFailureException() {
        super(message);
        this.status = StatusEnum.SEND_MAIL_FAILED;
    }

}

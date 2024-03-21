package bigsanghyuk.four_uni.config.mail.dto;

import bigsanghyuk.four_uni.config.mail.domain.SendMailInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SendMailRequest {

    private String email;

    public SendMailInfo toDomain() {
        return new SendMailInfo(email);
    }
}

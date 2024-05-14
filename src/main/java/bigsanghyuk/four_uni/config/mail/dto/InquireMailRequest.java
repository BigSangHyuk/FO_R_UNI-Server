package bigsanghyuk.four_uni.config.mail.dto;

import bigsanghyuk.four_uni.config.mail.domain.InquireMailInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquireMailRequest {

    private String inquiry;

    public InquireMailInfo toDomain() {
        return new InquireMailInfo(inquiry);
    }
}

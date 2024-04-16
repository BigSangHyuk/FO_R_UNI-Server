package bigsanghyuk.four_uni.config.fcm.dto;

import bigsanghyuk.four_uni.config.fcm.domain.Message;
import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class FcmMessageDto {

    private boolean validateOnly;
    private Message message;
}

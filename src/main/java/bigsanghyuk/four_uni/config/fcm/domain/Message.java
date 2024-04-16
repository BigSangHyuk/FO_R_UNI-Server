package bigsanghyuk.four_uni.config.fcm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class Message {

    private Notification notification;
    private String token;
}

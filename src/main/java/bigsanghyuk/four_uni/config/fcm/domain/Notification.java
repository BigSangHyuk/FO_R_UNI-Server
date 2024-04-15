package bigsanghyuk.four_uni.config.fcm.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter
@AllArgsConstructor
public class Notification {

    private String title;
    private String body;
    private String image;
}

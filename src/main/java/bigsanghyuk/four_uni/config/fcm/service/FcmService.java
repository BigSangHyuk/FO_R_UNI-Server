package bigsanghyuk.four_uni.config.fcm.service;

import bigsanghyuk.four_uni.config.fcm.domain.Message;
import bigsanghyuk.four_uni.config.fcm.domain.Notification;
import bigsanghyuk.four_uni.config.fcm.dto.FcmMessageDto;
import bigsanghyuk.four_uni.config.fcm.dto.FcmSendDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class FcmService {

    public boolean sendMessageTo(FcmSendDto fcmSendDto) throws IOException {
        String message = makeMessage(fcmSendDto);
        RestTemplate restTemplate = new RestTemplate();

        /*
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + getAccessToken());

        HttpEntity<?> entity = new HttpEntity<>(message, headers);
        */

        HttpEntity<?> entity = new HttpEntity<>(message);

        String API_URL = "<https://fcm.googleapis.com/v1/projects/fouruni-2024/messages:send>";
        ResponseEntity<?> response = restTemplate.exchange(API_URL, HttpMethod.POST, entity, String.class);

        log.info("response.getStatusCode()={}", response.getStatusCode());
        return response.getStatusCode() == HttpStatus.OK;
    }

    private String makeMessage(FcmSendDto fcmSendDto) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Notification notification = Notification.builder()
                .title(fcmSendDto.getTitle())
                .body(fcmSendDto.getBody())
                .image(null)
                .build();
        Message message = Message.builder()
                .token(fcmSendDto.getToken())
                .notification(notification)
                .build();
        FcmMessageDto messageDto = FcmMessageDto.builder()
                .message(message)
                .validateOnly(false).build();
        return objectMapper.writeValueAsString(messageDto);
    }

    /*
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/fouruni-2024-firebase-adminsdk-65ucu-8c302a7bed.json";
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("<https://www.googleapis.com/auth/cloud-platform>"));
        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
     */
}

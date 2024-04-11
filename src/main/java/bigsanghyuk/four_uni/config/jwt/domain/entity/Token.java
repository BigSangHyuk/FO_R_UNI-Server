package bigsanghyuk.four_uni.config.jwt.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter @RedisHash("refreshToken") @Builder
@AllArgsConstructor @NoArgsConstructor
public class Token {

    @Id @JsonIgnore
    private Long id;

    private String refreshToken;

    @Setter
    @TimeToLive(unit = TimeUnit.MINUTES)
    private Integer expiration;
}

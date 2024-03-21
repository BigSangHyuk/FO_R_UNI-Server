package bigsanghyuk.four_uni.config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public String getData(String email) {
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(email);
    }

    public void setData(String key, String value) {
        ValueOperations<String,String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataWithExpiration(String email, String code) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(60 * 5L);  // 인증코드 유효기간 5분
        valueOperations.set(email, code, expireDuration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}

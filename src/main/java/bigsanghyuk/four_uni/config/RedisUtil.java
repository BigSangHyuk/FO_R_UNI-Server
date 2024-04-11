package bigsanghyuk.four_uni.config;

import bigsanghyuk.four_uni.exception.mail.VerificationCodeExpiredException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;
    private final StringRedisTemplate blackListRedisTemplate;

    public String getData(String email) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String code = valueOperations.get(email);
        if (code == null) {
            throw new VerificationCodeExpiredException();
        }
        return code;
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

    public void setBlackList(String key, Object o, Long milliSeconds) throws JsonProcessingException {
        ValueOperations<String, String> opsForValue = blackListRedisTemplate.opsForValue();
        opsForValue.set(key, new ObjectMapper().writeValueAsString(o), milliSeconds, TimeUnit.MILLISECONDS);
    }

    public Boolean hasKeyBlackList(String key) {
        return blackListRedisTemplate.hasKey(key);
    }

    public Object getBlackList(String key) throws JsonProcessingException {
        ValueOperations<String, String> opsForValue = blackListRedisTemplate.opsForValue();
        String value = opsForValue.get(key);
        return value != null ? new ObjectMapper().readValue(value, Object.class) : null;
    }

    public Boolean deleteBlackList(String key) {
        return blackListRedisTemplate.delete(key);
    }
}

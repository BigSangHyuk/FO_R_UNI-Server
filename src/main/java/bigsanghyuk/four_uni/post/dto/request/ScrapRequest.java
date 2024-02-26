package bigsanghyuk.four_uni.post.dto.request;

import bigsanghyuk.four_uni.post.domain.ScrapInfo;
import bigsanghyuk.four_uni.user.domain.EditUserInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ScrapRequest {

    @NotNull
    private Long userId;
    @NotNull
    private Long postId;

    public ScrapInfo toDomain() {
        return new ScrapInfo(userId, postId);
    }
}

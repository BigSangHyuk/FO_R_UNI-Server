package bigsanghyuk.four_uni.post.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class GetPostRequest {

    @NotNull
    private String title;
    @NotNull
    private String content;

    private String imageUrl; // 이미지로 된 게시글일 때 : 이미지 URL

    @NotNull
    private int views; // 조회수
}

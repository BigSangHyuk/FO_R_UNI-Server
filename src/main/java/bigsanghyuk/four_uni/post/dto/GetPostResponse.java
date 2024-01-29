package bigsanghyuk.four_uni.post.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetPostResponse {

    private Long id;

    private String title;

    private String content;

    private String imageUrl;

    private int views;

}

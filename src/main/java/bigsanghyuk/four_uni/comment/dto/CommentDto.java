package bigsanghyuk.four_uni.comment.dto;

import bigsanghyuk.four_uni.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class CommentDto {

    private Long commentId;
    private Long userId;
    private UserDto user;
    private Integer commentLike;
    private String content;
    private List<CommentDto> children;
}

package bigsanghyuk.four_uni.comment.dto;

import bigsanghyuk.four_uni.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private Long commentId;
    private Long userId;
    private UserDto user;
    private Integer commentLike;
    private String content;
    private Boolean isDeleted;
    private List<CommentDto> children;
}

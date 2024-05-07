package bigsanghyuk.four_uni.comment.dto;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private Long id;
    private UserDto user;
    private Integer commentLike;
    private String content;
    private Boolean isDeleted;
    private Long parentId;
    private List<CommentDto> children;
    private LocalDateTime createdAt;
//    private String createdAt;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.user = comment.isDeleted() ? null : UserDto.of(comment.getUser());
        this.commentLike = comment.isDeleted() ? null : comment.getCommentLike();
        this.content = comment.isDeleted() ? null : comment.getContent();
        this.isDeleted = comment.isDeleted() ? true : null;
        this.parentId = comment.getParent() != null ? comment.getParent().getId() : null;
        this.children = new ArrayList<>();
        this.createdAt = comment.getCreatedAt();
//        this.createdAt = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 H시 m분"));
    }
}

package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void register(RegisterCommentInfo registerCommentInfo) {

        commentRepository.save(
                new Comment(
                        registerCommentInfo.getUserId(),
                        registerCommentInfo.getContent()
                )
        );
    }

    public void edit(Long commentId, @Valid EditCommentInfo editCommentInfo) {
        commentRepository.findById(editCommentInfo.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 아닙니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        comment.CommentEdit(editCommentInfo);
        commentRepository.save(comment);
    }

    public void remove(Long postId, Long commentId) {
        commentRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        if (comment.CommentRemove(postId, commentId)) {
            commentRepository.deleteCommentByPostIdAndId(postId, commentId);
        } else {
            throw new IllegalArgumentException("삭제를 실패했습니다.");
        }
    }
}

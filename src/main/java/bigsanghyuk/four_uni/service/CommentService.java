package bigsanghyuk.four_uni.service;

import bigsanghyuk.four_uni.domain.CommentEditInfo;
import bigsanghyuk.four_uni.domain.CommentRegisterInfo;
import bigsanghyuk.four_uni.domain.entity.Comment;
import bigsanghyuk.four_uni.repository.CommentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public void register(CommentRegisterInfo commentRegisterInfo) {

        commentRepository.save(
                new Comment(
                        commentRegisterInfo.getUserId(),
                        commentRegisterInfo.getContent()
                )
        );
    }

    public void edit(Long commentId, @Valid CommentEditInfo commentEditInfo) {
        commentRepository.findById(commentEditInfo.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 아닙니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        comment.CommentEdit(commentEditInfo);
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

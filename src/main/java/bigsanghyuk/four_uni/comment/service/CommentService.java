package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public void register(RegisterCommentInfo registerCommentInfo) {
        Comment comment = new Comment(
                registerCommentInfo.getUserId(),
                registerCommentInfo.getPostId(),
                registerCommentInfo.getParentCommentId(),
                registerCommentInfo.getCommentLike(),
                registerCommentInfo.getContent()
        );

        commentRepository.save(comment);
    }

    public Comment edit(Long commentId, @Valid EditCommentInfo editCommentInfo) {
        commentRepository.findByUserId(editCommentInfo.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 아닙니다."));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        comment.CommentEdit(editCommentInfo);
        commentRepository.save(comment);
        return comment;
    }

    public void remove(Long postId, Long commentId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        commentRepository.deleteCommentByPostIdAndId(postId, commentId);
    }

    public List<Comment> getAllComments(Long postId) {
        commentRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        List<Comment> comments;
        List<Comment> childComments = new ArrayList<>();
        comments = commentRepository.findParentComments(postId);

        for (Comment parentComment : comments) {
            childComments.addAll(commentRepository.findChildComments(postId, parentComment.getId()));
        }

        comments.addAll(childComments);
        return comments;
    }
}

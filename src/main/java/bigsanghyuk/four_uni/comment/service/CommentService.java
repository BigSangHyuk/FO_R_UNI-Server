package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentEditOtherUserException;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeCommentRepository likeCommentRepository;

    public void register(Long postId, RegisterCommentInfo registerCommentInfo) {
        postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment(
                registerCommentInfo.getUserId(),
                postId,
                registerCommentInfo.getParentCommentId(),
                registerCommentInfo.getCommentLike(),
                registerCommentInfo.getContent(),
                false
        );
        commentRepository.save(comment);
    }

    public Comment edit(Long postId, Long commentId, @Valid EditCommentInfo editCommentInfo) {
        postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        commentRepository.findByUserIdOrderByIdDesc(editCommentInfo.getUserId())
                .orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!editCommentInfo.getUserId().equals(comment.getUserId())) {
            throw new CommentEditOtherUserException();
        }

        comment.edit(editCommentInfo);
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void remove(Long postId, Long commentId) {
        postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        commentRepository.deleteCommentByPostIdAndId(postId, commentId);
        likeCommentRepository.deleteLikeCommentByCommentId(commentId);
    }

    public List<Comment> getAllComments(Long postId) {
        postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

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

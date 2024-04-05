package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.comment.LikeCommentNotFoundException;
import bigsanghyuk.four_uni.exception.likecomment.AlreadyLikeException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeCommentService {

    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final UserRepository userRepository;

    @Transactional
    public void likeComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        checkDeleted(comment);
        checkIsLiked(user, comment);
        increaseLike(user, comment);
    }

    @Transactional
    public void unLikeComment(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        checkDeleted(comment);
        checkIsUnliked(user, comment);
        decreaseLike(user, comment);
    }

    @Transactional
    protected void increaseLike(User user, Comment comment) {
        checkDeleted(comment);
        comment.increaseLike();
        Comment likedComment = commentRepository.save(comment);
        likeCommentRepository.save(new LikeComment(user, likedComment));
    }

    @Transactional
    protected void decreaseLike(User user, Comment comment) {
        checkDeleted(comment);
        comment.decreaseLike();
        Comment unlikedComment = commentRepository.save(comment);
        likeCommentRepository.deleteLikeCommentByUserAndComment(user, unlikedComment);
    }

    private void checkIsLiked(User user, Comment comment) {
        likeCommentRepository.findByUserAndComment(user, comment)
                .ifPresent(likeComment -> {
                    throw new AlreadyLikeException();
                });
    }

    private void checkIsUnliked(User user, Comment comment) {
        likeCommentRepository.findByUserAndComment(user, comment)
                .orElseThrow(LikeCommentNotFoundException::new);
    }

    private void checkDeleted(Comment comment) {
        if (comment.isDeleted()) {
            throw new CommentNotFoundException();
        }
    }
}

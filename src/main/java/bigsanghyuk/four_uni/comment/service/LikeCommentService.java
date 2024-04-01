package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.LikeCommentInfo;
import bigsanghyuk.four_uni.comment.domain.UnLikeCommentInfo;
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
    public void likeComment(Long userId, LikeCommentInfo likeCommentInfo) {

        Long commentId = likeCommentInfo.getCommentId();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        likeCommentRepository.findByUserAndComment(user, comment)
                .ifPresent(likeComment -> {
                            throw new AlreadyLikeException();
                    }
                );

        commentRepository.increaseLikesByCommentId(commentId);
        likeCommentRepository.save(new LikeComment(user, comment));
    }

    @Transactional
    public void unLikeComment(Long userId, UnLikeCommentInfo unLikeCommentInfo) {
        Long commentId = unLikeCommentInfo.getCommentId();

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        likeCommentRepository.findByUserAndComment(user, comment).orElseThrow(LikeCommentNotFoundException::new);

        commentRepository.decreaseLikesByCommentId(commentId);
        likeCommentRepository.deleteLikeCommentByUserAndComment(user, comment);
    }
}

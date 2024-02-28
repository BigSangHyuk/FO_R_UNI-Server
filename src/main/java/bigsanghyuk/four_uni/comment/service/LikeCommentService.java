package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.LikeCommentInfo;
import bigsanghyuk.four_uni.comment.domain.UnLikeCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.comment.LikeCommentNotFoundException;
import bigsanghyuk.four_uni.exception.likecomment.AlreadyLikeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeCommentService {

    private final CommentRepository commentRepository;
    private final LikeCommentRepository likeCommentRepository;

    @Transactional
    public void likeComment(LikeCommentInfo likeCommentInfo) {

        Long userId = likeCommentInfo.getUserId();
        Long commentId = likeCommentInfo.getCommentId();

        commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        likeCommentRepository.findByUserIdAndCommentId(userId, commentId)
                .ifPresent(comment -> {
                            throw new AlreadyLikeException();
                    }
                );
        commentRepository.increaseLikesByCommentId(commentId);
        likeCommentRepository.save(new LikeComment(userId, commentId));
    }

    @Transactional
    public void unLikeComment(UnLikeCommentInfo unLikeCommentInfo) {
        Long userId = unLikeCommentInfo.getUserId();
        Long commentId = unLikeCommentInfo.getCommentId();

        commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        likeCommentRepository.findByUserIdAndCommentId(userId, commentId).orElseThrow(LikeCommentNotFoundException::new);

        commentRepository.decreaseLikesByCommentId(commentId);
        likeCommentRepository.deleteLikeCommentByUserIdAndCommentId(userId, commentId);
    }

}

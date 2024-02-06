package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.LikeCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
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
    public void likeComment(LikeCommentInfo domain) {

        Long userId = domain.getUserId();
        Long commentId = domain.getCommentId();

        if(commentRepository.findById(commentId).isEmpty()) {
            throw new CommentNotFoundException();
        } else if(likeCommentRepository.findByUserIdAndCommentId(userId, commentId).isPresent()) {
            throw new AlreadyLikeException();
        } else {
            commentRepository.incrementLikesByCommentId(commentId);
            likeCommentRepository.save(new LikeComment(userId, commentId));
        }
    }

}

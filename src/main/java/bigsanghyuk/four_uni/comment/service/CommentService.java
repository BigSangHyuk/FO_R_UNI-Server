package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.DeleteCommentInfo;
import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentEditOtherUserException;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.comment.CommentRemoveOtherUserException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final UserRepository userRepository;

    public void register(RegisterCommentInfo registerCommentInfo) {
        Long userId = registerCommentInfo.getUserId();
        Long postId = registerCommentInfo.getPostId();

        Comment parent = null;
        if (registerCommentInfo.getParentCommentId() != null) {
            parent = commentRepository.findById(registerCommentInfo.getParentCommentId()).orElseThrow(CommentNotFoundException::new);
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .parent(parent)
                .content(registerCommentInfo.getContent())
                .build();

        commentRepository.save(comment);
    }

    public Comment edit(Long commentId, @Valid EditCommentInfo editCommentInfo) {
        postRepository.findById(editCommentInfo.getPostId())
                .orElseThrow(PostNotFoundException::new);

        commentRepository.findByUserIdOrderByIdDesc(editCommentInfo.getUserId())
                .orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!editCommentInfo.getUserId().equals(comment.getUser().getId())) {
            throw new CommentEditOtherUserException();
        }

        comment.edit(editCommentInfo);
        commentRepository.save(comment);
        return comment;
    }

    @Transactional
    public void remove(Long commentId, @Valid DeleteCommentInfo deleteCommentInfo) {
        postRepository.findById(deleteCommentInfo.getPostId())
                .orElseThrow(PostNotFoundException::new);

        commentRepository.findByUserIdOrderByIdDesc(deleteCommentInfo.getUserId())
                .orElseThrow(UserNotFoundException::new);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);

        if (!deleteCommentInfo.getUserId().equals(comment.getUser().getId())) {
            throw new CommentRemoveOtherUserException();
        }

        commentRepository.deleteById(comment.getId());
        likeCommentRepository.deleteLikeCommentByComment(comment);
    }

    public List<Comment> getAllComments(Long postId) {

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        List<Comment> comments;
        List<Comment> childComments = new ArrayList<>();
        comments = commentRepository.findParentComments(post);

        for (Comment parent : comments) {
            childComments.addAll(commentRepository.findChildComments(post, parent));
        }

        comments.addAll(childComments);
        return comments;
    }

    public List<Comment> getLikedComment(Long userId) {

        LinkedList<Comment> comments = new LinkedList<>();
        List<LikeComment> likedComments = likeCommentRepository.findByUserIdOrderByIdDesc(userId);
        for (LikeComment likeComment : likedComments) {
            comments.add(likeComment.getComment());
        }

        return comments;
    }
}

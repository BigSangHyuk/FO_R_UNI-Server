package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.domain.entity.CommentRequired;
import bigsanghyuk.four_uni.comment.dto.CommentDto;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.comment.CommentRemoveOtherUserException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeCommentRepository likeCommentRepository;
    private final UserRepository userRepository;

    public void write(Long userId, RegisterCommentInfo registerCommentInfo) {
        Comment parent = null;
        Long postId = registerCommentInfo.getPostId();
        String content = registerCommentInfo.getContent();
        if (registerCommentInfo.getParentCommentId() != null) {
            parent = commentRepository.findById(registerCommentInfo.getParentCommentId()).orElseThrow(CommentNotFoundException::new);
        }
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.builder()
                .user(user)
                .postId(postId)
                .parent(parent)
                .content(content)
                .build();
        commentRepository.save(comment);
    }

    public void edit(Long userId, Long commentId, @Valid EditCommentInfo editCommentInfo) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        validateRequest(user, comment);

        comment.edit(editCommentInfo);
        commentRepository.save(comment);
    }

    @Transactional
    public void remove(Long userId, Long commentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);

        validateRequest(user, comment);

        removeComment(comment);
    }

    public List<CommentDto> getAllComments(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Long> likedCommentIds = likeCommentRepository.findCommentIds(user);

        List<Comment> parents = commentRepository.findParents(postId);
        List<Comment> children = new ArrayList<>();
        for (Comment parent : parents) {
            children.addAll(commentRepository.findByParent(parent));
        }
        parents.addAll(children);
        return convertToCommentDto(likedCommentIds, parents);
    }

    private List<CommentDto> convertToCommentDto(List<Long> likedCommentIds, List<Comment> parents) {
        List<CommentDto> getCommentData = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();
        HashSet<Long> set = new HashSet<>(likedCommentIds);
        for (Comment parent : parents) {
            CommentDto dto = new CommentDto(parent, set);
            map.put(dto.getId(), dto);
            if (dto.getParentId() != null) {
                map.get(dto.getParentId()).getChildren().add(dto);
            } else {
                getCommentData.add(dto);
            }
        }
        return getCommentData;
    }

    public List<CommentRequired> getLikedComment(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<CommentRequired> comments = new ArrayList<>();
        List<Long> commentIds = likeCommentRepository.findCommentIds(user);
        for (Long commentId : commentIds) {
            CommentRequired commentRequired = commentRepository.findCommentRequired(commentId);
            comments.add(commentRequired);
        }
        return comments;
    }

    private void validateRequest(User user, Comment comment) {
        if (!user.equals(comment.getUser())) {    // 내가 단 댓글이 아니면 예외
            throw new CommentRemoveOtherUserException();
        } else if (comment.isDeleted()) {
            throw new CommentNotFoundException();
        } else {
            return;
        }
    }

    @Transactional
    protected void removeComment(Comment comment) {
        likeCommentRepository.deleteLikeCommentByComment(comment);
        commentRepository.deleteById(comment.getId());  // soft delete 사용
    }
}

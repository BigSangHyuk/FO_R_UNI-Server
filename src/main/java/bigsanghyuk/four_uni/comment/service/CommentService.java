package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.DeleteCommentInfo;
import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.domain.entity.CommentProfile;
import bigsanghyuk.four_uni.comment.domain.entity.CommentRequired;
import bigsanghyuk.four_uni.comment.dto.CommentDto;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentEditOtherUserException;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.comment.CommentRemoveOtherUserException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.domain.entity.UserRequired;
import bigsanghyuk.four_uni.user.dto.UserDto;
import bigsanghyuk.four_uni.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    public void register(RegisterCommentInfo registerCommentInfo) {
        Long userId = registerCommentInfo.getUserId();
        Long postId = registerCommentInfo.getPostId();

        Comment parent = null;
        if (registerCommentInfo.getParentCommentId() != null) {
            parent = commentRepository.findById(registerCommentInfo.getParentCommentId()).orElseThrow(CommentNotFoundException::new);
        }

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.builder()
                .user(user)
                .postId(postId)
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

        likeCommentRepository.deleteLikeCommentByComment(comment);
        commentRepository.deleteById(comment.getId());  // soft delete 사용
    }

    public List<CommentDto> getAllComments(Long postId) {
        postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<CommentProfile> parentsProfile = commentRepository.findParentComments(postId);
        List<CommentDto> comments = new ArrayList<>();
        for (CommentProfile parent : parentsProfile) {
            UserDto parentDto = makeUserDto(parent, parent.isDeleted());
            List<CommentProfile> childrenProfile = commentRepository.findChildComments(postId, parent.getCommentId());
            List<CommentDto> children = new ArrayList<>();
            for (CommentProfile child : childrenProfile) {
                UserDto childDto = makeUserDto(child, child.isDeleted());
                children.add(makeCommentDto(child, childDto, null));
            }
            comments.add(makeCommentDto(parent, parentDto, children));
        }
        return comments;
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

    private UserDto makeUserDto(CommentProfile profile, boolean isDeleted) {
        if (isDeleted) {
            return null;
        } else {
            UserRequired userRequired = userRepository.getUserRequired(profile.getUserId());
            return UserDto.builder()
                    .userId(userRequired.getUserId())
                    .email(userRequired.getEmail())
                    .name(userRequired.getName())
                    .nickName(userRequired.getNickName())
                    .image(userRequired.getImage())
                    .build();
        }
    }

    private CommentDto makeCommentDto(CommentProfile profile, UserDto userDto, List<CommentDto> children) {
        boolean isDeleted = (userDto == null);      // userDto 가 null 인 경우는 makeUserDto() 에서 이미 isDeleted == true 임
        return CommentDto.builder()
                .commentId(profile.getCommentId())
                .userId(isDeleted ? null : profile.getUserId())
                .user(userDto)
                .commentLike(isDeleted ? null : profile.getCommentLike())
                .content(isDeleted ? null : profile.getContent())
                .isDeleted(isDeleted ? true : null)
                .children(children)
                .build();
    }
}

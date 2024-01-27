package bigsanghyuk.four_uni.user.service;

import bigsanghyuk.four_uni.user.domain.UpdateUserInfo;
import bigsanghyuk.four_uni.user.domain.RegisterUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(RegisterUserInfo registerUserInfo) {

//        Optional<User> result = userRepository.findByEmail(userRegisterInfo.getEmail());
//        if (result.isPresent()) {
//            throw new IllegalStateException("이미 존재하는 이메일입니다.");
//        }

        userRepository.findByEmail(registerUserInfo.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
                });

        userRepository.save(
                new User(
                        registerUserInfo.getEmail(),
                        registerUserInfo.getPassword(),
                        registerUserInfo.getName(),
                        registerUserInfo.getDept(),
                        registerUserInfo.getNickName(),
                        registerUserInfo.getImage()
                )
        );
    }

    public void updateUser(UpdateUserInfo updateUserInfo) {

        User user = userRepository.findById(updateUserInfo.getId())
                .orElseThrow(() -> new IllegalStateException("등록되지 않은 유저입니다. 다시 로그인 시도하세요."));

        user.edit(updateUserInfo);
        userRepository.save(user);
    }

    //회원 전체 조회
//    public List<User> findUsers() {
//        return userRepository.findAll();
//    }
//
//    public User findOne(Long userId) {
//        return userRepository.findOne(userId);
//    }

}

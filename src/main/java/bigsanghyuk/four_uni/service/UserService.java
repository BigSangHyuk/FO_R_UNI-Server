package bigsanghyuk.four_uni.service;

import bigsanghyuk.four_uni.domain.UserRegisterInfo;
import bigsanghyuk.four_uni.domain.entity.User;
import bigsanghyuk.four_uni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void register(UserRegisterInfo userRegisterInfo) {

        userRepository.findById(userRegisterInfo.getId())
                .orElseThrow(() -> new IllegalStateException("이미 존재하는 회원입니다."));

        userRepository.save(
                new User(
                        userRegisterInfo.getId(),
                        userRegisterInfo.getEmail(),
                        userRegisterInfo.getPassword(),
                        userRegisterInfo.getName(),
                        userRegisterInfo.getDept(),
                        userRegisterInfo.getNickName(),
                        userRegisterInfo.getImage()
                )
        ); // id 값이 key 값
    }

    private void validateDuplicateUser(User user) {
        // ID가 같을 경우 Exception

    }

    //회원 전체 조회
    public List<User> findMembers() {
        return userRepository.findAll();
    }

//    public User findOne(Long userId) {
//        return userRepository.findOne(userId);
//    }

}

package bigsanghyuk.four_uni.user.repository;

import bigsanghyuk.four_uni.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndNickName(String email, String nickName);
}

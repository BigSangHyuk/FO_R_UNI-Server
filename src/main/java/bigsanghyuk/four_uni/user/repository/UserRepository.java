package bigsanghyuk.four_uni.user.repository;

import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.domain.entity.UserRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndNickName(String email, String nickName);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    void updatePassword(@Param("email") String email, @Param("password") String encodedPw);

    @Query(
            nativeQuery = true,
            value = "SELECT user_id as userId, email, name, nick_name as nickName, image FROM Users " +
                    "WHERE user_id = :userId")
    Optional<UserRequired> getUserRequired(@Param("userId") Long userId);
}

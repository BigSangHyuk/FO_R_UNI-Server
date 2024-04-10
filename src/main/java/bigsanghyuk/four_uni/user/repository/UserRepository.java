package bigsanghyuk.four_uni.user.repository;

import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.domain.entity.UserRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(
            nativeQuery = true,
            value = "SELECT user_id as userId, email, nick_name as nickName, image FROM users " +
                    "WHERE user_id = :userId")
    Optional<UserRequired> getUserRequired(@Param("userId") Long userId);
}

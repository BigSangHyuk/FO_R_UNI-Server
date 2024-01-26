package bigsanghyuk.four_uni.repository;

import bigsanghyuk.four_uni.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}

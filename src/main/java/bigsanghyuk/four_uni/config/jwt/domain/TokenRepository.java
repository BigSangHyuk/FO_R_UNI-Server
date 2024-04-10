package bigsanghyuk.four_uni.config.jwt.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {

    Optional<Token> findByRefreshToken(String refreshToken);
}

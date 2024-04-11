package bigsanghyuk.four_uni.config.jwt.repository;

import bigsanghyuk.four_uni.config.jwt.domain.entity.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, Long> {
}

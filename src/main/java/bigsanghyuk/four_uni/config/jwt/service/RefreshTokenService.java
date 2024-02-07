package bigsanghyuk.four_uni.config.jwt.service;

import bigsanghyuk.four_uni.config.jwt.domain.RefreshToken;
import bigsanghyuk.four_uni.config.jwt.domain.RefreshTokenRepository;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(TokenNotFoundException::new);
    }
}

package bigsanghyuk.four_uni.config.jwt.service;

import bigsanghyuk.four_uni.config.jwt.TokenProvider;
import bigsanghyuk.four_uni.config.jwt.domain.RefreshToken;
import bigsanghyuk.four_uni.config.jwt.domain.RefreshTokenRepository;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    private static final int EXP_ACCESS = 30;
    private static final int EXP_REFRESH = 43200;

    public String issueRefreshToken(Long userId) {
        String refreshToken = tokenProvider.generateToken(userId, EXP_REFRESH);
        RefreshToken tokenEntity = new RefreshToken(userId, refreshToken);
        refreshTokenRepository.save(tokenEntity);
        return refreshToken;
    }

    public String updateRefreshToken(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(TokenNotFoundException::new);
        String newRefreshToken = tokenProvider.generateToken(tokenEntity.getUserId(), EXP_REFRESH);
        tokenEntity.updateRefreshToken(newRefreshToken);
        refreshTokenRepository.save(tokenEntity);
        return newRefreshToken;
    }

    public String refreshAccessToken(String refreshToken) {
        RefreshToken tokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken).orElseThrow(TokenNotFoundException::new);
        return tokenProvider.generateToken(tokenEntity.getUserId(), EXP_ACCESS);
    }

    public String createAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new TokenNotFoundException();
        }
        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        return tokenProvider.generateToken(userId, EXP_ACCESS);
    }
}

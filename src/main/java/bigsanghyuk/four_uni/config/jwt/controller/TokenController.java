package bigsanghyuk.four_uni.config.jwt.controller;

import bigsanghyuk.four_uni.config.jwt.dto.request.CreateAccessTokenRequest;
import bigsanghyuk.four_uni.config.jwt.dto.request.IssueRefreshTokenRequest;
import bigsanghyuk.four_uni.config.jwt.dto.request.RefreshAccessTokenRequest;
import bigsanghyuk.four_uni.config.jwt.dto.request.UpdateRefreshTokenRequest;
import bigsanghyuk.four_uni.config.jwt.dto.response.CreateAccessTokenResponse;
import bigsanghyuk.four_uni.config.jwt.dto.response.IssueRefreshTokenResponse;
import bigsanghyuk.four_uni.config.jwt.dto.response.RefreshAccessTokenResponse;
import bigsanghyuk.four_uni.config.jwt.dto.response.UpdateRefreshTokenResponse;
import bigsanghyuk.four_uni.config.jwt.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/jwt")
@Tag(name = "tokens", description = "JWT API")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "access token 발급", description = "body 에 refreshToken 담아서 요청")
    @PostMapping("/create-access-token")
    public ResponseEntity<CreateAccessTokenResponse> createAccessToken(@RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = tokenService.createAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    @Operation(summary = "refresh token 발급", description = "body 에 userId 담아서 요청")
    @PostMapping("/issue-refresh-token")
    public ResponseEntity<IssueRefreshTokenResponse> issueRefreshToken(@RequestBody IssueRefreshTokenRequest request) {
        String newRefreshToken = tokenService.issueRefreshToken(request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new IssueRefreshTokenResponse(newRefreshToken));
    }

    @Operation(summary = "refresh token 재발급", description = "body 에 refreshToken 담아서 요청")
    @PostMapping("/update-refresh-token")
    public ResponseEntity<UpdateRefreshTokenResponse> updateRefreshToken(@RequestBody UpdateRefreshTokenRequest request) {
        String updatedRefreshToken = tokenService.updateRefreshToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(new UpdateRefreshTokenResponse(updatedRefreshToken));
    }

    @Operation(summary = "access token 재발급", description = "body 에 refreshToken 담아서 요청")
    @PostMapping("/refresh-access-token")
    public ResponseEntity<RefreshAccessTokenResponse> refreshAccessToken(@RequestBody RefreshAccessTokenRequest request) {
        String newAccessToken = tokenService.refreshAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.OK).body(new RefreshAccessTokenResponse(newAccessToken));
    }
}

package bigsanghyuk.four_uni.user.controller;

import bigsanghyuk.four_uni.common.CommonResponse;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.user.dto.request.*;
import bigsanghyuk.four_uni.user.dto.response.EditResponse;
import bigsanghyuk.four_uni.user.dto.response.LoginResponse;
import bigsanghyuk.four_uni.user.dto.response.SignResponse;
import bigsanghyuk.four_uni.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "users", description = "회원 API - JWT")
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 등록")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignRequest request) throws Exception {
        userService.register(request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request.toDomain());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/log-out")
    public ResponseEntity<CommonResponse> logout(@RequestAttribute(name = "userId") Long userId, @RequestBody LogoutUserRequest request) throws JsonProcessingException {
        userService.logout(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "탈퇴", description = "user를 참조하는 엔티티 모두 삭제")
    @DeleteMapping("/leave")
    public ResponseEntity<CommonResponse> leave(@RequestAttribute(name = "userId") Long userId) {
        userService.leave(userId);
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "회원 정보 수정")
    @PatchMapping("/users/edit")
    public ResponseEntity<EditResponse> editUser(@RequestAttribute(name = "userId") Long userId, @RequestBody EditUserRequest request) {
        EditResponse response = userService.edit(userId, request.toDomain());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "비밀번호 변경", description = "body에 이전 비밀번호 (임시 비밀번호), 신규 비밀번호 전달")
    @PatchMapping("/users/password")
    public ResponseEntity<CommonResponse> changePassword(@RequestAttribute(name = "userId") Long userId, @RequestBody ChangePasswordRequest request) {
        userService.changePassword(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "유저 조회", description = "일반 권한 유저는 본인 정보만 열람")
    @GetMapping("/users/info")
    public ResponseEntity<SignResponse> getUser(@RequestAttribute(name = "userId") Long userId) {
        SignResponse response = userService.getUser(userId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "관리자 권한 유저 조회", description = "관리자 권한 유저는 userId로 모든 유저 조회가능")
    @GetMapping("/admins/info/{userId}")
    public ResponseEntity<SignResponse> getUserForAdmin(@PathVariable(name = "userId") Long userId) {
        SignResponse response = userService.getUser(userId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "토큰 재발급 요청", description = "body 에 accessToken, refreshToken 담아서 요청")
    @PatchMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto tokenDto) throws Exception {
        TokenDto newTokenDto = userService.refreshAccessToken(tokenDto);
        return ResponseEntity.ok().body(newTokenDto);
    }
}

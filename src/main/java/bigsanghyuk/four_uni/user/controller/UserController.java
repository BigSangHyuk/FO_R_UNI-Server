package bigsanghyuk.four_uni.user.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.user.dto.request.EditRequest;
import bigsanghyuk.four_uni.user.dto.request.LoginRequest;
import bigsanghyuk.four_uni.user.dto.request.SignRequest;
import bigsanghyuk.four_uni.user.dto.response.EditResponse;
import bigsanghyuk.four_uni.user.dto.response.LoginResponse;
import bigsanghyuk.four_uni.user.dto.response.SignResponse;
import bigsanghyuk.four_uni.user.service.UserService;
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
        return ResponseEntity.ok().body(new CommonResponse(userService.register(request.toDomain())));

    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest request) {
        return ResponseEntity.ok().body(userService.login(request.toDomain()));
    }

    @Operation(summary = "회원 정보 수정")
    @PatchMapping("/users/{userId}")
    public ResponseEntity<EditResponse> editUser(@PathVariable(name = "userId") Long userId, @RequestBody EditRequest request) {
        return ResponseEntity.ok().body(userService.edit(userId, request.toDomain()));
    }

    @Operation(summary = "유저 조회", description = "경로에 이메일 입력")
    @GetMapping("/users/{userId}")
    public ResponseEntity<SignResponse> getUser(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }

    @Operation(summary = "관리자 권한 유저 조회", description = "경로에 이메일 입력")
    @GetMapping("/admins/{userId}")
    public ResponseEntity<SignResponse> getUserForAdmin(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok().body(userService.getUser(userId));
    }

    @Operation(summary = "토큰 재발급 요청", description = "body 에 accessToken, refreshToken 담아서 요청")
    @GetMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto tokenDto) throws Exception {
        return new ResponseEntity<>(userService.refreshAccessToken(tokenDto), HttpStatus.OK);
    }
}

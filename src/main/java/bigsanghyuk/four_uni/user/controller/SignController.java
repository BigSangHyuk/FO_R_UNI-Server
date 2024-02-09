package bigsanghyuk.four_uni.user.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.user.dto.request.EditRequest;
import bigsanghyuk.four_uni.user.dto.request.LoginRequest;
import bigsanghyuk.four_uni.user.dto.request.SignRequest;
import bigsanghyuk.four_uni.user.dto.response.EditResponse;
import bigsanghyuk.four_uni.user.dto.response.GetUserResponse;
import bigsanghyuk.four_uni.user.dto.response.LoginResponse;
import bigsanghyuk.four_uni.user.dto.response.SignResponse;
import bigsanghyuk.four_uni.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "users_jwt", description = "회원 API - JWT")
public class SignController {

    private final UserService userService;

    @Operation(summary = "회원 등록")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignRequest request) throws Exception {
        return ResponseEntity.ok().body(new CommonResponse(userService.register(request)));

    }

    @Operation(summary = "로그인")
    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest request) throws Exception {
        return ResponseEntity.ok().body(userService.login(request));
    }

    @Operation(summary = "회원 정보 수정")
    @PatchMapping("/user/edit")
    public ResponseEntity<EditResponse> editUser(@RequestBody EditRequest request) {
        return ResponseEntity.ok().body(userService.edit(request));
    }

    @Operation(summary = "유저 조회")
    @GetMapping("/user/get")
    public ResponseEntity<SignResponse> getUser(@RequestParam(name = "email") String email) throws Exception {
        return ResponseEntity.ok().body(userService.getMember(email));
    }

    @Operation(summary = "관리자 권한 유저 조회")
    @GetMapping("/admin/get")
    public ResponseEntity<SignResponse> getUserForAdmin(@RequestParam(name = "email") String email) throws Exception {
        return ResponseEntity.ok().body(userService.getMember(email));
    }

    @Operation(summary = "토큰 재발급 요청")
    @GetMapping("/refresh")
    public ResponseEntity<TokenDto> refresh(@RequestBody TokenDto tokenDto) throws Exception {
        return new ResponseEntity<>(userService.refreshAccessToken(tokenDto), HttpStatus.OK);
    }
}

package bigsanghyuk.four_uni.user.controller;

import bigsanghyuk.four_uni.user.dto.request.LoginUserRequest;
import bigsanghyuk.four_uni.user.dto.request.UpdateUserRequest;
import bigsanghyuk.four_uni.user.dto.request.RegisterUserRequest;
import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.user.dto.response.GetUserResponse;
import bigsanghyuk.four_uni.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/register")
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        userService.register(request.toDomain());
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @PatchMapping("/v1/mypage/edit")
    public ResponseEntity<GetUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateUser(updateUserRequest.toDomain());
        GetUserResponse getUserResponse = new GetUserResponse(updateUserRequest.getPassword(), updateUserRequest.getNickName(), updateUserRequest.getImage());
        return new ResponseEntity(getUserResponse, HttpStatus.OK);
    }

    @GetMapping("/v1/login")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody LoginUserRequest request) {
        boolean isLoginSuccess = userService.login(request.toDomain());
        return new ResponseEntity<>(new CommonResponse(isLoginSuccess), HttpStatus.OK);
    }
}

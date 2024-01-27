package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.controller.request.UpdateUserRequest;
import bigsanghyuk.four_uni.controller.request.UserRegisterRequest;
import bigsanghyuk.four_uni.controller.response.CommonResponse;
import bigsanghyuk.four_uni.controller.response.GetUserResponse;
import bigsanghyuk.four_uni.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/register")
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        userService.register(request.toDomain());
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @PatchMapping("/v1/mypage/edit")
    public ResponseEntity<GetUserResponse> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        userService.updateUser(updateUserRequest.toDomain());
        GetUserResponse getUserResponse = new GetUserResponse(updateUserRequest.getPassword(), updateUserRequest.getNickName(), updateUserRequest.getImage());
        return new ResponseEntity(getUserResponse, HttpStatus.OK);
    }

}

package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.entity.Authority;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class EditRequest {

    @NotNull
    private Long id;
    private String email;
    private String password;
    private String name;
    private String nickName;
    private int dept;
    private String image;
}

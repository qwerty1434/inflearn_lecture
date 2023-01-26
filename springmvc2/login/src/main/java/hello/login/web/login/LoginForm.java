package hello.login.web.login; //web에 생성

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginForm {
    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;
}

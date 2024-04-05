package zerobase.dividends.dto;

import lombok.Data;
import zerobase.dividends.domain.Member;

import java.util.List;

public class AuthDto {

    @Data
    public static class Logout {
        private String username;
    }

    @Data
    public static class LogIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        private List<String> roles;

        public Member toEntity() {
            return Member.builder()
                        .username(this.username)
                        .password(this.password)
                        .roles(this.roles)
                        .build();
        }
    }
}

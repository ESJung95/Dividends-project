package zerobase.dividends.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.dividends.dto.AuthDto;
import zerobase.dividends.security.TokenProvider;
import zerobase.dividends.service.MemberService;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AuthDto.SignUp request) {
        log.info("Signup request for username -> " + request.getUsername());
        var result = this.memberService.register(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody AuthDto.SignIn request) {
        log.info("Signin request for username -> " + request.getUsername());
        // 아이디와 패스워드가 일치하는지 확인 = 패스워드 검증
        var member = this.memberService.authenticate(request);
        // 토큰을 생성해서 반환
        var token = this.tokenProvider.generateToken(member.getUsername(), member.getRoles());

        return ResponseEntity.ok(token);
    }

    // TODO _ 로그아웃 기능 구현
    @PostMapping("/signout")
    public ResponseEntity<?> signout(@RequestBody AuthDto.SignOut request) {
        log.info("Signout request for username -> " + request.getUsername());
        return null;
    }

}

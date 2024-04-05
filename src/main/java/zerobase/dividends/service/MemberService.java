package zerobase.dividends.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.dividends.domain.Member;
import zerobase.dividends.dto.AuthDto;
import zerobase.dividends.exception.impl.AlreadyExistUserException;
import zerobase.dividends.exception.impl.NoExistIdException;
import zerobase.dividends.exception.impl.NoExistUserException;
import zerobase.dividends.exception.impl.NoMatchPassword;
import zerobase.dividends.repository.MemberRepository;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(NoExistUserException::new);
    }

    // 회원 가입
    public Member register(AuthDto.SignUp member) {
        // 아이디 중복 체크
        boolean exists = this.memberRepository.existsByUsername(member.getUsername());
        if (exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(this.passwordEncoder.encode(member.getPassword()));
        return this.memberRepository.save(member.toEntity());
    }

    // 로그인 시 검증
    public Member authenticate(AuthDto.SignIn member) {
        var user = this.memberRepository.findByUsername(member.getUsername())
                                     .orElseThrow(NoExistIdException::new);

        // 사용자에게 받아오는 password 인코딩해서 비교
        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new NoMatchPassword();
        }

        return user;
    }
}

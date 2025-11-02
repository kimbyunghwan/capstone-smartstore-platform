package me.bttf.smartstore.auth;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        System.out.println(">>> 로그인 시 불러온 roles: " + member.getRoles());
        return new CustomUserDetails(member);
    }
}

package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.auth.JwtUtil;
import me.bttf.smartstore.auth.SessionMember;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.domain.member.MemberType;
import me.bttf.smartstore.domain.member.Role;
import me.bttf.smartstore.domain.store.Store;
import me.bttf.smartstore.domain.store.StoreStatus;
import me.bttf.smartstore.dto.auth.LoginReq;
import me.bttf.smartstore.dto.auth.RegisterReq;
import me.bttf.smartstore.dto.auth.TokenRes;
import me.bttf.smartstore.repository.MemberRepository;
import me.bttf.smartstore.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public AuthService(MemberRepository repo,
                       StoreRepository storeRepository,
                       @Value("${app.jwt.secret}") String secret,
                       @Value("${app.jwt.issuer}") String issuer,
                       @Value("${app.jwt.access-token-ttl-minutes}") long ttl) {
        this.memberRepository = repo;
        this.storeRepository = storeRepository;
        this.jwtUtil = new JwtUtil(secret, issuer, ttl);
    }

    // 1) 회원가입
    public void register(RegisterReq req) {
        if (memberRepository.existsByEmail(req.email()))
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");

        String hash = encoder.encode(req.password());
        Member m = Member.builder()
                .email(req.email())
                .password(hash)
                .name(req.name())
                .phone(req.phone())
                .memberType(req.memberType())
                .build();

        m.getRoles().clear();
        m.getRoles().add(req.memberType() == MemberType.BUYER ? Role.USER : Role.SELLER);

        m = memberRepository.saveAndFlush(m);

        if (m.getMemberType() == MemberType.BUYER) {
            m.getRoles().add(Role.USER);
        } else {
            m.getRoles().add(Role.SELLER);

            if (req.storeName() == null || req.storeName().isBlank()) {
                throw new IllegalArgumentException("판매자는 스토어 이름을 입력해야 합니다.");
            }

            Store store = Store.builder()
                    .owner(m)
                    .storeName(req.storeName().trim())
                    .status(StoreStatus.ACTIVE)
                    .build();

            storeRepository.save(store);
        }

        //memberRepository.save(m);
    }

    // 2) 로그인 + 토큰 발급
    public TokenRes login(LoginReq req) {
        Member m = memberRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!encoder.matches(req.password(), m.getPassword()))
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");

        String token = jwtUtil.createToken(
                m.getId().toString(),
                Map.of("email", m.getEmail(), "name", m.getName())
        );
        return new TokenRes(token);
    }

    // 3) 페이지용: 로그인 + 세션에 넣을 DTO 반환
    public SessionMember loginSession(LoginReq req) {
        Member m = authenticate(req);
        return new SessionMember(m.getId(), m.getName(), m.getEmail());
    }

    // 공통 인증 로직
    private Member authenticate(LoginReq req) {
        if (req.email() == null || req.email().isBlank()) throw new IllegalArgumentException("이메일을 입력하세요.");
        if (req.password() == null || req.password().isBlank()) throw new IllegalArgumentException("비밀번호를 입력하세요.");

        Member m = memberRepository.findByEmail(req.email())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!encoder.matches(req.password(), m.getPassword())) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return m;
    }
}

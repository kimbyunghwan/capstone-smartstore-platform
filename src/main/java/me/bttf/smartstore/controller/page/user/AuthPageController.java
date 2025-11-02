package me.bttf.smartstore.controller.page.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.auth.CustomUserDetails;
import me.bttf.smartstore.auth.SessionConst;
import me.bttf.smartstore.auth.SessionMember;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.domain.member.MemberType;
import me.bttf.smartstore.dto.auth.LoginReq;
import me.bttf.smartstore.dto.auth.RegisterReq;
import me.bttf.smartstore.repository.MemberRepository;
import me.bttf.smartstore.service.AuthService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static me.bttf.smartstore.domain.member.MemberType.BUYER;
import static me.bttf.smartstore.domain.member.Role.SELLER;

// 2~3. 회원가입(/register), 로그인(/login)
@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthService authService;
    private final MemberRepository memberRepo;

    @GetMapping("/register")
    public String register() {
        return "register"; // templates/register.html
    }

    /**
     * 회원가입 처리 (POST)
     */
    @PostMapping("/register")
    public String registerProcess(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam MemberType memberType,
            @RequestParam(required = false) String storeName,
            Model model) {

        try {
            authService.register(new RegisterReq(email, password, name, phone, memberType, storeName));
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("name", name);
            return "register";
        }
    }

    /**
     * 로그인 페이지 (GET)
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String registered,
            Model model) {

        if (error != null) {
            model.addAttribute("error", "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        if (registered != null) {
            model.addAttribute("success", "회원가입이 완료되었습니다. 로그인해주세요.");
        }

        return "login"; // templates/login.html
    }

    // 로그인 처리 (POST) - 세션 로그인
    @PostMapping("/login")
    public String loginProcess(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletRequest request,
            HttpSession session,
            Model model) {

        try {
            // 1) 비밀번호/이메일 검증
            SessionMember sm = authService.loginSession(new LoginReq(email, password));

            // 2) DB에서 Member 로드 → CustomUserDetails 생성
            Member member = memberRepo.findByEmail(sm.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

            CustomUserDetails principal = new CustomUserDetails(member); // ✅ 여기서 권한 자동 매핑
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    principal, null, principal.getAuthorities()
            );

            // 3) SecurityContext에 올리기
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
            request.getSession(true).setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

            // 4) 기존처럼 표시용 세션 값도 유지
            session.setAttribute(SessionConst.LOGIN_MEMBER, sm);

            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "login";
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
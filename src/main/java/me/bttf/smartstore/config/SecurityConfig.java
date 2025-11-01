package me.bttf.smartstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // @PreAuthorize, @Secured 등 메소드 보안 활성화
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 설정
                .csrf(csrf -> csrf
                        // API 경로는 CSRF 비활성화 (JWT 사용 시)
                        .ignoringRequestMatchers("/api/**")
                )

                // 인가 규칙
                .authorizeHttpRequests(auth -> auth
                        // 공개 경로 (누구나 접근 가능)
                        .requestMatchers(
                                "/",                          // 메인 페이지
                                "/products/**",               // 상품 목록/상세
                                "/css/**",                    // CSS 파일
                                "/js/**",                     // JavaScript 파일
                                "/images/**",                 // 이미지 파일
                                "/login",                     // 로그인 페이지
                                "/register",              // 회원가입 페이지
                                "/error",                     // 에러 페이지
                                "/favicon.ico"                // 파비콘
                        ).permitAll()

                        // 판매자 경로 (SELLER 역할 필요)
                        .requestMatchers("/seller/**").hasRole("SELLER")

                        // 관리자 경로 (ADMIN 역할 필요)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 그 외 모든 요청은 인증 필요
                        .anyRequest().authenticated()
                )

                // 폼 로그인 설정
                .formLogin(login -> login
                        .loginPage("/login")                    // 커스텀 로그인 페이지
                        .usernameParameter("email")             // username 대신 email 사용
                        .passwordParameter("password")           // 비밀번호 파라미터명
                        .defaultSuccessUrl("/", true)           // 로그인 성공 시 메인 페이지로
                        .failureUrl("/login?error=true")        // 로그인 실패 시
                        .permitAll()                            // 로그인 페이지는 누구나 접근
                )

                // 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/logout")                   // 로그아웃 URL
                        .logoutSuccessUrl("/")                  // 로그아웃 성공 시 메인으로
                        .deleteCookies("JSESSIONID")            // 세션 쿠키 삭제
                        .invalidateHttpSession(true)            // 세션 무효화
                        .permitAll()
                )

                // 세션 관리
                .sessionManagement(session -> session
                        .maximumSessions(1)                     // 동일 계정 동시 접속 1개만 허용
                        .maxSessionsPreventsLogin(false)        // 새 로그인이 이전 세션 만료시킴
                )

                // Remember-me (선택사항)
                .rememberMe(remember -> remember
                        .key("smartstore-remember-me-key")      // 쿠키 암호화 키
                        .tokenValiditySeconds(86400 * 7)        // 7일간 유효
                        .rememberMeParameter("remember-me")     // 체크박스 name
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt 암호화 (강도 10 - 기본값)
        return new BCryptPasswordEncoder();
    }
}

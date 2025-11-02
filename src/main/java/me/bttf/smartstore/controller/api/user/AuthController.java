package me.bttf.smartstore.controller.api.user;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.dto.auth.LoginReq;
import me.bttf.smartstore.dto.auth.RegisterReq;
import me.bttf.smartstore.dto.auth.TokenRes;
import me.bttf.smartstore.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterReq req) {
        authService.register(req);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<TokenRes> login(@RequestBody LoginReq req) {
        return ResponseEntity.ok(authService.login(req));
    }
}

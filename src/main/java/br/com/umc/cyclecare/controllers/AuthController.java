package br.com.umc.cyclecare.controllers;

import br.com.umc.cyclecare.daos.AuthDao;
import br.com.umc.cyclecare.dtos.AuthRequest;
import br.com.umc.cyclecare.dtos.GoogleUserInfo;
import br.com.umc.cyclecare.dtos.JwtResponse;
import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.User;
import br.com.umc.cyclecare.services.GoogleAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final GoogleAuthService googleAuthService;
    private final Controller controller;
    private final AuthDao authDao;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            DomainEntity userResp = controller.create(user);
            return ResponseEntity.ok(userResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest auth) {
        try {
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setJwt(authDao.login(auth));

            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody JwtResponse jwtResponse) {
        try {
            String jwt = jwtResponse.getJwt();

            GoogleUserInfo googleUserInfo = googleAuthService.verifyAccessToken(jwt);

            jwtResponse.setJwt(authDao.googleLogin(googleUserInfo, jwt));

            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody AuthRequest auth) {
        try {
            User user = new User();
            user.setEmail(auth.getEmail());

            controller.update(user);

            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

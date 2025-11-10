package br.com.umc.cyclecare.controllers;

import br.com.umc.cyclecare.daos.UserDao;
import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserDao userDao;

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal String id) {
        try {
            User user = new User();
            user.setId(id);
            DomainEntity userResp = userDao.read(user);
            return ResponseEntity.ok(userResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@AuthenticationPrincipal String id, @RequestBody User user) {
        try {
            user.setId(id);
            DomainEntity respUser = userDao.update(user);
            return ResponseEntity.ok(respUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal String id) {
        try {
            User user = new User();
            user.setId(id);
            userDao.delete(user);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

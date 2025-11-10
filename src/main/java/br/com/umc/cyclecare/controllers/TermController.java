package br.com.umc.cyclecare.controllers;

import br.com.umc.cyclecare.daos.TermDao;
import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/terms")
@RequiredArgsConstructor
public class TermController {
    private final TermDao termDao;

    @GetMapping
    public ResponseEntity<?> getTerm() {
        try {
            Term term = new Term();
            term.setActive(true);

            DomainEntity termResp = termDao.read(term);
            return ResponseEntity.ok(termResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody Term term) {
        try {
            DomainEntity userResp = termDao.create(term);
            return ResponseEntity.ok(userResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

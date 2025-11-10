package br.com.umc.cyclecare.controllers;

import br.com.umc.cyclecare.models.Cycle;
import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cycles")
@RequiredArgsConstructor
public class CycleController {
    private final Controller controller;

    @GetMapping
    public ResponseEntity<?> getCycles(@AuthenticationPrincipal String id) {
        try {
            User user = new User();
            user.setId(id);

            Cycle cycle = new Cycle();
            cycle.setUser(user);

            List<DomainEntity> cyclesResp = controller.listAll(cycle);
            return ResponseEntity.ok(cyclesResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCycle(@PathVariable String id) {
        try {
            Cycle cycle = new Cycle();
            cycle.setId(id);

            DomainEntity cyclesResp = controller.read(cycle);
            return ResponseEntity.ok(cyclesResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createCycle(@AuthenticationPrincipal String id, @RequestBody Cycle cycle) {
        try {
            User user = new User();
            user.setId(id);

            cycle.setUser(user);

            DomainEntity cycleResp = controller.create(cycle);

            return ResponseEntity.ok(cycleResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCycle(@PathVariable String id) {
        try {
            Cycle cycle = new Cycle();
            cycle.setId(id);

            controller.delete(cycle);

            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCycle(@PathVariable String id, @RequestBody Cycle cycle) {
        try {
            cycle.setId(id);

            DomainEntity cycleResp = controller.update(cycle);

            return ResponseEntity.ok(cycleResp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

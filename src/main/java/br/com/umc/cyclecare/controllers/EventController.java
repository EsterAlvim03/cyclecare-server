package br.com.umc.cyclecare.controllers;

import br.com.umc.cyclecare.daos.UserDao;
import br.com.umc.cyclecare.dtos.CalendarEventResponse;
import br.com.umc.cyclecare.dtos.EventRequest;
import br.com.umc.cyclecare.models.DomainEntity;
import br.com.umc.cyclecare.models.User;
import br.com.umc.cyclecare.services.GoogleCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final GoogleCalendarService googleCalendarService;
    private final UserDao userDao;

    @GetMapping
    public ResponseEntity<?> getEvents(@AuthenticationPrincipal String id) {
        try {
            List<CalendarEventResponse> events = googleCalendarService.listUpcomingEvents(getToken(id));

            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@AuthenticationPrincipal String id, @PathVariable String eventId) {
        try {
            EventRequest event = new EventRequest();
            event.setAccessToken(getToken(id));
            event.setId(eventId);

            CalendarEventResponse respEvent = googleCalendarService.getEvent(event);

            return ResponseEntity.ok(respEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@AuthenticationPrincipal String id, @RequestBody EventRequest event) {
        try {
            event.setAccessToken(getToken(id));
            CalendarEventResponse respEvent = googleCalendarService.createEvent(event);

            return ResponseEntity.ok(respEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@AuthenticationPrincipal String id, @PathVariable String eventId, @RequestBody EventRequest event) {
        try {
            event.setAccessToken(getToken(id));
            event.setId(eventId);
            CalendarEventResponse respEvent = googleCalendarService.updateEvent(event);

            return ResponseEntity.ok(respEvent);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@AuthenticationPrincipal String id, @PathVariable String eventId) {
        try {
            EventRequest event = new EventRequest();
            event.setAccessToken(getToken(id));
            event.setId(eventId);
            googleCalendarService.deleteEvent(event);

            return ResponseEntity.ok(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private String getToken(String id) {
        User userId = new User();
        userId.setId(id);

        DomainEntity domainEntity = userDao.read(userId);
        User user = (User) domainEntity;

        if (user.getGoogleAccessToken() == null) {
            throw new RuntimeException("Você precisa entrar com o Google para ter acesso aos eventos");
        }

        return user.getGoogleAccessToken();
    }
}
package br.com.umc.cyclecare.services;

import br.com.umc.cyclecare.config.GoogleConfig;
import br.com.umc.cyclecare.dtos.CalendarEventResponse;
import br.com.umc.cyclecare.dtos.EventRequest;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoogleCalendarService {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final GoogleConfig googleConfig;

    private Calendar getCalendarService(String accessToken) {
        try {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            Credential credential = new Credential(BearerToken.authorizationHeaderAccessMethod())
                    .setAccessToken(accessToken);

            return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(googleConfig.getApplication().getName())
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Erro ao conectar com Google Calendar", e);
        }
    }

    public List<CalendarEventResponse> listUpcomingEvents(String accessToken) {
        try {
            Calendar service = getCalendarService(accessToken);

            DateTime now = new DateTime(System.currentTimeMillis());

            Events events = service.events().list("primary")
                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

            return events.getItems().stream()
                    .filter(event -> event.getSummary() != null && event.getSummary().startsWith("CycleCare - "))
                    .map(this::convertToResponse)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new RuntimeException("Erro ao listar eventos do Calendar", e);
        }
    }

    public CalendarEventResponse createEvent(EventRequest request) {
        try {
            Calendar service = getCalendarService(request.getAccessToken());

            Event event = new Event()
                    .setSummary("CycleCare - " + request.getSummary())
                    .setDescription(request.getDescription());

            ZoneId zoneId = ZoneId.of(request.getTimeZone());
            ZonedDateTime startZoned = request.getStartDateTime().atZone(zoneId);
            ZonedDateTime endZoned = request.getEndDateTime().atZone(zoneId);

            EventDateTime start = new EventDateTime()
                    .setDateTime(new DateTime(startZoned.toInstant().toEpochMilli()))
                    .setTimeZone(request.getTimeZone());
            event.setStart(start);

            EventDateTime end = new EventDateTime()
                    .setDateTime(new DateTime(endZoned.toInstant().toEpochMilli()))
                    .setTimeZone(request.getTimeZone());
            event.setEnd(end);

            Event createdEvent = service.events().insert("primary", event).execute();

            return convertToResponse(createdEvent);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar evento no Calendar", e);
        }
    }

    public CalendarEventResponse getEvent(EventRequest event) {
        try {
            Calendar service = getCalendarService(event.getAccessToken());

            Event respEvent = service.events().get("primary", event.getId()).execute();

            return convertToResponse(respEvent);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao buscar evento do Calendar", e);
        }
    }

    public CalendarEventResponse updateEvent(EventRequest request) {
        try {
            Calendar service = getCalendarService(request.getAccessToken());

            Event event = new Event()
                    .setSummary("CycleCare - " + request.getSummary())
                    .setDescription(request.getDescription());

            ZoneId zoneId = ZoneId.of(request.getTimeZone());
            ZonedDateTime startZoned = request.getStartDateTime().atZone(zoneId);
            ZonedDateTime endZoned = request.getEndDateTime().atZone(zoneId);

            EventDateTime start = new EventDateTime()
                    .setDateTime(new DateTime(startZoned.toInstant().toEpochMilli()))
                    .setTimeZone(request.getTimeZone());
            event.setStart(start);

            EventDateTime end = new EventDateTime()
                    .setDateTime(new DateTime(endZoned.toInstant().toEpochMilli()))
                    .setTimeZone(request.getTimeZone());
            event.setEnd(end);

            Event updatedEvent = service.events().update("primary", request.getId(), event).execute();

            return convertToResponse(updatedEvent);

        } catch (IOException e) {
            throw new RuntimeException("Erro ao atualizar evento no Calendar", e);
        }
    }

    public void deleteEvent(EventRequest event) {
        try {
            Calendar service = getCalendarService(event.getAccessToken());

            service.events().delete("primary", event.getId()).execute();

        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar evento do Calendar", e);
        }
    }

    private CalendarEventResponse convertToResponse(Event event) {
        CalendarEventResponse response = new CalendarEventResponse();
        response.setId(event.getId());
        response.setSummary(event.getSummary());
        response.setDescription(event.getDescription());
        response.setStatus(event.getStatus());
        response.setHtmlLink(event.getHtmlLink());

        if (event.getStart() != null) {
            Map<String, String> start = new HashMap<>();
            if (event.getStart().getDateTime() != null) {
                start.put("dateTime", event.getStart().getDateTime().toString());
            }
            if (event.getStart().getDate() != null) {
                start.put("date", event.getStart().getDate().toString());
            }
            response.setStart(start);
        }

        if (event.getEnd() != null) {
            Map<String, String> end = new HashMap<>();
            if (event.getEnd().getDateTime() != null) {
                end.put("dateTime", event.getEnd().getDateTime().toString());
            }
            if (event.getEnd().getDate() != null) {
                end.put("date", event.getEnd().getDate().toString());
            }
            response.setEnd(end);
        }

        return response;
    }
}
package br.com.umc.cyclecare.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EventRequest {
    private String id;
    private String accessToken;
    private String summary;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String timeZone = "America/Sao_Paulo";
}
package br.com.umc.cyclecare.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class CalendarEventResponse {
    private String id;
    private String summary;
    private String description;
    private String status;
    private String htmlLink;
    private Map<String, String> start;
    private Map<String, String> end;
}
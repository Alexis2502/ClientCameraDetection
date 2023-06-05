package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventLogEntry {
    private String eventId;
    private Date date;
    private String userLogin;
    private boolean dangerDetected;
    private List<DetectedPerson> detectedPeople;
}
package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetectedPerson {
    // true jeśli ma, false jeśli nie ma, null jeśli na zdjęciu nie widać
    // danej części ciała
    private Boolean hasFaceCover;
    private Boolean hasHandsCover;
    private Boolean hasHeadCover;
}
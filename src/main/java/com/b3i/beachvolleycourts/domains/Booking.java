package com.b3i.beachvolleycourts.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    private String bookingId;
    private String name;
    private String startTime;
    private String endTime;
    private String userId;
    private List<User> playerList;
    private boolean isApproved;
    private String notes;
    private LocalDate cancellationDate;
    private String cancellationNotes;
    private String scheduleId;
    // TODO: private Payment payment;
}

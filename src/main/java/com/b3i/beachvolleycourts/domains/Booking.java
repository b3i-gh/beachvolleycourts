package com.b3i.beachvolleycourts.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    private String id;
    private String name;
    private String startTime;
    private String endTime;
    private String userId;
    private List<User> playerList;
    private boolean isApproved;
    private String notes;
    private LocalDate cancellationDate;
    private String cancellationNotes;
    // TODO: private Payment paymennt;
}
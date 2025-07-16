package com.example.report_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocketNewApplicationEvent {
    private String bookingUuid;
    private String consultantUuid;
    private SubmitApplicationEvent submitApplicationEvent;
}

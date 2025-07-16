package com.example.report_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitApplicationEvent {
    Integer totalApplication;
    Integer approveCount;
    Integer rejectCount;
}

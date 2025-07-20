package com.example.report_service.Service;

import com.example.report_service.DTO.Response.BookingReportResponse;
import com.example.report_service.DTO.Response.UserReportResponse;
import com.example.report_service.Entity.BookingReport;
import com.example.report_service.Entity.UserReport;
import com.example.report_service.InterFace.IUserReport;
import com.example.report_service.Service.redis.RedisService;
import com.example.report_service.event.UserReportEvent;
import com.example.report_service.Repository.UserReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserReportService implements IUserReport {

    private final RedisService redisService;
    private final UserReportRepository userReportRepository;

    public UserReport userReport(Map<String, String> data) {
        int newUser = data.containsKey("newUser") ? Integer.parseInt(data.get("newUser")) : 0;

        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        int weekOfYear = now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        Optional<UserReport> optional = userReportRepository.findByMonthAndYearAndWeekOfYear(month, year, weekOfYear);

        UserReport report = optional.orElseGet(() -> {
            UserReport r = new UserReport();
            r.setMonth(month);
            r.setYear(year);
            r.setWeekOfYear(weekOfYear);
            r.setNewUser(0);
            return r;
        });

        report.setNewUser(report.getNewUser() + newUser);
        userReportRepository.save(report);

        redisService.sendApplicationMessage("new-user", "/topic/new-user-report");

        return report;
    }

    public UserReportResponse getCount(){
        List<UserReport> userReports = userReportRepository.findAll();

        int totalUser = 0;

        for (UserReport report : userReports) {
            totalUser += report.getNewUser();
        }

        UserReportResponse response = new UserReportResponse();
        response.setNewUser(totalUser);

        return response;
    }

    public List<UserReport> filter (Integer weakOfYear, Integer month, Integer year){
        return userReportRepository.filter(weakOfYear,month,year);
    }

}

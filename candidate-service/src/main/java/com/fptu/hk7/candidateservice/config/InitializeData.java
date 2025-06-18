package com.fptu.hk7.candidateservice.config;

import com.fptu.hk7.candidateservice.pojo.Scholarship;
import com.fptu.hk7.candidateservice.service.ScholarshipService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class InitializeData {
    @Bean
    public CommandLineRunner commandLineRunner(ScholarshipService scholarshipService) {
        return args -> {
            if (scholarshipService.count() == 0) {
                scholarshipService.save(new Scholarship(
                        "Học bổng Chuyên gia Toàn cầu",
                        "100 suất. Học bổng dành cho sinh viên xuất sắc, định hướng chuyên gia toàn cầu."
                ));
                scholarshipService.save(new Scholarship(
                        "Học bổng Học đường",
                        "900 suất. Học bổng cho sinh viên có thành tích học tập tốt ở bậc phổ thông."
                ));
                scholarshipService.save(new Scholarship(
                        "Học bổng Toàn phần",
                        "300 suất. Học bổng miễn toàn bộ học phí cho sinh viên ưu tú."
                ));
                scholarshipService.save(new Scholarship(
                        "Học bổng 2 năm",
                        "500 suất. Học bổng hỗ trợ học phí 2 năm cho sinh viên."
                ));
                scholarshipService.save(new Scholarship(
                        "Học bổng 1 năm",
                        "1.000 suất. Học bổng hỗ trợ học phí 1 năm cho sinh viên."
                ));
                scholarshipService.save(new Scholarship(
                        "Học bổng nữ sinh STEM",
                        "Học bổng dành riêng cho nữ sinh có thành tích xuất sắc ngành STEM."
                ));
            }
        };
    }
}

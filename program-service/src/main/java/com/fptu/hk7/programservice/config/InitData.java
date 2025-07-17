package com.fptu.hk7.programservice.config;

import com.fptu.hk7.programservice.pojo.Campus;
import com.fptu.hk7.programservice.pojo.Major;
import com.fptu.hk7.programservice.pojo.Offering;
import com.fptu.hk7.programservice.pojo.Specialization;
import com.fptu.hk7.programservice.repository.CampusRepository;
import com.fptu.hk7.programservice.repository.MajorRepository;
import com.fptu.hk7.programservice.repository.OfferingRepository;
import com.fptu.hk7.programservice.repository.SpecializationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class InitData {
    @Bean
    public CommandLineRunner initDataProgram(CampusRepository campusRepo,
                                      MajorRepository majorRepo,
                                      OfferingRepository offeringRepo,
                                      SpecializationRepository specializationRepo) {
        return args -> {
            if (campusRepo.count() == 0) {
                // ======= CAMPUS =======
                Campus hanoi = new Campus(
                        null,
                        "Đại học FPT Hà Nội",
                        "Khu Công nghệ cao Hòa Lạc, Thạch Thất, Hà Nội",
                        "Cơ sở chính",
                        "hanoi@fpt.edu.vn",
                        "02473001866",
                        null,
                        new ArrayList<>()
                );

                Campus hcm = new Campus(
                        null,
                        "Đại học FPT TP.HCM",
                        "Lô E2a-7, Đường D1, Khu Công nghệ cao, TP Thủ Đức, TP.HCM",
                        "Cơ sở TP.HCM",
                        "hcmc@fpt.edu.vn",
                        "02873001866",
                        null,
                        new ArrayList<>()
                );

                Campus danang = new Campus(
                        null,
                        "Đại học FPT Đà Nẵng",
                        "Khu đô thị công nghệ FPT City, Ngũ Hành Sơn, Đà Nẵng",
                        "Cơ sở Đà Nẵng",
                        "danang@fpt.edu.vn",
                        "023673001866",
                        null,
                        new ArrayList<>()
                );

                Campus cantho = new Campus(
                        null,
                        "Đại học FPT Cần Thơ",
                        "Khu đô thị Nam Cần Thơ, Quận Cái Răng, TP Cần Thơ",
                        "Cơ sở Cần Thơ",
                        "cantho@fpt.edu.vn",
                        "029273001866",
                        null,
                        new ArrayList<>()
                );

                campusRepo.saveAll(List.of(hanoi, hcm, danang, cantho));

                // ======= MAJOR =======
                Major it = new Major(null, "Software Engineering", "Kỹ thuật phần mềm", new ArrayList<>());
                Major ai = new Major(null, "Artificial Intelligence", "Trí tuệ nhân tạo", new ArrayList<>());
                Major biz = new Major(null, "Business Administration", "Quản trị kinh doanh", new ArrayList<>());
                Major tourism = new Major(null, "Hospitality & Tourism Management", "Quản trị khách sạn và du lịch", new ArrayList<>());
                Major japanese = new Major(null, "Japanese Studies", "Ngôn ngữ Nhật", new ArrayList<>());
                Major english = new Major(null, "English Language", "Ngôn ngữ Anh", new ArrayList<>());
                Major multimedia = new Major(null, "Multimedia Communications", "Truyền thông đa phương tiện", new ArrayList<>());
                Major graphic = new Major(null, "Graphic Design", "Thiết kế đồ họa", new ArrayList<>());

                majorRepo.saveAll(List.of(it, ai, biz, tourism, japanese, english, multimedia, graphic));

                // ======= SPECIALIZATION =======
                List<Specialization> specs = new ArrayList<>();
                for (Major major : List.of(it, ai, biz, tourism, japanese, english, multimedia, graphic)) {
                    if (major.getName().equals("Software Engineering")) {
                        specs.add(new Specialization(null, "Web Development", "Lập trình web", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "Mobile Development", "Lập trình di động", major, new ArrayList<>()));
                    } else if (major.getName().equals("Artificial Intelligence")) {
                        specs.add(new Specialization(null, "Data Science", "Khoa học dữ liệu", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "Machine Learning", "Học máy", major, new ArrayList<>()));
                    } else if (major.getName().equals("Business Administration")) {
                        specs.add(new Specialization(null, "International Business", "Kinh doanh quốc tế", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "Digital Marketing", "Marketing số", major, new ArrayList<>()));
                    } else if (major.getName().equals("Hospitality & Tourism Management")) {
                        specs.add(new Specialization(null, "Hotel Management", "Quản trị khách sạn", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "Travel Management", "Quản trị du lịch", major, new ArrayList<>()));
                    } else if (major.getName().equals("Japanese Studies")) {
                        specs.add(new Specialization(null, "Translation & Interpretation", "Biên phiên dịch", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "Business Japanese", "Tiếng Nhật thương mại", major, new ArrayList<>()));
                    } else if (major.getName().equals("English Language")) {
                        specs.add(new Specialization(null, "Translation & Interpretation", "Biên phiên dịch", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "Teaching English", "Giảng dạy tiếng Anh", major, new ArrayList<>()));
                    } else if (major.getName().equals("Multimedia Communications")) {
                        specs.add(new Specialization(null, "Content Production", "Sản xuất nội dung", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "Brand Communication", "Truyền thông thương hiệu", major, new ArrayList<>()));
                    } else if (major.getName().equals("Graphic Design")) {
                        specs.add(new Specialization(null, "Visual Branding", "Thiết kế nhận diện thương hiệu", major, new ArrayList<>()));
                        specs.add(new Specialization(null, "UI/UX Design", "Thiết kế giao diện người dùng", major, new ArrayList<>()));
                    }
                }
                specializationRepo.saveAll(specs);

                // ======= OFFERING =======
                Random rand = new Random();
                List<Offering> offerings = new ArrayList<>();
                for (Specialization spec : specs) {
                    for (Campus campus : List.of(hanoi, hcm, danang, cantho)) {
                        Offering o = new Offering();
                        o.setYear(2025);
                        o.setTarget(rand.nextInt(151) + 100); // 100 - 250 chỉ tiêu
                        o.setPrice(28000000 + rand.nextInt(3) * 3000000); // 28tr - 34tr
                        o.setCampus(campus);
                        o.setSpecialization(spec);
                        offerings.add(o);
                    }
                }
                offeringRepo.saveAll(offerings);
            }
        };
    }
}

//package com.fptu.hk7.programservice.config;
//
//import com.fptu.hk7.programservice.pojo.Campus;
//import com.fptu.hk7.programservice.pojo.Major;
//import com.fptu.hk7.programservice.pojo.Offering;
//import com.fptu.hk7.programservice.pojo.Specialization;
//import com.fptu.hk7.programservice.repository.CampusRepository;
//import com.fptu.hk7.programservice.repository.MajorRepository;
//import com.fptu.hk7.programservice.repository.OfferingRepository;
//import com.fptu.hk7.programservice.repository.SpecializationRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Configuration
//public class InitData {
//    @Bean
//    public CommandLineRunner initDataProgram(CampusRepository campusRepo,
//                                      MajorRepository majorRepo,
//                                      OfferingRepository offeringRepo,
//                                      SpecializationRepository specializationRepo) {
//        return args -> {
//            // ======= CAMPUS =======
//            Campus hanoi = new Campus(null, "FPT Hà Nội", "Hòa Lạc, Hà Nội", "Cơ sở chính", new ArrayList<>());
//            Campus hcm = new Campus(null, "FPT Hồ Chí Minh", "Khu CNC Q.9, TP.HCM", "Cơ sở miền Nam", new ArrayList<>());
//            Campus danang = new Campus(null, "FPT Đà Nẵng", "Ngũ Hành Sơn, Đà Nẵng", "Cơ sở miền Trung", new ArrayList<>());
//            Campus cantho = new Campus(null, "FPT Cần Thơ", "Khu đô thị Cần Thơ", "Cơ sở miền Tây", new ArrayList<>());
//
//            campusRepo.saveAll(List.of(hanoi, hcm, danang, cantho));
//
//            // ======= MAJOR =======
//            Major it = new Major(null, "Computer Science", "Ngành Công nghệ thông tin", new ArrayList<>());
//            Major biz = new Major(null, "International Business", "Ngành Kinh doanh quốc tế", new ArrayList<>());
//            Major design = new Major(null, "Graphic Design", "Ngành Thiết kế đồ họa", new ArrayList<>());
//            Major comm = new Major(null, "Multimedia Communication", "Ngành truyền thông", new ArrayList<>());
//            Major english = new Major(null, "English Linguistics", "Ngành ngôn ngữ Anh", new ArrayList<>());
//
//            majorRepo.saveAll(List.of(it, biz, design, comm, english));
//
//            // ======= SPECIALIZATION =======
//            List<Specialization> specs = new ArrayList<>();
//            for (Major major : List.of(it, biz, design, comm, english)) {
//                for (int i = 1; i <= 2; i++) {
//                    Specialization s = new Specialization();
//                    s.setName("Specialization " + i + " of " + major.getName());
//                    s.setDescription("Mô tả chuyên ngành " + i + " của " + major.getName());
//                    s.setMajor(major);
//                    s.setOfferings(new ArrayList<>());
//                    specs.add(s);
//                }
//            }
//            specializationRepo.saveAll(specs);
//
//            // ======= OFFERING =======
//            Random rand = new Random();
//            List<Offering> offerings = new ArrayList<>();
//            for (Specialization spec : specs) {
//                for (Campus campus : List.of(hanoi, hcm, danang, cantho)) {
//                    Offering o = new Offering();
//                    o.setYear(2025);
//                    o.setTarget(rand.nextInt(151) + 100); // 100 - 250
//                    o.setPrice(24000000 + rand.nextInt(4) * 3000000); // 24tr - 33tr
//                    o.setCampus(campus);
//                    o.setSpecialization(spec);
//                    offerings.add(o);
//                }
//            }
//            offeringRepo.saveAll(offerings);
//        };
//    }
//}

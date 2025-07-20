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
    public CommandLineRunner initDataProgram(CampusRepository campusRepo, MajorRepository majorRepo, OfferingRepository offeringRepo, SpecializationRepository specializationRepo) {
        return args -> {
            if (campusRepo.count() == 0) {
                // ======= CAMPUS =======
                Campus hanoi = new Campus(null, "Đại học FPT Hà Nội", "Khu Công nghệ cao Hòa Lạc, Thạch Thất, Hà Nội", "Cơ sở chính", "hanoi@fpt.edu.vn", "02473001866", null, new ArrayList<>());

                Campus hcm = new Campus(null, "Đại học FPT TP.HCM", "Lô E2a-7, Đường D1, Khu Công nghệ cao, TP Thủ Đức, TP.HCM", "Cơ sở TP.HCM", "hcmc@fpt.edu.vn", "02873001866", null, new ArrayList<>());

                Campus danang = new Campus(null, "Đại học FPT Đà Nẵng", "Khu đô thị công nghệ FPT City, Ngũ Hành Sơn, Đà Nẵng", "Cơ sở Đà Nẵng", "danang@fpt.edu.vn", "023673001866", null, new ArrayList<>());

                Campus cantho = new Campus(null, "Đại học FPT Cần Thơ", "Khu đô thị Nam Cần Thơ, Quận Cái Răng, TP Cần Thơ", "Cơ sở Cần Thơ", "cantho@fpt.edu.vn", "029273001866", null, new ArrayList<>());

                campusRepo.saveAll(List.of(hanoi, hcm, danang, cantho));

                // ======= MAJOR =======
                Major it = new Major(null, "Công nghệ thông tin", "Information Technology", new ArrayList<>());
                Major biz = new Major(null, "Quản trị kinh doanh", "Business Administration", new ArrayList<>());
                Major media = new Major(null, "Công nghệ truyền thông", "Media Technology", new ArrayList<>());
                Major en = new Major(null, "Ngôn ngữ Anh", "English Language", new ArrayList<>());
                Major zh = new Major(null, "Ngôn ngữ Trung Quốc", "Chinese Language", new ArrayList<>());
                Major jp = new Major(null, "Ngôn ngữ Nhật", "Japanese Language", new ArrayList<>());
                Major kr = new Major(null, "Ngôn ngữ Hàn Quốc", "Korean Language", new ArrayList<>());
                Major law = new Major(null, "Luật", "Law", new ArrayList<>());

                majorRepo.saveAll(List.of(it, biz, media, en, zh, jp, kr, law));


                // ======= SPECIALIZATION =======
                List<Specialization> specs = new ArrayList<>();

                specs.add(new Specialization(null, "An toàn thông tin", "Information Security", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Công nghệ ô tô số", "Digital Automotive Engineering", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Kỹ thuật phần mềm", "Software Engineering", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Chuyển đổi số", "Digital Transformation", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Thiết kế mỹ thuật số", "Digital Art & Design", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Thiết kế vi mạch bán dẫn", "Semiconductor & IC Design", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Trí tuệ nhân tạo", "Artificial Intelligence", it, new ArrayList<>()));

                specs.add(new Specialization(null, "Công nghệ tài chính", "Financial Technology", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Digital Marketing", "Digital Marketing", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Kinh doanh quốc tế", "International Business", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Logistics & quản lý chuỗi cung ứng toàn cầu", "Logistics & Global Supply Chain", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Quản trị khách sạn", "Hotel Management", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Quản trị dịch vụ du lịch & lữ hành", "Travel & Tourism Management", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Tài chính doanh nghiệp", "Corporate Finance", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Ngân hàng số - Tài chính", "Digital Banking and Finance", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Tài chính đầu tư", "Investment Finance", biz, new ArrayList<>()));

                specs.add(new Specialization(null, "Quan hệ công chúng", "Public Relations", media, new ArrayList<>()));
                specs.add(new Specialization(null, "Truyền thông đa phương tiện", "Multimedia Communications", media, new ArrayList<>()));

                specs.add(new Specialization(null, "Ngôn ngữ Anh", "English Language", en, new ArrayList<>()));
                specs.add(new Specialization(null, "Song ngữ Trung - Anh", "Chinese - English Bilingual", zh, new ArrayList<>()));
                specs.add(new Specialization(null, "Song ngữ Nhật - Anh", "Japanese - English Bilingual", jp, new ArrayList<>()));
                specs.add(new Specialization(null, "Song ngữ Hàn - Anh", "Korean - English Bilingual", kr, new ArrayList<>()));

                specs.add(new Specialization(null, "Luật kinh tế", "Economic Law", law, new ArrayList<>()));
                specs.add(new Specialization(null, "Luật thương mại quốc tế", "International Trade Law", law, new ArrayList<>()));

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

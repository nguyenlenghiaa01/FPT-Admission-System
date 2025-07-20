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
                Major it = new Major(null, "Information Technology", "Công nghệ thông tin", new ArrayList<>());
                Major biz = new Major(null, "Business Administration", "Quản trị kinh doanh", new ArrayList<>());
                Major media = new Major(null, "Media Technology", "Công nghệ truyền thông", new ArrayList<>());
                Major en = new Major(null, "English Language", "Ngôn ngữ Anh", new ArrayList<>());
                Major zh = new Major(null, "Chinese Language", "Ngôn ngữ Trung Quốc", new ArrayList<>());
                Major jp = new Major(null, "Japanese Language", "Ngôn ngữ Nhật", new ArrayList<>());
                Major kr = new Major(null, "Korean Language", "Ngôn ngữ Hàn Quốc", new ArrayList<>());
                Major law = new Major(null, "Law", "Luật", new ArrayList<>());

                majorRepo.saveAll(List.of(it, biz, media, en, zh, jp, kr, law));


                // ======= SPECIALIZATION =======
                List<Specialization> specs = new ArrayList<>();

                specs.add(new Specialization(null, "Information Security", "An toàn thông tin", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Digital Automotive Engineering", "Công nghệ ô tô số", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Software Engineering", "Kỹ thuật phần mềm", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Digital Transformation", "Chuyển đổi số", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Digital Art & Design", "Thiết kế mỹ thuật số", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Semiconductor & IC Design", "Thiết kế vi mạch bán dẫn", it, new ArrayList<>()));
                specs.add(new Specialization(null, "Artificial Intelligence", "Trí tuệ nhân tạo", it, new ArrayList<>()));

                specs.add(new Specialization(null, "Financial Technology", "Công nghệ tài chính", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Digital Marketing", "Digital Marketing", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "International Business", "Kinh doanh quốc tế", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Logistics & Global Supply Chain", "Logistics & quản lý chuỗi cung ứng toàn cầu", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Hotel Management", "Quản trị khách sạn", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Travel & Tourism Management", "Quản trị dịch vụ du lịch & lữ hành", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Corporate Finance", "Tài chính doanh nghiệp", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Digital Banking and Finance", "Ngân hàng số - Tài chính", biz, new ArrayList<>()));
                specs.add(new Specialization(null, "Investment Finance", "Tài chính đầu tư", biz, new ArrayList<>()));

                specs.add(new Specialization(null, "Public Relations", "Quan hệ công chúng", media, new ArrayList<>()));
                specs.add(new Specialization(null, "Multimedia Communications", "Truyền thông đa phương tiện", media, new ArrayList<>()));

                specs.add(new Specialization(null, "English Language", "Ngôn ngữ Anh", en, new ArrayList<>()));
                specs.add(new Specialization(null, "Chinese - English Bilingual", "Song ngữ Trung - Anh", zh, new ArrayList<>()));
                specs.add(new Specialization(null, "Japanese - English Bilingual", "Song ngữ Nhật - Anh", jp, new ArrayList<>()));
                specs.add(new Specialization(null, "Korean - English Bilingual", "Song ngữ Hàn - Anh", kr, new ArrayList<>()));

                specs.add(new Specialization(null, "Economic Law", "Luật kinh tế", law, new ArrayList<>()));
                specs.add(new Specialization(null, "International Trade Law", "Luật thương mại quốc tế", law, new ArrayList<>()));

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

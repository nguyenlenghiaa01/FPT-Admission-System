package com.fptu.hk7.blogservice.config;

import com.fptu.hk7.blogservice.pojo.Category;
import com.fptu.hk7.blogservice.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataInitial {
    @Bean
    public CommandLineRunner commandLineRunner(CategoryRepository categoryRepository) {
        return args -> {
            if(categoryRepository.count() == 0) {
                List<Category> list = new ArrayList<>();
                list.add(new Category("tin tức chung"));
                list.add(new Category("báo chí nói về fpt"));
                list.add(new Category("trách nhiệm cộng đồng"));
                categoryRepository.saveAll(list);
            }
        };
    }
}

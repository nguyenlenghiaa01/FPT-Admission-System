package com.fptu.hk7.blogservice.service;

import com.fptu.hk7.blogservice.pojo.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll();
    Category findById(UUID id);
    Category save(Category category);
    Category update(UUID id, Category category);
    void deleteById(UUID id);
}

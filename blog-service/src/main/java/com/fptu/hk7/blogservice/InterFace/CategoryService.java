package com.fptu.hk7.blogservice.InterFace;

import com.fptu.hk7.blogservice.pojo.Category;
import com.fptu.hk7.blogservice.pojo.Post;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> findAll();
    Category findById(UUID id);
    Category save(Category category);
    Category update(UUID id, Category category);
    void deleteById(UUID id);
}

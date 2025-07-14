package com.fptu.hk7.blogservice.controller;

import com.fptu.hk7.blogservice.dto.Request.CategoryRequest;
import com.fptu.hk7.blogservice.pojo.Category;
import com.fptu.hk7.blogservice.InterFace.CategoryService;
import com.fptu.hk7.blogservice.service.CategoryServiceImp;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@SecurityRequirement(name = "api")
public class CategoryController {
    private final CategoryServiceImp categoryService;

    public CategoryController(CategoryServiceImp categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Category> getAll() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable("id") UUID id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok().body(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Category create(@RequestBody CategoryRequest categoryRequest) {
        Category category = new Category(categoryRequest.getName());
        return categoryService.save(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable("id") UUID id, @RequestBody Category category) {
        try {
            return ResponseEntity.ok(categoryService.update(id, category));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

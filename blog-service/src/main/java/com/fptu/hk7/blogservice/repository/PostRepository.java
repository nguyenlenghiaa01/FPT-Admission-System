package com.fptu.hk7.blogservice.repository;

import com.fptu.hk7.blogservice.pojo.Category;
import com.fptu.hk7.blogservice.pojo.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    Page<Post> findAllByCategory(Category category, Pageable pageable);
}

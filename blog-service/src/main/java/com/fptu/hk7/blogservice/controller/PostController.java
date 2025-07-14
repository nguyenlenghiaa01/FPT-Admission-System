package com.fptu.hk7.blogservice.controller;

import com.fptu.hk7.blogservice.dto.PostResponse;
import com.fptu.hk7.blogservice.dto.Request.PostRequest;
import com.fptu.hk7.blogservice.pojo.Category;
import com.fptu.hk7.blogservice.pojo.Post;
import com.fptu.hk7.blogservice.InterFace.CategoryService;
import com.fptu.hk7.blogservice.InterFace.PostService;
import com.fptu.hk7.blogservice.service.CategoryServiceImp;
import com.fptu.hk7.blogservice.service.PostServiceImp;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@SecurityRequirement(name = "api")
@RequiredArgsConstructor
public class PostController {
    private final PostServiceImp postService;

    private final CategoryServiceImp categoryService;

    ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public List<PostResponse> getAll() {
        List<Post> posts = postService.findAll();
        return posts.stream().map(post -> {
            PostResponse postResponse = modelMapper.map(post, PostResponse.class);
            postResponse.setCategory_name(post.getCategory().getName());
            return postResponse;
        }).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getById(@PathVariable UUID id) {
        Post post = postService.findById(id);
        return ResponseEntity.ok(post);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public Post create(@RequestBody PostRequest postRequest) {
        Post post = modelMapper.map(postRequest, Post.class);
        Category category = categoryService.findById(UUID.fromString(postRequest.getCategoryId()));
        post.setCategory(category);
        post.setView(0);
        return postService.save(post);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable("id") String id, @Validated @RequestBody PostRequest postRequest) {
        try {
            return ResponseEntity.ok(postService.update(UUID.fromString(id), postRequest));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @PutMapping("/read/{id}")
//    public void readPost(@PathVariable("id") String postId) {
//        postService.readPost(UUID.fromString(postId));
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter/{id}")
    public ResponseEntity<?> filterByCategoryId(@PathVariable("id") UUID id, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(postService.filter(id, page, size));
    }
}

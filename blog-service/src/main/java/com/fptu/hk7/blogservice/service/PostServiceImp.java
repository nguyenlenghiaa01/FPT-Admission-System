package com.fptu.hk7.blogservice.service;

import com.fptu.hk7.blogservice.InterFace.PostService;
import com.fptu.hk7.blogservice.dto.Request.PostRequest;
import com.fptu.hk7.blogservice.pojo.Category;
import com.fptu.hk7.blogservice.pojo.Post;
import com.fptu.hk7.blogservice.repository.CategoryRepository;
import com.fptu.hk7.blogservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImp implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Post findById(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Can not find Post"));
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void deleteById(UUID id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post update(UUID id, PostRequest postRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setCategory(categoryRepository.findById(UUID.fromString(postRequest.getCategoryId())).orElseThrow(() -> new RuntimeException("Category not found")));
        post.setTopic(postRequest.getTopic());
        post.setDeltaContent(postRequest.getDeltaContent());
        post.setHtmlContent(postRequest.getHtmlContent());
        post.setThumbnail(postRequest.getThumbnail());
        postRepository.save(post);
        return post;
    }

    @Override
    public Page<Post> filter(UUID id, int page, int size) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        return postRepository.findAllByCategory(category, PageRequest.of(page, size));
    }
}

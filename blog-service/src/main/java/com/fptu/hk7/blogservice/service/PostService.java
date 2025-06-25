package com.fptu.hk7.blogservice.service;

import com.fptu.hk7.blogservice.dto.Request.PostRequest;
import com.fptu.hk7.blogservice.pojo.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> findAll();
    Post findById(UUID id);
    Post save(Post post);
    void deleteById(UUID id);
    Post update(UUID id, PostRequest postRequest);
}

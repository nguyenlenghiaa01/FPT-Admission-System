package com.fptu.hk7.blogservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PostResponse {
    private UUID id;

    private String topic;

    private String htmlContent;

    private String deltaContent;

    private String stamp;

    private int view;

    private String thumbnail;

    private String category_name;
}


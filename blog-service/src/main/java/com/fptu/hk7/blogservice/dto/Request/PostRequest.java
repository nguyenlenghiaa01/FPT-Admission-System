package com.fptu.hk7.blogservice.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "Title cannot be blank")
    private String topic;

    @NotBlank(message = "Title cannot be blank")
    private String htmlContent;

    @NotBlank(message = "Title cannot be blank")
    private String deltaContent;

    @NotBlank(message = "Category cannot be blank")
    private String categoryId;

    @NotBlank(message = "Thumbnail image cannot be blank")
    private String thumbnail;
}

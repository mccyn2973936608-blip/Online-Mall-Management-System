package com.mall.backend.dto;

import lombok.Data;

@Data
public class ProductQueryRequest {
    private Long categoryId;
    private String keyword;
    private String sort;
}

package com.mall.backend.dto;

import lombok.Data;

@Data
public class OrderQueryRequest {
    private String orderNo;
    private String username;
    private Integer status;
}

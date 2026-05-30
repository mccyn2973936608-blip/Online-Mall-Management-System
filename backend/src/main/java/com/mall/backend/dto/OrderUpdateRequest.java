package com.mall.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderUpdateRequest {
    private Long orderId;

    @NotBlank(message = "收货人不能为空")
    private String receiverName;

    @NotBlank(message = "收货电话不能为空")
    private String receiverPhone;

    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;

    private String remark;
}

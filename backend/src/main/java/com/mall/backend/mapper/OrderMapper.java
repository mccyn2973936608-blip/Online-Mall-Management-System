package com.mall.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.backend.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderEntity> {
}

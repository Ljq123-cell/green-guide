package com.greenguide.module.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.greenguide.module.learning.entity.UserPoints;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserPointsMapper extends BaseMapper<UserPoints> {
}

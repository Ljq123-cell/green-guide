package com.greenguide.module.garbage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.greenguide.module.garbage.entity.GarbageCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GarbageCategoryMapper extends BaseMapper<GarbageCategory> {
}

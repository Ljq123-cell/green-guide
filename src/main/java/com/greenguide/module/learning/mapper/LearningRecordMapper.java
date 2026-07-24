package com.greenguide.module.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.greenguide.module.learning.entity.LearningRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LearningRecordMapper extends BaseMapper<LearningRecord> {
}

package com.greenguide.module.quiz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.greenguide.module.quiz.entity.QuizQuestion;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuizQuestionMapper extends BaseMapper<QuizQuestion> {
}

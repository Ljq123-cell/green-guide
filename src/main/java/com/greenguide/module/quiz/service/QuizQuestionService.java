package com.greenguide.module.quiz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.module.quiz.entity.QuizQuestion;

import java.util.List;

public interface QuizQuestionService {

    Page<QuizQuestion> page(int page, int size, String difficulty, String categoryTag, String status);

    QuizQuestion getById(Long id);

    void create(QuizQuestion question);

    void update(QuizQuestion question);

    void delete(Long id);

    void publish(Long id);

    /** 随机获取已发布的题目 */
    List<QuizQuestion> randomQuestions(int count, String difficulty);
}

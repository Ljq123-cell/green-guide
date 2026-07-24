package com.greenguide.module.quiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.quiz.entity.QuizQuestion;
import com.greenguide.module.quiz.mapper.QuizQuestionMapper;
import com.greenguide.module.quiz.service.QuizQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizQuestionServiceImpl implements QuizQuestionService {

    private final QuizQuestionMapper mapper;

    @Override
    public Page<QuizQuestion> page(int page, int size, String difficulty, String categoryTag, String status) {
        LambdaQueryWrapper<QuizQuestion> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(difficulty)) {
            wrapper.eq(QuizQuestion::getDifficulty, difficulty);
        }
        if (StringUtils.hasText(categoryTag)) {
            wrapper.eq(QuizQuestion::getCategoryTag, categoryTag);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(QuizQuestion::getStatus, status);
        }
        wrapper.orderByDesc(QuizQuestion::getCreatedAt);
        return mapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public QuizQuestion getById(Long id) {
        QuizQuestion q = mapper.selectById(id);
        if (q == null) {
            throw new BusinessException(404, "题目不存在");
        }
        return q;
    }

    @Override
    public void create(QuizQuestion question) {
        question.setUsageCount(0);
        question.setCorrectCount(0);
        question.setStatus("DRAFT");
        mapper.insert(question);
    }

    @Override
    public void update(QuizQuestion question) {
        QuizQuestion existing = getById(question.getId());
        existing.setStem(question.getStem());
        existing.setQuestionType(question.getQuestionType());
        existing.setOptions(question.getOptions());
        existing.setCorrectAnswer(question.getCorrectAnswer());
        existing.setDifficulty(question.getDifficulty());
        existing.setExplanation(question.getExplanation());
        existing.setCategoryTag(question.getCategoryTag());
        mapper.updateById(existing);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        mapper.deleteById(id);
    }

    @Override
    public void publish(Long id) {
        QuizQuestion q = getById(id);
        q.setStatus("PUBLISHED");
        mapper.updateById(q);
    }

    @Override
    public List<QuizQuestion> randomQuestions(int count, String difficulty) {
        // 先用普通查询拿到候选集，再在内存中随机
        LambdaQueryWrapper<QuizQuestion> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(QuizQuestion::getStatus, "PUBLISHED");
        if (StringUtils.hasText(difficulty)) {
            wrapper.eq(QuizQuestion::getDifficulty, difficulty);
        }
        wrapper.last("ORDER BY RAND() LIMIT " + Math.min(count, 50));
        return mapper.selectList(wrapper);
    }
}

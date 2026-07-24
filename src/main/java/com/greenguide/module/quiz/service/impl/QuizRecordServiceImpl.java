package com.greenguide.module.quiz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.quiz.entity.QuizQuestion;
import com.greenguide.module.quiz.entity.QuizRecord;
import com.greenguide.module.quiz.mapper.QuizQuestionMapper;
import com.greenguide.module.quiz.mapper.QuizRecordMapper;
import com.greenguide.module.quiz.service.QuizRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuizRecordServiceImpl implements QuizRecordService {

    private final QuizQuestionMapper questionMapper;
    private final QuizRecordMapper recordMapper;

    @Override
    @Transactional
    public Map<String, Object> submitAnswers(Long userId, Map<Long, String> answers) {
        int total = answers.size();
        int correct = 0;
        List<Map<String, Object>> details = new ArrayList<>();

        for (Map.Entry<Long, String> entry : answers.entrySet()) {
            Long questionId = entry.getKey();
            String userAnswer = entry.getValue();

            QuizQuestion question = questionMapper.selectById(questionId);
            if (question == null) {
                continue;
            }

            boolean isCorrect = userAnswer != null
                    && userAnswer.trim().equalsIgnoreCase(question.getCorrectAnswer().trim());

            if (isCorrect) correct++;

            // 保存答题记录
            QuizRecord record = new QuizRecord();
            record.setUserId(userId);
            record.setQuestionId(questionId);
            record.setUserAnswer(userAnswer);
            record.setIsCorrect(isCorrect ? 1 : 0);
            recordMapper.insert(record);

            // 更新题目统计
            question.setUsageCount(
                    Optional.ofNullable(question.getUsageCount()).orElse(0) + 1);
            if (isCorrect) {
                question.setCorrectCount(
                        Optional.ofNullable(question.getCorrectCount()).orElse(0) + 1);
            }
            questionMapper.updateById(question);

            // 详情
            Map<String, Object> detail = new HashMap<>();
            detail.put("questionId", questionId);
            detail.put("stem", question.getStem());
            detail.put("yourAnswer", userAnswer);
            detail.put("correctAnswer", question.getCorrectAnswer());
            detail.put("isCorrect", isCorrect);
            detail.put("explanation", question.getExplanation());
            details.add(detail);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("correct", correct);
        result.put("score", total > 0 ? Math.round(correct * 100.0 / total) : 0);
        result.put("details", details);
        return result;
    }

    @Override
    public Map<String, Object> userStats(Long userId) {
        Long total = recordMapper.selectCount(
                new LambdaQueryWrapper<QuizRecord>().eq(QuizRecord::getUserId, userId));
        Long correctCount = recordMapper.selectCount(
                new LambdaQueryWrapper<QuizRecord>()
                        .eq(QuizRecord::getUserId, userId)
                        .eq(QuizRecord::getIsCorrect, 1));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAnswers", total);
        stats.put("correctAnswers", correctCount);
        stats.put("accuracy", total > 0 ? Math.round(correctCount * 100.0 / total) : 0);
        return stats;
    }
}

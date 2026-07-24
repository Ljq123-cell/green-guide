package com.greenguide.module.quiz.service;

import java.util.Map;

public interface QuizRecordService {

    /** 提交答案，返回每道题的对错和总分 */
    Map<String, Object> submitAnswers(Long userId, Map<Long, String> answers);

    /** 查看某用户的答题统计 */
    Map<String, Object> userStats(Long userId);
}

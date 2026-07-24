package com.greenguide.module.learning.service;

import java.util.Map;

public interface LearningService {

    Map<String, Object> getUserStats(Long userId);

    void recordSearch(Long userId, String garbageName, String category);
}

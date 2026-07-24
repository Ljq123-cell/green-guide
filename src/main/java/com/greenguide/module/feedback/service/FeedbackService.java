package com.greenguide.module.feedback.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.module.feedback.entity.Feedback;

public interface FeedbackService {

    Page<Feedback> page(int page, int size, String feedbackType, String status);

    Feedback getById(Long id);

    void process(Long id, Long handlerId, String handlerNote, String status);

    /** 用户提交反馈 */
    void submit(Feedback feedback);
}

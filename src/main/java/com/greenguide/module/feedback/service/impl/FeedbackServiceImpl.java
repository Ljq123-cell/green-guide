package com.greenguide.module.feedback.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.feedback.entity.Feedback;
import com.greenguide.module.feedback.mapper.FeedbackMapper;
import com.greenguide.module.feedback.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackMapper mapper;

    @Override
    public Page<Feedback> page(int page, int size, String feedbackType, String status) {
        LambdaQueryWrapper<Feedback> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(feedbackType)) {
            wrapper.eq(Feedback::getFeedbackType, feedbackType);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(Feedback::getStatus, status);
        }
        wrapper.orderByDesc(Feedback::getCreatedAt);
        return mapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public Feedback getById(Long id) {
        Feedback f = mapper.selectById(id);
        if (f == null) {
            throw new BusinessException(404, "反馈不存在");
        }
        return f;
    }

    @Override
    public void process(Long id, Long handlerId, String handlerNote, String status) {
        Feedback f = getById(id);
        f.setHandlerId(handlerId);
        f.setHandlerNote(handlerNote);
        f.setStatus(status);
        f.setProcessedAt(LocalDateTime.now());
        mapper.updateById(f);
    }

    @Override
    public void submit(Feedback feedback) {
        feedback.setStatus("PENDING");
        mapper.insert(feedback);
    }
}

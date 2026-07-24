package com.greenguide.module.knowledge.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.knowledge.entity.KnowledgeArticle;
import com.greenguide.module.knowledge.mapper.KnowledgeArticleMapper;
import com.greenguide.module.knowledge.service.KnowledgeArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KnowledgeArticleServiceImpl implements KnowledgeArticleService {

    private final KnowledgeArticleMapper mapper;

    @Override
    public Page<KnowledgeArticle> page(int page, int size, String category, String status, String keyword) {
        LambdaQueryWrapper<KnowledgeArticle> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(category)) {
            wrapper.eq(KnowledgeArticle::getCategory, category);
        }
        if (StringUtils.hasText(status)) {
            wrapper.eq(KnowledgeArticle::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(KnowledgeArticle::getTitle, keyword)
                    .or().like(KnowledgeArticle::getSummary, keyword));
        }
        wrapper.orderByDesc(KnowledgeArticle::getCreatedAt);
        return mapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public KnowledgeArticle getById(Long id) {
        KnowledgeArticle article = mapper.selectById(id);
        if (article == null) {
            throw new BusinessException(404, "文章不存在");
        }
        return article;
    }

    @Override
    public void create(KnowledgeArticle article) {
        article.setViewCount(0);
        article.setStatus("DRAFT");
        mapper.insert(article);
    }

    @Override
    public void update(KnowledgeArticle article) {
        KnowledgeArticle existing = getById(article.getId());
        existing.setTitle(article.getTitle());
        existing.setSummary(article.getSummary());
        existing.setContent(article.getContent());
        existing.setCategory(article.getCategory());
        existing.setCoverImage(article.getCoverImage());
        existing.setSource(article.getSource());
        existing.setTags(article.getTags());
        mapper.updateById(existing);
    }

    @Override
    public void delete(Long id) {
        getById(id);
        mapper.deleteById(id);
    }

    @Override
    public void publish(Long id) {
        KnowledgeArticle article = getById(id);
        article.setStatus("PUBLISHED");
        article.setPublishedAt(LocalDateTime.now());
        mapper.updateById(article);
    }

    @Override
    public void unpublish(Long id) {
        KnowledgeArticle article = getById(id);
        article.setStatus("DRAFT");
        article.setPublishedAt(null);
        mapper.updateById(article);
    }

    // ==================== 公开浏览 ====================

    @Override
    public Page<KnowledgeArticle> publicPage(int page, int size, String category, String keyword) {
        LambdaQueryWrapper<KnowledgeArticle> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(KnowledgeArticle::getStatus, "PUBLISHED");
        if (StringUtils.hasText(category)) {
            wrapper.eq(KnowledgeArticle::getCategory, category);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(KnowledgeArticle::getTitle, keyword)
                    .or().like(KnowledgeArticle::getSummary, keyword));
        }
        wrapper.orderByDesc(KnowledgeArticle::getPublishedAt);
        return mapper.selectPage(new Page<>(page, size), wrapper);
    }

    @Override
    public KnowledgeArticle publicGetById(Long id) {
        KnowledgeArticle article = mapper.selectById(id);
        if (article == null || !"PUBLISHED".equals(article.getStatus())) {
            throw new BusinessException(404, "文章不存在");
        }
        // 浏览量 +1
        article.setViewCount(article.getViewCount() + 1);
        mapper.updateById(article);
        return article;
    }
}

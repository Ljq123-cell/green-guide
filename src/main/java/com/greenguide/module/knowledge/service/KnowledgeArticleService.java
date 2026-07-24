package com.greenguide.module.knowledge.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.greenguide.module.knowledge.entity.KnowledgeArticle;

public interface KnowledgeArticleService {

    // === 管理后台 ===
    Page<KnowledgeArticle> page(int page, int size, String category, String status, String keyword);

    KnowledgeArticle getById(Long id);

    void create(KnowledgeArticle article);

    void update(KnowledgeArticle article);

    void delete(Long id);

    void publish(Long id);

    void unpublish(Long id);

    // === 公开浏览 ===
    /** 公开文章列表（仅已发布） */
    Page<KnowledgeArticle> publicPage(int page, int size, String category, String keyword);

    /** 公开文章详情（增加浏览量） */
    KnowledgeArticle publicGetById(Long id);
}

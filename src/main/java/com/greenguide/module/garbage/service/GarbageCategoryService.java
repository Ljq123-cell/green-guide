package com.greenguide.module.garbage.service;

import com.greenguide.module.garbage.entity.GarbageCategory;

import java.util.List;

public interface GarbageCategoryService {

    /** 关键词搜索垃圾名称 */
    List<GarbageCategory> search(String keyword);

    /** 按类别筛选 */
    List<GarbageCategory> listByCategory(String category);

    /** 详情 */
    GarbageCategory getById(Long id);

    /** 全部分类列表 */
    List<GarbageCategory> listAll();
}

package com.greenguide.module.garbage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.greenguide.common.exception.BusinessException;
import com.greenguide.module.garbage.entity.GarbageCategory;
import com.greenguide.module.garbage.mapper.GarbageCategoryMapper;
import com.greenguide.module.garbage.service.GarbageCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GarbageCategoryServiceImpl implements GarbageCategoryService {

    private final GarbageCategoryMapper mapper;

    @Override
    public List<GarbageCategory> search(String keyword) {
        LambdaQueryWrapper<GarbageCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GarbageCategory::getIsActive, 1);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(GarbageCategory::getName, keyword)
                    .or().like(GarbageCategory::getDescription, keyword));
        }
        wrapper.orderByAsc(GarbageCategory::getCategory);
        return mapper.selectList(wrapper);
    }

    @Override
    public List<GarbageCategory> listByCategory(String category) {
        LambdaQueryWrapper<GarbageCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GarbageCategory::getIsActive, 1);
        if (StringUtils.hasText(category)) {
            wrapper.eq(GarbageCategory::getCategory, category);
        }
        wrapper.orderByAsc(GarbageCategory::getName);
        return mapper.selectList(wrapper);
    }

    @Override
    public GarbageCategory getById(Long id) {
        GarbageCategory item = mapper.selectById(id);
        if (item == null) {
            throw new BusinessException(404, "垃圾分类条目不存在");
        }
        return item;
    }

    @Override
    public List<GarbageCategory> listAll() {
        return mapper.selectList(
                new LambdaQueryWrapper<GarbageCategory>()
                        .eq(GarbageCategory::getIsActive, 1)
                        .orderByAsc(GarbageCategory::getCategory));
    }
}

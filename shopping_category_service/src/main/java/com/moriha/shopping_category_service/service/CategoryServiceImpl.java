package com.moriha.shopping_category_service.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Category;
import com.moriha.shopping_category_service.mapper.CategoryMapper;
import com.moriha.common.service.CategoryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

@DubboService
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void add(Category category) {
        categoryMapper.insert(category);
        refreshRedisCategory();
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateById(category);
        refreshRedisCategory();
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        categoryMapper.updateStatus(id, status);
        refreshRedisCategory();
    }

    @Override
    public void delete(Long[] ids) {
        categoryMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public Category findById(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public Page<Category> search(int page, int size) {
        return categoryMapper.selectPage(new Page<>(page, size), null);
    }

    @Override
    public List<Category> findAll() {
        //1. 从redis中获取缓存数据
        ListOperations<String,Category> listOperations = redisTemplate.opsForList();
        List<Category> categories = listOperations.range("categories", 0, -1);
        if(categories != null && categories.size() > 0){
            //2. 查到结果，直接返回
            return categories;
        }else{
            //3. 没有查到，就从数据库中查询，并同步到redis中
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1);
            List<Category> categoryList = categoryMapper.selectList(queryWrapper);
            listOperations.leftPushAll("categories", categoryList);
            return categoryList;
        }
    }

    /**
     * 更新redis中的广告数据
     */
    public void refreshRedisCategory() {
        // 从数据库查询广告
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        List<Category> categoryList = categoryMapper.selectList(queryWrapper);
        // 删除redis中的原有广告数据
        redisTemplate.delete("categories");
        // 将新的广告数据同步到redis中
        ListOperations<String,Category> listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll("categories", categoryList);
    }
}

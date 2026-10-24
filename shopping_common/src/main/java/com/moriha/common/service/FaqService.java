package com.moriha.common.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Faq;
/*
 * FAQ服务接口
 * 提供FAQ问答相关的业务功能
 */
public interface FaqService {
    /**
     * 分页查询FAQ答案
     * @param page 页码
     * @param size 每页大小
     * @param categoryId 分类ID，按分类去查询
     * @return FAQ答案
     */
    Page<Faq> getFaqPage(int page, int size,Integer categoryId);

    /**
     * 根据ID查询FAQ答案
     * @param id FAQ答案ID
     * @return FAQ答案实体
     */
    Faq getFaqById(String id);

    /**
     * 创建FAQ答案
     *
     * @param faq FAQ答案实体
     * @return 创建的FAQ答案ID
     */
    String createFaq(Faq faq);

    /**
     * 更新FAQ答案
     *
     * @param faq FAQ答案实体
     */
    void updateFaq(Faq faq);

    /**
     * 删除FAQ答案
     *
     * @param id FAQ答案ID
     */
    void deleteFaq(String id);

    /**
     * 匹配最佳答案
     * @param question 问题
     * @return 最佳答案。如果没有答案返回 null
     */
    Faq findBestAnswer(String question);
}
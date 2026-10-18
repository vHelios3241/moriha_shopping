package com.moriha.shopping_custcare_service.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.Faq;
import com.moriha.common.service.FaqService;
import com.moriha.shopping_custcare_service.mapper.FaqMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/*
 * FAQ服务实现类
 */
@DubboService
@Service
public class FaqServiceImpl implements FaqService {

    @Autowired
    private FaqMapper faqMapper;
    // Spring AI提供的操作向量数据库的工具
    @Autowired
    private VectorStore vectorStore;

    @Override
    public Page<Faq> getFaqPage(int page, int size, Integer categoryId) {
        return null;
    }

    @Override
    public Faq getFaqById(String id) {
        return null;
    }

    /**
     * 创建 FAQ 答案
     *
     * @param faq FAQ答案实体
     * @return 创建的FAQ答案ID
     */
    @Override
    public String createFaq(Faq faq) {
        // 保存FAQ答案到数据库
        // 手动生成UUID
        if (faq.getId() == null) {
            faq.setId(UUID.randomUUID().toString());
        }
        if (faq.getStatus() == null) {
            faq.setStatus(1);
        }
        if (faq.getUseCount() == null) {
            faq.setUseCount(0);
        }
        faqMapper.insert(faq);

        // 同步到向量数据库
        if (faq.getStatus() == 1) {
            insertFaqToQdrant(faq);
        }
        return faq.getId();
    }

    public void insertFaqToQdrant(Faq faq) {
        // 构建元数据
        HashMap<String, Object> metadata = new HashMap<>();
        metadata.put("faq_id", faq.getId());
        metadata.put("category_id", faq.getCategoryId());
        metadata.put("question", faq.getQuestion());
        metadata.put("answer", faq.getAnswer());

        // 构建Document
        Document document = new Document(faq.getId(), faq.getAnswer(), metadata);
        ArrayList<Document> doc = new ArrayList<>();
        doc.add(document);

        // 添加到向量数据库
        vectorStore.add(doc);
    }

    /**
     * 更新 FAQ 答案
     *
     * @param faq FAQ答案实体
     */
    @Override
    public void updateFaq(Faq faq) {
        // 更新数据库
        faqMapper.updateById(faq);
        // 同步数据到向量数据库
        if (faq.getStatus() != null && faq.getStatus() == 0) {
            // 删除旧文档
            deleteFaqVector(faq.getId());
        } else {
            // 删除旧文档，插入新文档
            deleteFaqVector(faq.getId());
            // 插入新文档
            if (faq.getStatus() == 1) {
                insertFaqToQdrant(faq);
            }
        }
    }


    /**
     * 从 Qdrant 删除 FAQ 向量
     */
    public void deleteFaqVector(String faqId){
        List<String> docs = new ArrayList<>();
        docs.add(faqId);
        vectorStore.delete(docs);
    }

    @Override
    public void deleteFaq(String id) {

    }
}




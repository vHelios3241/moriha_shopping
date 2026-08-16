package com.moriha.shopping_search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import com.moriha.common.pojo.*;
import com.moriha.common.service.SearchService;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@DubboService
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;


    /**
     * 分词
     * @param text     要拆分的原文
     * @param analyzer 选择的分词器
     * @return
     */
    @SneakyThrows
    public List<String> analyze(String text, String analyzer) {
        // 组装 ES 分词请求
        AnalyzeRequest request = AnalyzeRequest.of(a -> a.index("goods").analyzer(analyzer).text(text));
        // 发送网络请求到 ES 服务器
        AnalyzeResponse response = elasticsearchClient.indices().analyze(request);
        // 解析返回数据
        ArrayList<String> words = new ArrayList<>();
        List<AnalyzeToken> tokens = response.tokens();
        for (AnalyzeToken token : tokens) {
            words.add(token.token());
        }
        return words;
    }


    @Override
    public List<String> autoSuggest(String keyword) {
        return List.of();
    }

    @Override
    public GoodsSearchResult search(GoodsSearchParam goodsSearchParam) {
        return null;
    }

    /**
     * 同步商品到 ES
     *
     * @param goodsDesc 商品详情
     */
    @Override
    public void syncGoodsToES(GoodsDesc goodsDesc) {

    }


    @Override
    public void delete(Long id) {

    }
}

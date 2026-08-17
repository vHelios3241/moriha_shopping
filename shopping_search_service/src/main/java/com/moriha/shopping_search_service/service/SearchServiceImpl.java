package com.moriha.shopping_search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import com.moriha.common.pojo.*;
import com.moriha.common.service.SearchService;
import com.moriha.shopping_search_service.repository.GoodsESRepository;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DubboService
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private GoodsESRepository goodsESRepository;

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

    /**
     * 自动补齐关键字
     * @param keyword 被补齐的词
     * @return
     */
    @SneakyThrows
    @Override
    public List<String> autoSuggest(String keyword) {
        // 1. 组装补全请求条件
        Suggester suggester = Suggester.of(
                s -> s.suggesters("prefix_suggestion", FieldSuggester.of(
                        fs -> fs.completion(
                                c -> c.skipDuplicates(true)
                                        .size(10)
                                        .field("tags")
                        ).text(keyword)
                ))
        );
        // 2. 发送请求到 ES 服务器
        SearchResponse<Map> response = elasticsearchClient.search(
                s -> s.index("goods")
                        .suggest(suggester), Map.class  //把ES返回的数据转成Map
        );
        // 3. 解析 ES 返回的数据
        //取出补全联想模块的数据
        Map resultMap = response.suggest();
        //取出对应的补全结果集合
        List<Suggestion> suggestionList = (List)resultMap.get("prefix_suggestion");
        //取第 0 个，拿到整套匹配出来的联想结果
        Suggestion suggestion = suggestionList.get(0);
        //拿到所有联想候选对象
        List<CompletionSuggestOption> resultList = suggestion.completion().options();
        //循环提取词条，返回字符串
        List<String> result = new ArrayList<>();
        for (CompletionSuggestOption completionSuggestOption : resultList) {
            String text = completionSuggestOption.text();
            result.add(text);
        }
        return result;
    }

    /**
     * 搜索商品
     * @param goodsSearchParam 搜索条件
     * @return
     */
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
        // 将商品详情对象转为GoodsES对象
        GoodsES goodsES = new GoodsES();
        BeanUtils.copyProperties(goodsDesc,goodsES);
        goodsES.setBrand(goodsDesc.getBrand().getName());
        // 类目集合
        ArrayList<String> productType = new ArrayList<>();
        productType.add(goodsDesc.getProductType1().getName());
        productType.add(goodsDesc.getProductType2().getName());
        productType.add(goodsDesc.getProductType3().getName());
        goodsES.setProductType(productType);
        // 规格集合
        HashMap<String, List<String>> map = new HashMap<>();
        List<Specification> specifications = goodsDesc.getSpecifications();
        // 遍历规格集合
        for (Specification specification : specifications) {
            // 规格项集合
            List<SpecificationOption> options = specification.getSpecificationOptions();
            // 规格项名集合
            List<String> optionStrList = new ArrayList();
            for (SpecificationOption option : options) {
                optionStrList.add(option.getOptionName());
            }
            map.put(specification.getSpecName(), optionStrList);
        }
        goodsES.setSpecification(map);
        // 关键字
        List<String> tags = new ArrayList();
        tags.add(goodsDesc.getBrand().getName());  // 品牌名是关键字
        tags.addAll(analyze(goodsDesc.getGoodsName(), "ik_smart"));  // 商品名分词后为关键词
        tags.addAll(analyze(goodsDesc.getCaption(), "ik_smart"));  // 副标题分词后为关键词
        goodsES.setTags(tags);

        // 将GoodsES对象存入ES
        goodsESRepository.save(goodsES);
    }


    @Override
    public void delete(Long id) {

    }
}

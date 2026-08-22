package com.moriha.shopping_search_service.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggestOption;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.Suggester;
import co.elastic.clients.elasticsearch.core.search.Suggestion;
import co.elastic.clients.elasticsearch.indices.AnalyzeRequest;
import co.elastic.clients.elasticsearch.indices.AnalyzeResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.moriha.common.pojo.*;
import com.moriha.common.service.SearchService;
import com.moriha.shopping_search_service.repository.GoodsESRepository;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.print.DocFlavor;
import java.util.*;

@DubboService
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Autowired
    private GoodsESRepository goodsESRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

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


    /*
     * 搜索商品
     * @param goodsSearchParam 搜索条件
     * @return
     */
    @Override
    public GoodsSearchResult search(GoodsSearchParam goodsSearchParam) {
        // 1.构造ES搜索条件
        NativeQuery query = buildQuery(goodsSearchParam);
        // 2.搜索
        SearchHits<GoodsES> search = elasticsearchTemplate.search(query, GoodsES.class);
        // 3.将查询结果封装为Page对象
        // 3.1 将SearchHits转为List
        List<GoodsES> list = new ArrayList<>();
        for (SearchHit<GoodsES> goodsESSearchHit : search) {
            GoodsES content = goodsESSearchHit.getContent();
            list.add(content);
        }
        // 3.2 将List转为MP的Page对象
        Page<GoodsES> page = new Page<>();
        page.setCurrent(goodsSearchParam.getPage())  //当前页
                .setSize(goodsSearchParam.getSize()) //每页条数
                .setTotal(search.getTotalHits())     //总条数
                .setRecords(list);  //结果集
        // 4.封装结果对象
        // 4.1 查询结果
        GoodsSearchResult result = new GoodsSearchResult();
        // 当前页的商品数据 + 分页信息
        result.setGoodsPage(page);
        // 4.2 查询参数
        result.setGoodsSearchParam(goodsSearchParam);
        // 4.3 查询面板
        buildSearchPanel(goodsSearchParam, result);

        return result;
    }

    /*
     * 构造搜索条件
     * @param goodsSearchParam 查询条件对象
     * @return 搜索条件对象
     */
    public NativeQuery buildQuery(GoodsSearchParam goodsSearchParam) {
        // 1.创建复杂查询条件对象
        NativeQueryBuilder nativeQueryBuilder = new NativeQueryBuilder();
        BoolQuery.Builder builder = new BoolQuery.Builder();

        // 2.如果查询条件有关键词，关键词可以匹配商品名、副标题、品牌字段；否则查询所有商品
        String keyword = goodsSearchParam.getKeyword();
        if(!StringUtils.hasText(keyword)){
            MatchAllQuery matchAllQuery = new MatchAllQuery.Builder().build();
            builder.must(matchAllQuery._toQuery());
        }else{
            MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(q -> q
                    .query(keyword)
                    .fields("goodsName", "caption", "brand"));
            builder.must(multiMatchQuery._toQuery());
        }
        // 3.如果查询条件有品牌，则精准匹配品牌
        String brand = goodsSearchParam.getBrand();
        if(StringUtils.hasText(brand)){
            TermQuery termQuery = TermQuery.of(q -> q
                    .field("brand")
                    .value(brand));
            builder.must(termQuery._toQuery());
        }
        // 4.如果查询条件有价格，则匹配价格
        Double highPrice = goodsSearchParam.getHighPrice();
        Double lowPrice = goodsSearchParam.getLowPrice();
        if(highPrice != null && highPrice != 0){
            RangeQuery price = RangeQuery.of(q -> q
                    .field("price")
                    .lte(JsonData.of(highPrice)));
            builder.must(price._toQuery());
        }
        if(lowPrice != null && lowPrice != 0){
            RangeQuery price = RangeQuery.of(q -> q
                    .field("price")
                    .gte(JsonData.of(lowPrice)));
            builder.must(price._toQuery());
        }
        // 5.如果查询条件有规格项，则精准匹配规格项
        Map<String, String> specificationOption = goodsSearchParam.getSpecificationOption();
        if(specificationOption != null && !specificationOption.isEmpty()){
            Set<Map.Entry<String, String>> entrySet = specificationOption.entrySet();
            for (Map.Entry<String, String> es : entrySet) {
                String key = es.getKey();
                String value = es.getValue();
                TermQuery termQuery = TermQuery.of( q -> q
                        .field("specification." + key + ".keyword")
                        .value(value));
                builder.must(termQuery._toQuery());
            }
        }
        nativeQueryBuilder.withQuery(builder.build()._toQuery());
        // 6.添加分页条件
        PageRequest pageRequest = PageRequest.of(goodsSearchParam.getPage() - 1, goodsSearchParam.getSize());
        nativeQueryBuilder.withPageable(pageRequest);
        // 7.如果查询条件有排序，则添加排序条件
        String sortFiled = goodsSearchParam.getSortFiled();  // 排序字段
        String sort = goodsSearchParam.getSort();  // 排序方式
        if (StringUtils.hasText(sortFiled) && StringUtils.hasText(sort)){
            Sort sortParam = null;  // 组装好的排序规则
            // 新品的正序是id的倒序
            if (sortFiled.equals("NEW")){
                if (sort.equals("ASC")){  // 升序
                    sortParam = Sort.by(Sort.Direction.DESC, "id"); // id从小到大 → 旧商品在前
                }
                if (sort.equals("DESC")){  // 降序
                    sortParam = Sort.by(Sort.Direction.ASC, "id"); // id从大到小 → 新商品在前
                }
            }
            // 价格的正序是price的正序
            if (sortFiled.equals("PRICE")){
                if (sort.equals("ASC")){
                    sortParam = Sort.by(Sort.Direction.ASC, "price");
                }
                if (sort.equals("DESC")){
                    sortParam = Sort.by(Sort.Direction.DESC, "price");
                }
            }
            nativeQueryBuilder.withSort(sortParam);
        }

        // 8.返回查询条件对象
        return nativeQueryBuilder.build();
    }

    /*
      封装查询面板，即根据查询条件，找到查询结果关联度前20名的商品进行封装
      @param goodsSearchParam
     * @param goodsSearchResult
     */
    public void buildSearchPanel(GoodsSearchParam goodsSearchParam, GoodsSearchResult goodsSearchResult){
        // 1.构造搜索条件
        goodsSearchParam.setPage(1);
        goodsSearchParam.setSize(20);
        goodsSearchParam.setSort(null);
        goodsSearchParam.setSortFiled(null);
        NativeQuery nativeQuery = buildQuery(goodsSearchParam);
        // 2.搜索
        SearchHits<GoodsES> search = elasticsearchTemplate.search(nativeQuery, GoodsES.class);
        // 3.将结果封装为List对象
        List<GoodsES> list = new ArrayList<>();
        for (SearchHit<GoodsES> goodsESSearchHit : search) {
            GoodsES content = goodsESSearchHit.getContent();
            list.add(content);
        }
        // 4.遍历集合，封装查询面板
        // 商品相关的品牌列表
        Set<String> brands = new HashSet<>();
        // 商品相关的类型列表
        Set<String> productType = new HashSet<>();
        // 商品相关的规格列表
        HashMap<String, Set<String>> specifications = new HashMap<>();
        for (GoodsES l : list) {
            // 获取品牌
            brands.add(l.getBrand());
            // 获取类型
            productType.addAll(l.getProductType());
            // 获取规格
            Map<String, List<String>> specification = l.getSpecification();
            Set<Map.Entry<String, List<String>>> entries = specification.entrySet();
            for (Map.Entry<String, List<String>> entry : entries) {
                String key = entry.getKey();
                List<String> values = entry.getValue();
                // 如果没有遍历出该规格，新增键值对，如果已经遍历出该规格，则向规格中添加规格项
                if(!specifications.containsKey(key)){
                    specifications.put(key, new HashSet(values)); // 新增键值对
                }
                specifications.get(key).addAll(values); // 向规格中添加规格项
            }
        }
        goodsSearchResult.setBrands(brands);
        goodsSearchResult.setProductType(productType);
        goodsSearchResult.setSpecifications(specifications);
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
        goodsESRepository.deleteById(id);
    }
}

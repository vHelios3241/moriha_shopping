package com.moriha.shopping_search_service.repository;

import com.moriha.common.pojo.GoodsES;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsESRepository extends ElasticsearchRepository<GoodsES,Long> {
}

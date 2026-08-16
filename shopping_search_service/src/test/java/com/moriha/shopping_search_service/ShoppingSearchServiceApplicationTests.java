package com.moriha.shopping_search_service;

import com.moriha.shopping_search_service.service.SearchServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ShoppingSearchServiceApplicationTests {
    @Autowired
    private SearchServiceImpl searchServiceimpl;

    @Test
    void contextLoads() {
        List<String> analyze = searchServiceimpl.analyze("我爱所有人", "ik_pinyin");
        System.out.println(analyze);
    }
}

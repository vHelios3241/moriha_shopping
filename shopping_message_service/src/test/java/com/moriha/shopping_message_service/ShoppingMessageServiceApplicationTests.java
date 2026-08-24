package com.moriha.shopping_message_service;

import com.moriha.common.result.BaseResult;
import com.moriha.common.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class ShoppingMessageServiceApplicationTests {

    @Autowired
    private MessageService messageService;
    @Test
    void contextLoads() {
        BaseResult baseResult = messageService.sendMessage("13045248697", "12345");
        System.out.println(baseResult);
    }
}

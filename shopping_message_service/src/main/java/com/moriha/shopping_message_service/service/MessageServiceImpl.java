package com.moriha.shopping_message_service.service;

import com.alibaba.nacos.shaded.com.google.gson.Gson;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dypnsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeRequest;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponse;
import com.aliyun.sdk.service.dypnsapi20170525.models.SendSmsVerifyCodeResponseBody;
import com.moriha.common.result.BaseResult;
import com.moriha.common.service.MessageService;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.SneakyThrows;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@DubboService
@Service
public class MessageServiceImpl implements MessageService {

    @Value("${aliyun.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.accessKeySecret}")
    private String accessKeySecret;

    @SneakyThrows
    @Override
    public BaseResult sendMessage(String phoneNumber, String code) {

        // Configure Credentials authentication information
        // DefaultCredentialProvider provider = DefaultCredentialProvider.builder().build();
        StaticCredentialProvider provider = StaticCredentialProvider.create(
                Credential.builder()
                        .accessKeyId(accessKeyId)
                        .accessKeySecret(accessKeySecret)
                        .build()
        );


        // Configure the Client
        try (AsyncClient client = AsyncClient.builder()
                .region("cn-shanghai") // Region ID
                .credentialsProvider(provider)
                // Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dypnsapi
                                .setEndpointOverride("dypnsapi.aliyuncs.com")
                )
                .build()) {

            // Parameter settings for API request
            SendSmsVerifyCodeRequest sendSmsVerifyCodeRequest = SendSmsVerifyCodeRequest.builder()
                    .signName("恒创联众")
                    .templateCode("100001")
                    .phoneNumber(phoneNumber)
                    .templateParam("{\"code\":\"##code##\",\"min\":\"5\"}")
                    // Request-level configuration rewrite, can set Http request parameters, etc.
                    // .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
                    .build();

            CompletableFuture<SendSmsVerifyCodeResponse> response = client.sendSmsVerifyCode(sendSmsVerifyCodeRequest);
            // Synchronously get the return value of the API request

            SendSmsVerifyCodeResponse resp = response.get();
            System.out.println(new Gson().toJson(resp));

            // Asynchronous processing of return values
            /*
             * response.thenAccept(resp -> {
                System.out.println(new Gson().toJson(resp));
            }).exceptionally(throwable -> { // Handling exceptions
                System.out.println(throwable.getMessage());
                return null;
            });*/

            SendSmsVerifyCodeResponseBody body = resp.getBody();
            if("OK".equals(body.getCode())){
                return new BaseResult(200, body.getCode(), body.getMessage());
            }else{
                return new BaseResult(500, body.getCode(), body.getMessage());
            }

        }
    }
}


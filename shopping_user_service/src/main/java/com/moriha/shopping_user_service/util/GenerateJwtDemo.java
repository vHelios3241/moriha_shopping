package com.moriha.shopping_user_service.util;

import org.jose4j.jwk.JsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

/*
 * 生成公钥和私钥
 */
public class GenerateJwtDemo {
    public static void main(String[] args) throws JoseException {
        RsaJsonWebKey rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
        final String publicKeyString = rsaJsonWebKey.toJson(JsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        final String privateKeyString = rsaJsonWebKey.toJson(JsonWebKey.OutputControlLevel.INCLUDE_PRIVATE);
        System.out.println(publicKeyString);
        System.out.println(privateKeyString);
    }
}

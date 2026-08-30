package com.moriha.shopping_user_service.util;

import lombok.SneakyThrows;
import org.jose4j.json.JsonUtil;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
  // 私钥
  public final static String PRIVATE_JSON = "{\"kty\":\"RSA\",\"n\":\"n5WbkHM17prgPdtW8QNk18UKhExXEqb--R16K_gHzKMNWykjTzoyt4b1UlcnTzZpSmZL4b9hkOfcRS5Orij85VMHn70UQS8mJ9-2GpjJcHHUGnDM28-KpyYBLcrg1LfqVTFBwEqEkVAzGMP8nXZnnJWXGt52g0bVWqj2r8HgQvnvyYaUwUsQR4Hf6v5pZ6jVpWC1gYFNFDT37xIFQIlXa1Qos7TsmD8bVXCJd8QwJhZJmXRLIyw0BwqVG8BXphHhgtSJAC4ZlTFSrxoItHwnqDqIcje7G5456t9GiQwzU_J-gpgBfA6UcG3y6jNea_TXNh3WQ1z9nRKK1j0qkGLQ_Q\",\"e\":\"AQAB\",\"d\":\"YqXYxDJz5EtY0uQDmni-naLwHVgTezQ_6T6Phx6Ls7QXTYLgkNkpAMosW_oEErNP4u3ZenMTnL-3Kgy2FnK_4gKxc80t-B1tOLjRmR9ZZmj29GFGQZrJ2wljMSt4UAZglMkeG__0ct9gtq5wOeY3plPSVImwOPZQbPqFg6AVQVDETZDemY-sxJztm74RwfUDvskYJBFjQJL-XCjfv8ORbn4dAPZx65fKuivw7grbw0AgH0IcDrWI82bzrs957WvSa9UPulfaX6siWMzeSz96dW69Y_NDlq1k1v9j8_Vpm0nmL9e1hnfQ0s3inLVc9yDEQxTkLPPC7ltZ2p5lfnhh\",\"p\":\"3LoJ5_ArjYcKI0w9oyYyC3_RNHkNRJkvskloGAqYOg7U_9PitgU95o1JAGbQoexhuK6_XQnI6_FGppsZlOQdumXiclEc1k2xpbAQLaa0Ea82rSqvJAkA7JZU6stPDJH6vrQWzFalK73T4vX3QiGZbgy_kEqpuT1l3Pfh1YQow90\",\"q\":\"uRY6eZdnDb7J6_vmGSmPbGXIYgLu7nvkHbc14gjT4zZh9_HltVkcXRxVXgh8ME9eM0v028s5EnFESpLUm4xUz5uvYwXEsiUWg1DWjNkEtplPkSbfSpLpXKuqvoAbMfnbbicWNCWHl_K2CM9H4Pan4D1uM8LCvK0h6PpPmm0Zf6E\",\"dp\":\"0UreJb0HrxUCueGK-G45Ocoi1ryJpigSn90XBOTW3wY9Va7V434o6bnCJM1YupDnQDUCYvfqOj73nPaoVRPQVHRTOc5Lp7DX56GAVakiSTp4f85fb63R4IPTVSced27b8YPVsj_eMhG3HhfYMi6ly0HvSqRhNrJPKi0pSEoeVUk\",\"dq\":\"eblZua_N8adBKvbejDvd_F8wGu1s7-EA1F4yjLZbi7mvUp5_APZbBg1lQ1N3QVphTIEuxwJvRWbCj_zi4G25NVRhU1Fu2-4yUShwAe-T_vby8jZLccMOwIiR4Xlk6Ug2fMot-xuXgmH4P_D6h8QQAhTEjjwPUIDYpV1BzwDjXSE\",\"qi\":\"LiiU1oArJnfhKqXsjjmiEYByFD4qJZw3uoDFvdPmofkSrG98xOwDWAJ6f5yLVSW54Us4h8vEQphFSoLTCBlcKeJ8QLE2Gppxs23tb67bWyzTNIwXmFIJHiNut9tDFFOqq8dXrctakO0kHtrXzExGmhemG539tmVAzEqf9Hfe4O8\"}";
  // 公钥
  public final static String PUBLIC_JSON = "{\"kty\":\"RSA\",\"n\":\"n5WbkHM17prgPdtW8QNk18UKhExXEqb--R16K_gHzKMNWykjTzoyt4b1UlcnTzZpSmZL4b9hkOfcRS5Orij85VMHn70UQS8mJ9-2GpjJcHHUGnDM28-KpyYBLcrg1LfqVTFBwEqEkVAzGMP8nXZnnJWXGt52g0bVWqj2r8HgQvnvyYaUwUsQR4Hf6v5pZ6jVpWC1gYFNFDT37xIFQIlXa1Qos7TsmD8bVXCJd8QwJhZJmXRLIyw0BwqVG8BXphHhgtSJAC4ZlTFSrxoItHwnqDqIcje7G5456t9GiQwzU_J-gpgBfA6UcG3y6jNea_TXNh3WQ1z9nRKK1j0qkGLQ_Q\",\"e\":\"AQAB\"}";


  /*
   * 生成token
   *
   * @param userId    用户id
   * @param username 用户名字
   * @return
   */
  @SneakyThrows
  public static String sign(Long userId, String username) {
    // 1、 创建jwtclaims  jwt内容载荷部分
    JwtClaims claims = new JwtClaims();
    // 是谁创建了令牌并且签署了它
    claims.setIssuer("itbaizhan");
    // 令牌将被发送给谁
    claims.setAudience("audience");
    // 失效时间长 （分钟）
    claims.setExpirationTimeMinutesInTheFuture(60 * 24);
    // 令牌唯一标识符
    claims.setGeneratedJwtId();
    // 当令牌被发布或者创建现在
    claims.setIssuedAtToNow();
    // 再次之前令牌无效
    claims.setNotBeforeMinutesInThePast(2);
    // 主题
    claims.setSubject("shopping");
    // 可以添加关于这个主题得声明属性
    claims.setClaim("userId", userId);
    claims.setClaim("username", username);


    // 2、签名
    JsonWebSignature jws = new JsonWebSignature();
    //赋值载荷
    jws.setPayload(claims.toJson());


    // 3、jwt使用私钥签署
    PrivateKey privateKey = new RsaJsonWebKey(JsonUtil.parseJson(PRIVATE_JSON)).getPrivateKey();
    jws.setKey(privateKey);


    // 4、设置关键 kid
    jws.setKeyIdHeaderValue("keyId");


    // 5、设置签名算法
    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
    // 6、生成jwt
    String jwt = jws.getCompactSerialization();
    return jwt;
   }




  /*
   * 解密token，获取token中的信息
   *
   * @param token
   */
  @SneakyThrows
  public static Map<String, Object> verify(String token){
    // 1、引入公钥
    PublicKey publicKey = new RsaJsonWebKey(JsonUtil.parseJson(PUBLIC_JSON)).getPublicKey();
    // 2、使用jwtcoonsumer  验证和处理jwt
    JwtConsumer jwtConsumer = new JwtConsumerBuilder()
         .setRequireExpirationTime() //过期时间
         .setAllowedClockSkewInSeconds(30) //允许在验证得时候留有一些余地 计算时钟偏差  秒
         .setRequireSubject() // 主题生命
         .setExpectedIssuer("itbaizhan") // jwt需要知道谁发布得 用来验证发布人
         .setExpectedAudience("audience") //jwt目的是谁 用来验证观众
         .setVerificationKey(publicKey) // 用公钥验证签名  验证密钥
         .setJwsAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.WHITELIST, AlgorithmIdentifiers.RSA_USING_SHA256))
         .build();
    // 3、验证jwt 并将其处理为 claims
    try {
      JwtClaims jwtClaims = jwtConsumer.processToClaims(token);
      return jwtClaims.getClaimsMap();
     }catch (Exception e){
      return new HashMap();
     }
   }




  public static void main(String[] args){
    // 生成
    String baizhan = sign(1001L, "baizhan");
    System.out.println(baizhan);


    Map<String, Object> stringObjectMap = verify(baizhan);
    System.out.println(stringObjectMap.get("userId"));
    System.out.println(stringObjectMap.get("username"));
   }
}

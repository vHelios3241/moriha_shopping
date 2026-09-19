package com.moriha.shopping_seckill_service.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLock {

    @Autowired
    private RedissonClient redissonClient;

    /*
     * 加锁
     * @param key 锁名（redis中的键）
     * @param expireTime 键或者说锁过期时间
     */
    public boolean lock(String key, long expireTime){
        // 拿到锁名(锁名为秒杀商品id)
        RLock lock = redissonClient.getLock("lock" + key);
        try {
            /**
             * 尝试获取锁，如果没有别人占用这把锁，则拿到锁
             * 参数代表redis过期时间，即如果没有主动释放锁，到期会自动释放，防止服务器出现问题后引发死锁
             */
            return lock.tryLock(expireTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // 没有拿到锁证明别人占用，中断当前线程
            Thread.currentThread().interrupt();
            return false;
        }
    }
    // 释放锁
    public void unlock(String key){
        // 拿到锁名
        RLock lock = redissonClient.getLock("lock"+key);
        // 如果锁被占用，释放锁
        if(lock != null && lock.isLocked()){
            lock.unlock();
        }
    }
}

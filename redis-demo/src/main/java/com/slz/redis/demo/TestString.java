package com.slz.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/20
 */

@Slf4j(topic = "c.TestString")
public class TestString {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("8.130.102.188", 6379)) {
            String set = jedis.set("k2", "v2");
            log.debug(set);
            String k2 = jedis.get("k2");
            log.debug(k2);
            String mset = jedis.mset("k3", "v3", "k4", "v4");
            log.debug(mset);
            List<String> mget = jedis.mget("k1", "k2", "k3");
            log.debug(mget.toString());
            String setex = jedis.setex("kk", 30, "vv");
            log.debug(setex);
            TimeUnit.SECONDS.sleep(5);
            long ttl = jedis.ttl("kk");
            log.debug(String.valueOf(ttl));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

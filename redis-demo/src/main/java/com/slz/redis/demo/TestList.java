package com.slz.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/20
 */

@Slf4j(topic = "c.TestList")
public class TestList {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("8.130.102.188", 6379)) {
            long lpush = jedis.lpush("list1", "a", "b", "c", "d", "e");
            log.debug(String.valueOf(lpush));
            List<String> list1 = jedis.lpop("list1", 2);
            log.debug(list1.toString());
            List<String> list11 = jedis.lrange("list1", 0, 1);
            log.debug(list11.toString());
        }
    }
}

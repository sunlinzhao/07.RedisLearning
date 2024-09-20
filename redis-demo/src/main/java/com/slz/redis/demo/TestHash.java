package com.slz.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/20
 */

@Slf4j(topic = "c.TestHash")
public class TestHash {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("8.130.102.188", 6379)) {
            long hset1 = jedis.hset("user:001", "name", "zhangsan");
            long hset2 = jedis.hset("user:001", "age", "22");
            long hset3 = jedis.hset("user:001", "gender", "female");
            log.debug("" + hset1 + " " + hset2 + " " + hset3);
            String name = jedis.hget("user:001", "name");
            Map<String, String> map = jedis.hgetAll("user:001");
            log.debug(map.toString());
            Map<String, String> m = new HashMap<>();
            m.put("name", "xiaoming");
            m.put("age", "25");
            long hset = jedis.hset("stu:001", m);
            log.debug(String.valueOf(hset));
        }
    }
}

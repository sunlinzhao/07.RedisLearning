package com.slz.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/19
 */

@Slf4j(topic = "c.TestConnection")
public class TestConnection {
    public static void main(String[] args) {
        String ping;
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            ping = jedis.ping();
        }
        log.debug(ping);
    }
}

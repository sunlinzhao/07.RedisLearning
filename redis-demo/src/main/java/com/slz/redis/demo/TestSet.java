package com.slz.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/20
 */
@Slf4j(topic = "c.TestSet")
public class TestSet {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("localhost", 6379)) {
            long sadd = jedis.sadd("myset", "a", "b", "a", "c", "d");
            log.debug(String.valueOf(sadd));
            Set<String> myset = jedis.smembers("myset");
            log.debug(myset.toString());
            long myset1 = jedis.scard("myset");
            log.debug(String.valueOf(myset1));
            long srem = jedis.srem("myset", "a");
            log.debug(String.valueOf(srem));
            String myset2 = jedis.spop("myset");
            log.debug(myset2);

            long sadd1 = jedis.sadd("myset2", "c", "c", "f", "g");
            log.debug(String.valueOf(sadd1));
            long sunionstore = jedis.sunionstore("union_set", "myset", "myset2");
            log.debug(String.valueOf(sunionstore));
        }
    }
}

package com.slz.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/20
 */
@Slf4j(topic = "c.TestZSet")
public class TestZSet {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("8.130.102.188", 6379);
        jedis.zadd("z1", 1, "a");
        jedis.zadd("z1", 2, "b");
        jedis.zadd("z1", 3, "c");

        Map<String, Double> m = new HashMap<>();
        m.put("c", 4.0);
        m.put("a",5.0);
        m.put("d",6.0);
        m.put("f", 7.0);
        jedis.zadd("z2",m);

        List<String> z2 = jedis.zrange("z2", 0, -1);
        log.debug(z2.toString());
        List<String> z21 = jedis.zrevrange("z2", 0, -1);
        log.debug(z21.toString());

        Set<String> zdiff1 = jedis.zdiff("z1", "z2");
        log.debug(zdiff1.toString());
        Set<String> zdiff2 = jedis.zdiff("z2", "z1");
        log.debug(zdiff2.toString());

        zdiff1.addAll(zdiff2);
        log.debug(zdiff1.toString());

        Long z22 = jedis.zrank("z2", "f"); // 返回索引位置
        log.debug(z22.toString());
    }
}

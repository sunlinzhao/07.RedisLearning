package com.slz.redis.demo;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/20
 */

@Slf4j(topic = "c.TestApp")
public class TestApp {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("8.130.102.188", 6379);
        send(jedis, "10010");
    }

    public static void send(Jedis jedis, String phone){
        String count_key = "v:" + phone + ":count"; // 当前手机号已发送次数
        String code_key = "v:" + phone + ":code"; // 当前手机号已收到的验证码
        String s = jedis.get(count_key); // 获取当前手机号已发送次数
        if(s==null){
            // 如果没有获取，即s==null，就表时该key不存在，第一次设置，有效时间为1天
            jedis.setex(count_key, 60*60*24, "1");
        } else if (Integer.parseInt(s)<3) { // 如果不到3次，可以为用户发送，每发送一次，记数增加1
            jedis.incr(count_key);

        } else {
            log.debug("今日已经请求3次，请24小时后再试");
            jedis.close();
            return;
        }
        String code = getCode();
        jedis.setex(code_key, 60*5, code); // 发送验证码，进行保存，设置过期时间
        log.debug("向" + phone + "发送成功", code);
        jedis.close();
    }

    public static String getCode(){
        String pattern = "0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(pattern.charAt((int)(Math.random()*pattern.length())));
        }
        return sb.toString();
//        return new DecimalFormat("000000").format(new Random().nextInt(100000));
    }
}

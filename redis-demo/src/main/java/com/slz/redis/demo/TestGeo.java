package com.slz.redis.demo;

/**
 * @author : SunLZ
 * @project : RedisLearning
 * @date : 2024/9/20
 */

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.args.GeoUnit;
import redis.clients.jedis.resps.GeoRadiusResponse;

import java.util.List;

@Slf4j(topic = "c.TestGeo")
public class TestGeo {
    public static void main(String[] args) {
        try (Jedis jedis = new Jedis("8.130.102.188", 6379)) {
            long loc1 = jedis.geoadd("geo:beijing", 116.417492, 39.911836, "gugong");
            long loc2 = jedis.geoadd("geo:beijing", 116.466935, 39.960963, "bridge");
            long loc3 = jedis.geoadd("geo:beijing", 116.216846, 39.91405, "mountain");

            Double geodist = jedis.geodist("geo:beijing", "gugong", "mountain", GeoUnit.KM);
            log.debug(geodist.toString());

            List<GeoRadiusResponse> georadius = jedis.georadius("geo:beijing", 116.417492, 39.911836, 2, GeoUnit.KM);
            georadius.forEach(g->{
                System.out.println(g.getMemberByString());
            });

            List<GeoCoordinate> geopos = jedis.geopos("geo:beijing", "gugong", "bridge", "mountain");
            geopos.forEach(g->{
                System.out.println(g.toString());
            });
        }
    }
}

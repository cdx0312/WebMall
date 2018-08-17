package com.cdx.jedis.testJedisHash;

import com.cdx.common.util.JsonUtils;
import com.cdx.domain.TbItem;
import com.cdx.jedis.JedisClient;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestJedisSpring {

    @Before
    public void before() {
    }

    @Test
    public void testJedisClientPool() throws Exception {
        //初始化Spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring/springmvc-redis.xml");
        //从容器中获得JedisClient对象
        JedisClient jedisClient = (JedisClient) applicationContext.getBean("jedisClientPool");
        //使用JedisClient对象操作redis
        Jedis jedis = jedisClient.getJedis();
        Map<String, String> map =  jedis.hgetAll("cd571320-3683-4f7a-b490-6873d9533727");
        //d.如果redis中有的数据，Cookie中也有，相同商品时数量相加
        System.out.println(map.size());
        List<TbItem> cartItemListFromRedis = new ArrayList<>();
        for (String string : map.keySet()) {
            cartItemListFromRedis.add(JsonUtils.jsonToPojo(map.get(string), TbItem.class));
        }
        System.out.println(cartItemListFromRedis);
    }

    @Test
    public void TestHgetAll() {
        Jedis jedis = new Jedis("192.168.25.133", 6379);
        Map<String, String> map =  jedis.hgetAll("cd571320-3683-4f7a-b490-6873d9533727");
        System.out.println(map.toString());
    }
}

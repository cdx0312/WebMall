package jedis;

import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/**
 * 测试Jedis
 */
public class TestJedis {

    @Test
    public void testJedis() throws Exception {
        //创建一个Jedis对象，需要制定服务的IP和端口号
        Jedis jedis = new Jedis("123.206.76.76", 6379);
        //直接操作redis数据库
        jedis.set("jedis-key", "1234");
        String result = jedis.get("jedis-key");
        System.out.println(result);
        //关闭Jedis
        jedis.close();
    }

    @Test
    public void testJedisPool() throws Exception {
        //创建一个数据库连接池对象，需要制定服务的IP和端口号
        JedisPool jedisPool = new JedisPool("123.206.76.76", 6379);
        //从连接池中获得连接
        Jedis jedis = jedisPool.getResource();
        //直接操作redis数据库
        jedis.set("jedisPool", "jedisPool");
        String result = jedis.get("jedisPool");
        System.out.println(result);
        //关闭Jedis连接
        jedis.close();
    }

    /**
     * 持久化方案：
     *      RDB：定期更新
     *      AOF：实时更新
     */

    /**
     * 测试JedisCluster
     * @throws Exception
     */
    @Test
    public void testJedisCluster() throws Exception {
        //创建一个JedisCluster对象，构造参数为Set类型，每个集合中每个元素是一个HostAndPort类型
        Set<HostAndPort> nodes = new HashSet<>();
        //向集合中添加节点
        nodes.add(new HostAndPort("123.206.76.76", 7001));
        nodes.add(new HostAndPort("123.206.76.76", 7002));
        nodes.add(new HostAndPort("123.206.76.76", 7003));
        nodes.add(new HostAndPort("123.206.76.76", 7004));
        nodes.add(new HostAndPort("123.206.76.76", 7005));
        nodes.add(new HostAndPort("123.206.76.76", 7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        //使用JedisCluster来操作redis，自带连接池。JedisCluster对象可以是单例的
        jedisCluster.set("cluster-test", "hello jedis cluster");
        String result = jedisCluster.get("cluster-test");
        System.out.println(result);
        //系统关闭前关闭JedisCluster
    }

}

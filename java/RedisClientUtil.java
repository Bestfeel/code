package com.gizwits.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 客户端 工具类
 */
public final class RedisClientUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(RedisClientUtil.class);

    //Redis服务器IP
    private static String ADDR = Constants.REDIS_HOST;
    //Redis的端口号
    private static int PORT = Constants.REDIS_PORT;
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_TOTAL = 1024;
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;
    private static int TIMEOUT = 10000;
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;
    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {

            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_TOTAL);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT);

            LOGGER.info("{\"type\":\"{}\",\"message\":\"{}\"}", "common", "连接redis信息host:" + ADDR + "...port:" + PORT);

        } catch (Exception e) {

            LOGGER.error("{\"type\":\"{}\",\"message\":\"{}\"}", "common", "redis连接失败host:" + ADDR + "....port:" + PORT + "Exception:" + e.getMessage());

            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();

                LOGGER.info("{\"type\":\"{}\",\"message\":\"{}\"}", "common", " 获取 redis  客户端连接成功host:" + ADDR + "..port:" + PORT);

                return resource;
            } else {

                LOGGER.error("{\"type\":\"{}\",\"message\":\"{}\"}", "common", " 获取 redis  客户端连接失败 host:" + ADDR + "..port:" + PORT);

                return null;
            }
        } catch (Exception e) {

            LOGGER.error("{\"type\":\"{}\",\"message\":\"{}\"}", "common", " 获取 redis  客户端连接失败异常 host:" + ADDR + "..port:" + PORT + "..Exception:" + e.getMessage());

            e.printStackTrace();
            return null;
        }
    }

    /**
     * 释放jedis资源.此api 已经过时.可以使用jedis.close();
     *
     * @param jedis
     */
    public static void returnResource(final Jedis jedis) {
        if (jedis != null) {

            jedis.close();

            LOGGER.info("{\"type\":\"{}\",\"message\":\"{}\"}", "common", "redis客户端连接关闭...host:" + ADDR + "....port:" + PORT);

        }
    }
}
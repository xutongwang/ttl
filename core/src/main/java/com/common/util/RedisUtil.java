package com.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	private static Logger log = LoggerFactory.getLogger(RedisUtil.class);
	private static JedisPool jedisPool = null;
	private static int MAX_ACTIVE = 500;
	private static int MAX_IDLE = 5;
	private static int MAX_WAIT = 1000;
	private static boolean TEST_ON_BORROW = false;
	private static String PASSWORD = "wxt";
	private static String IP = "127.0.0.1";
	private static int PORT = 6379;
	private static int TIMEOUT = 10000;

	public synchronized static String get(String key) {
		Jedis jedis = getJedis();
		if (null == jedis) {
			return null;
		}
		String value = jedis.get(key);
		jedis.close();
		return value;
	}

	public synchronized static Jedis getJedis() {
		if (null == jedisPool) {
			initialPool();
		}
		Jedis jedis = null;
		try {
			if (null != jedisPool) {
				jedis = jedisPool.getResource();
				try {
					jedis.auth(PASSWORD);
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {
			log.error("Get jedis error : " + e);
		}
		return jedis;
	}

	private synchronized static void initialPool() {
		if (null == jedisPool) {
			try {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(MAX_ACTIVE);
				config.setMaxIdle(MAX_IDLE);
				config.setMaxWaitMillis(MAX_WAIT);
				config.setTestOnBorrow(TEST_ON_BORROW);
				jedisPool = new JedisPool(config, IP, PORT, TIMEOUT);
			} catch (Exception e) {
				log.error("First create JedisPool error : " + e);
			}
		}
	}

	public synchronized static void lpush(String key, String... strings) {
		try {
			Jedis jedis = RedisUtil.getJedis();
			jedis.lpush(key, strings);
			jedis.close();
		} catch (Exception e) {
			log.error("lpush error : " + e);
		}
	}

	public synchronized static void set(String key, String value, int seconds) {
		try {
			value = StringUtils.isBlank(value) ? "" : value;
			Jedis jedis = getJedis();
			jedis.setex(key, seconds, value);
			jedis.close();
		} catch (Exception e) {
			log.error("Set keyex error : " + e);
		}
	}
}
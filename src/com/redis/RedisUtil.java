package com.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	
	
	
	
	
	
	
	
	
	// Redis服务器IP
	private static String ADDR = "10.200.11.88";
	// Redis的端口号
	private static int PORT = 6379;
	// 访问密码
	private static String AUTH = "root";
	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private static int MAX_ACTIVE = 300;
	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 200;
	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private static int MAX_WAIT = 10000;
	private static int TIMEOUT = 10000;
	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;
	private static JedisPool jedisPool = null;
	
	/**
	 * 初始化Redis连接池
	 */
	static {
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxActive(MAX_ACTIVE);
			config.setMaxIdle(MAX_IDLE);
			config.setMaxWait(MAX_WAIT);
			config.setTestOnBorrow(TEST_ON_BORROW);
			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取Jedis实例
	 * 
	 * @return
	 */
	public synchronized Jedis getJedis() {
		try {
			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 释放jedis资源
	 * 
	 * @param jedis
	 */
	public void releaseResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}
	}
	
	/**
	 * 向redis中添加有序的set
	 */
	public void addSortSet(){
		Jedis jedis = this.getJedis();
		for(int i = 5; i < 100000; i ++){
			jedis.zadd("testSet", i, "java" + i);
		}
		this.releaseResource(jedis);
	}
	/**
	 * 从redis中取出有序set
	 */
	public void findFromSortSet(){
		Jedis jedis = this.getJedis();
		Set<String> set = jedis.zrange("testSet", 700, 705);
		for(String str: set){
			System.out.println(str);
		}
		this.releaseResource(jedis);
	}
	/**
	 * 向redis中存放hash
	 */
	public void addHash(){
		Jedis jedis = this.getJedis();
		Map<String,String> map = new HashMap<String,String>();
		map.put("1", "java");
		map.put("2", "C++");
		map.put("3", "PhP");
		jedis.hmset("testHash", map);
		this.releaseResource(jedis);
	}
	public void getHash(){
		System.out.println("222222222222222222222222");
		Jedis jedis = this.getJedis();
		Map<String,String> map = jedis.hgetAll("testHash");
		Set<String> set = map.keySet();
		for(String str: set){
			System.out.println(map.get(str));
		}
		this.releaseResource(jedis);

	}
}

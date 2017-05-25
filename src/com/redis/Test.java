package com.redis;


public class Test {
	
	public static void main(String[] args) {
		
		
		RedisUtil redisUtil = new RedisUtil(); 
		redisUtil.findFromSortSet();
		
		//redisUtil.addHash();
		redisUtil.getHash();
		
	}

}

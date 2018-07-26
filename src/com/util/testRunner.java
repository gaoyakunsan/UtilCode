package com.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangkun on 2016/1/7.
 */
public class testRunner {
    public static void main(String[] args){
        //如果不能自动探测到配置文件，可通过如下方法设置配置文件
        //ConfigurationManager.setConfigurationPath("D:\\javawp\\loggingtest\\target\\classes","conf\\app.config");

        Map<String,String> tags = new HashMap<String, String>();
        tags.put("tester","wk");
        MDC.setContextMap(tags);//通过MDC标记Tags
        Logger logger = LoggerFactory.getLogger(testRunner.class);//使用当前类名（testRunner）作为日志LoggerName
        logger.debug("testInfo");//记录debug日志
    }
}

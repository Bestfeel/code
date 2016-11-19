package com.gizwits.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * Created by feel on 15/11/14.
 * 获取conf 文件配置的工具类
 */
public final class JsonConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(JsonConfig.class);

    public static Config config(String configPath) {

        return ConfigFactory.load(configPath);

    }

    public static Config config() {

        //  生产环境.加载classPath 中application-site.conf的配置
        URL resource = JsonConfig.class.getClassLoader().getResource("application-site.conf");
        if (resource != null) {
            return ConfigFactory.parseURL(resource);
        } else {
            LOGGER.error("{\"type\":\"{}\",\"message\":\"{}\"}", "common", "加载外部配置文件.. 为空");
        }

        return config("application-site.conf");
    }


}
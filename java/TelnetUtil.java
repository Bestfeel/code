package com.gizwits.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by feel on 16/6/23.
 * 测试网络连通性
 */
public final class TelnetUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(TelnetUtil.class);

    public static boolean telnet(String server, Integer port, int timeout) {
        Socket target = null;
        boolean result = true;
        try {
            target = new Socket();
            InetSocketAddress address = new InetSocketAddress(server, port);
            target.connect(address, timeout);
        } catch (Exception e) {
            LOGGER.info("{\"type\":\"{}\",\"message\":\"{}\"}", "common", "网络连接失败,Exception:" + e.getMessage());

            result = false;
        } finally {
            if (target != null)
                try {
                    target.close();
                } catch (IOException e) {
                    LOGGER.info("{\"type\":\"{}\",\"message\":\"{}\"}", "common", "网络连接关闭异常,Exception:" + e.getMessage());

                }
        }
        return result;
    }

    public static boolean ping(String server, int timeout) {
        try {
            InetAddress address = InetAddress.getByName(server);
            // 测试地址是否可达 boolean
            return address.isReachable(timeout);
        } catch (Exception e) {

            LOGGER.info("{\"type\":\"{}\",\"message\":\"{}\"}", "common", "网络不可以达,Exception:" + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


}

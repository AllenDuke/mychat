package com.github.AllenDuke;

import com.github.AllenDuke.clientService.RPCClient;
import com.github.AllenDuke.netty.WebSocketServer;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/7/27
 */
public class WebsocketApplication {

    public static void main(String[] args) throws Exception {
        RPCClient.init();//消费离线消息存储等服务
        new WebSocketServer().start();//提供在线消息服务
    }
}

package com.github.AllenDuke.service;


import com.github.AllenDuke.pojo.dto.Message;
import io.netty.channel.Channel;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/7/27
 */
public interface ChatService {

    void handleP2pMsg(Message message);

    void handleGroupMsg(Message message);

    void handleConnectedMsg(Channel channel, Message message);

    void handleFriendRequestMsg(Message message);
}

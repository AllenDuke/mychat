package com.github.AllenDuke.service.impl;


import com.alibaba.fastjson.JSON;
import com.github.AllenDuke.dao.UserFriendMapper;
import com.github.AllenDuke.pojo.do0.User;
import com.github.AllenDuke.pojo.do0.UserFriend;
import com.github.AllenDuke.pojo.dto.Message;
import com.github.AllenDuke.service.UserFriendService;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/4/25
 */
@Service
@Transactional
public class UserFriendServiceImpl implements UserFriendService {

    @Autowired
    UserFriendMapper userFriendMapper;

    @Autowired
    Channel channel;

    @Override
    public List<User> getFriendsByUserId(Integer userId) {
        List<User> friends = userFriendMapper.getFriendsByUserId(userId);
        return friends;
    }

    @Override
    public void save(UserFriend userFriend) {
        userFriendMapper.saveUserFriend(userFriend);
    }

    @Override
    public void delete(Integer userId, Integer friendId) {
        userFriendMapper.deleteUserFriend(userId, friendId);
    }

    @Override
    public void applyFriend(Message friendRequestMessage) {
        //尝试使用netty实时推送，如果对方离线，就把消息存下来
        //todo 除了将Message直接转发到Websocket服务器，也可以RPC
//        ChatService chatService= (ChatService) RPCClient.getServiceImpl(ChatService.class);
//        chatService.handleFriendRequestMsg(friendRequestMessage);
        System.out.println("尝试在线转发好友请求信息");
        channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(friendRequestMessage)));
    }
}

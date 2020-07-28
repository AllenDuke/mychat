package com.github.AllenDuke.service.impl;


import com.github.AllenDuke.dao.FriendRequestMessageMapper;
import com.github.AllenDuke.pojo.dto.FriendRequestMessage;
import com.github.AllenDuke.service.FriendRequestMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 杜科
 * @description 处理好友申请等
 * @contact AllenDuke@163.com
 * @date 2020/4/25
 */
@Service
@Transactional
public class FriendRequestMessageServiceImpl implements FriendRequestMessageService {

    @Autowired
    FriendRequestMessageMapper friendRequestMessageMapper;

    @Override
    public void save(FriendRequestMessage friendRequestMessage) {
//        if(getFriendRequestMessage(friendRequestMessage.getUserId(),friendRequestMessage.getFriendId())!=null){
//            return;
//        }
        friendRequestMessageMapper.saveFriendRequestMessage(friendRequestMessage);
    }

    @Override
    public void delete(Integer userId, Integer friendId) {
        friendRequestMessageMapper.deleteFriendRequestMessage(userId,friendId);
    }

    @Override
    public List<FriendRequestMessage> getFriendRequestMessages(Integer userId) {
        return friendRequestMessageMapper.getFriendRequestMessages(userId);
    }

    @Override
    public FriendRequestMessage getFriendRequestMessage(Integer userId,Integer friendId) {
        return friendRequestMessageMapper.getFriendRequestMessage(userId,friendId);
    }

}

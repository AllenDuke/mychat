package com.github.AllenDuke.controller;


import com.github.AllenDuke.pojo.ao.UserFriendAO;
import com.github.AllenDuke.pojo.dto.FriendRequestMessage;
import com.github.AllenDuke.pojo.do0.User;
import com.github.AllenDuke.pojo.do0.UserFriend;
import com.github.AllenDuke.pojo.dto.Message;
import com.github.AllenDuke.result.ErrorCode;
import com.github.AllenDuke.result.Result;
import com.github.AllenDuke.service.FriendRequestMessageService;
import com.github.AllenDuke.service.UserFriendService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private UserFriendService userFriendService;

    @Autowired
    Mapper dozerMapper;

    @Autowired
    FriendRequestMessageService friendRequestMessageService;

    @RequestMapping("/getFriendsByUserId")
    public Result<List<User>> getFriendsByUserId(Integer userId) {
        List<User> friends = userFriendService.getFriendsByUserId(userId);
        return Result.success(friends);
    }

    /**
     * @description: 某一人在确认时要双向确定，双向保存。删除好友申请信息
     * @param userFriendAO
     * @return: cn.edu.scau.dbclub.mychat.result.Result
     * @author: 杜科
     * @date: 2020/4/29
     */
    @RequestMapping("/save")
    public Result save(@RequestBody UserFriendAO userFriendAO){
        UserFriend userFriend=dozerMapper.map(userFriendAO,UserFriend.class);
        userFriendService.save(userFriend);

        UserFriend friendUser=new UserFriend();
        friendUser.setUserId(userFriend.getFriendId());
        friendUser.setFriendId(userFriend.getUserId());
        userFriendService.save(friendUser);
        //todo 通知对方已经成为好友，由前端发出一条固定信息，”我们已经成为好友，现在可以可是聊天“
        friendRequestMessageService.delete(userFriendAO.getFriendId(),userFriendAO.getUserId());
        return Result.success();
    }

    @RequestMapping("/delete")
    public Result delete(Integer userId, Integer friendId){
        userFriendService.delete(userId,friendId);
        userFriendService.delete(friendId,userId);
        return Result.success();
    }

    //申请添加一个好友
    @RequestMapping("/applyFriend")
    public Result applyFriend(@RequestBody Message applyMessage){
        if(applyMessage.getType()!=3) return Result.fail(ErrorCode.INVALID_PARAMETER,"不是好友申请信息");
        FriendRequestMessage friendRequestMessage =
                friendRequestMessageService.getFriendRequestMessage(applyMessage.getFriendRequestMessage().getUserId(),
                applyMessage.getFriendRequestMessage().getFriendId());
        if(friendRequestMessage!=null) return Result.success(friendRequestMessage);
        userFriendService.applyFriend(applyMessage);
        return Result.success();
    }

    /**
     * @description: todo 当拒绝与被拒绝时，要做一定通知操作，由前端发出一条固定信息”对不起，我拒绝了你的请求“
     * @param userFriendAO
     * @return: cn.edu.scau.dbclub.mychat.result.Result
     * @author: 杜科
     * @date: 2020/4/30
     */
    @RequestMapping("/rejectFriend")
    public Result reject(@RequestBody UserFriendAO userFriendAO){
        friendRequestMessageService.delete(userFriendAO.getFriendId(),userFriendAO.getUserId());
        return Result.success();
    }
}

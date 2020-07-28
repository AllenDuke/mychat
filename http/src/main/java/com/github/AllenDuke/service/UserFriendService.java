package com.github.AllenDuke.service;


import com.github.AllenDuke.pojo.do0.User;
import com.github.AllenDuke.pojo.do0.UserFriend;
import com.github.AllenDuke.pojo.dto.Message;

import java.util.List;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/4/21
 */
public interface UserFriendService {
    List<User> getFriendsByUserId(Integer userId);

    void save(UserFriend userFriend);

    void applyFriend(Message friendRequestMessage);

    void delete(Integer userId, Integer friendId);
}

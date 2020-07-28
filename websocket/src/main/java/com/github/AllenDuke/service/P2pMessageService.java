package com.github.AllenDuke.service;


import com.github.AllenDuke.pojo.dto.P2pMessage;

import java.util.List;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/4/20
 */
public interface P2pMessageService {
    void save(P2pMessage p2pMessage);

    List<P2pMessage> getUnReadP2pMessagesByUserId(Integer userId);

    void deleteMessages(List<Integer> p2pMessageIds);

    void deleteMessage(Integer p2pMessageId);
}

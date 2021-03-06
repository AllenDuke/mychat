package com.github.AllenDuke.dao;


import com.github.AllenDuke.pojo.dto.P2pMessage;

import java.util.List;

public interface P2pMessageMapper {
    int deleteP2pMessage(Integer id);

    int saveP2pMessage(P2pMessage record);

    P2pMessage getP2pMessage(Integer id);

    List<P2pMessage> listP2pMessages();

    int updateP2pMessage(P2pMessage record);

    List<P2pMessage> getUnReadP2pMessagesByUserId(Integer userId);

    void deleteP2pMessages(List<Integer> p2pMessageIds);
}
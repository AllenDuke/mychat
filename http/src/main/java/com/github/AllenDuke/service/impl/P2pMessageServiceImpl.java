package com.github.AllenDuke.service.impl;


import com.github.AllenDuke.dao.P2pMessageMapper;
import com.github.AllenDuke.pojo.dto.P2pMessage;
import com.github.AllenDuke.service.P2pMessageService;
import com.github.AllenDuke.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 杜科
 * @description
 * @contact AllenDuke@163.com
 * @date 2020/4/20
 */
@Service
@Transactional
public class P2pMessageServiceImpl implements P2pMessageService {

    @Autowired
    private ThreadPoolExecutor executor;

    @Autowired
    private P2pMessageMapper p2pMessageMapper;

    @Autowired
    FastDFSClient fastDFSClient;

    @Override
    public void save(P2pMessage p2pMessage) {
        //异步存到数据库
        executor.execute(()->p2pMessageMapper.saveP2pMessage(p2pMessage));
    }

    @Override
    public List<P2pMessage> getUnReadP2pMessagesByUserId(Integer userId) {
        List<P2pMessage> unReadP2pMessages = p2pMessageMapper.getUnReadP2pMessagesByUserId(userId);
        return unReadP2pMessages;
    }

    @Override
    public void deleteMessages(List<Integer> p2pMessageIds) {
        p2pMessageMapper.deleteP2pMessages(p2pMessageIds);
    }

    @Override
    public void deleteMessage(Integer p2pMessageId) {
        P2pMessage p2pMessage = p2pMessageMapper.getP2pMessage(p2pMessageId);
        String location = p2pMessage.getLocation();
        if(location!=null) fastDFSClient.deleteFile(location);//一道删除
        p2pMessageMapper.deleteP2pMessage(p2pMessageId);//todo 返回location
    }
}

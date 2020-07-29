package com.github.AllenDuke.netty;


import com.alibaba.fastjson.JSON;
import com.github.AllenDuke.clientService.RPCClient;
import com.github.AllenDuke.pojo.do0.Group;
import com.github.AllenDuke.pojo.dto.*;
import com.github.AllenDuke.service.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 处理消息的handler
 * TextWebSocketFrame: 在netty中，是用于为websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> implements ChatService {

    //key为userId，value为其channel
    private static Map<Integer, Channel> userIdChannelMap = new ConcurrentHashMap<>();

    //key为channel，value为userId，用于断连时，快速查找进而移除
    private static Map<Channel, Integer> channelUserIdMap = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 正在连接...");
        super.channelActive(ctx);
    }

    /**
     * @param ctx
     * @param msg 接收到的websocket帧
     * @description: 对消息进行解析，处理，转发 todo 是否应该增加消息消费的确认机制呢，
     * todo 万一服务端在把消息发送给客户端的时候，客户端离线，这时消息有可能丢失
     * @return: void
     * @author: 杜科
     * @date: 2020/4/20
     */
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

        String text = msg.text();
        System.out.println("接收到消息数据为：" + text);
        //todo 记录异常信息防止恶意攻击
        Message message = JSON.parseObject(text, Message.class);
        switch (message.getType()) {
            case 0:
                handleP2pMsg(message);
                break;
            case 1:
                handleGroupMsg(message);
                break;
            //处理连接信息，客户端连接成功后发送一条连接信息
            case 2:
                handleConnectedMsg(ctx.channel(), message);
                break;
            case 3:
                handleFriendRequestMsg(message);
                break;
        }

    }

    //todo 对方可能不在同一个服务器，所以这里应该检查redis，如果在线，那么从redis中拿到主机地址，这里再做转发
    @Override
    public void handleP2pMsg(Message message) {
        P2pMessage p2pMessage = message.getP2pMessage();
        Integer receiveId = p2pMessage.getReceiveId();
        Channel channel = userIdChannelMap.get(receiveId);
        if (channel == null) {//离线
            /**
             * 对面是异步存到数据库的，所以这里使用同步或者异步的区别不大，而且save方法的无返回值，trivial最终选择异步调用
             */
            P2pMessageService p2pMessageService = (P2pMessageService) RPCClient.getServiceImpl(P2pMessageService.class);
            p2pMessageService.save(p2pMessage);
        } else channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
    }

    @Override
    public void handleGroupMsg(Message message) {
        GroupMessage groupMessage = message.getGroupMessage();
        Integer groupId = groupMessage.getGroupId();
        GroupService groupService = (GroupService) RPCClient.getServiceImpl(GroupService.class);
        Group group = groupService.getGroup(groupId);
        GroupMemService groupMemService = (GroupMemService) RPCClient.getServiceImpl(GroupMemService.class);

//        Integer groupMemOnlineNum = groupService.getGroupMemOnlineNum(group);

//        if (!group.getMemNum().equals(groupMemOnlineNum)) {//有人离线
//            GroupMessageService groupMessageService = SpringUtil.getBean(GroupMessageServiceImpl.class);
//            groupMessageService.save(groupMessage);
//        }

        GroupMessageService groupMessageService =
                (GroupMessageService) RPCClient.getServiceImpl(GroupMessageService.class);
        groupMessageService.save(groupMessage);

        for (Integer memId : groupMemService.getMemIdsByGroupId(groupId)) {
            Channel channel1 = userIdChannelMap.get(memId);
            if (channel1 != null) //向在线的人发送
                channel1.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }


    //todo 将当前用户所处的websocket服务器地址记录在redis
    @Override
    public void handleConnectedMsg(Channel channel, Message message) {
        ConnectedMessage connectedMessage = message.getConnectedMessage();
        Integer userId = connectedMessage.getUserId();
        //用户上线，更新群在线人数
        handlerUserLogin(userId);
        userIdChannelMap.put(userId, channel);
        channelUserIdMap.put(channel, userId);
    }

    @Override
    public void handleFriendRequestMsg(Message message) {
        FriendRequestMessage friendRequestMessage = message.getFriendRequestMessage();
        Integer friendId = friendRequestMessage.getFriendId();
        Channel channel = userIdChannelMap.get(friendId);
        if (channel == null) {//离线
            System.out.println("用户：" + friendId + " 离线");
            FriendRequestMessageService requestMessageService =
                    (FriendRequestMessageService) RPCClient.getServiceImpl(FriendRequestMessageService.class);
            FriendRequestMessage requestMessage =
                    requestMessageService.getFriendRequestMessage(friendRequestMessage.getUserId(), friendId);
            if (requestMessage != null) {
                System.out.println("数据库已经存在，对方尚未处理");
                return;
            }
            /**
             * todo 只有把save置于getFriendRequestMessage前面，save才会执行，否则似乎没有了下面的代码一样，不执行，没有异常，断点也进不去
             */
            requestMessageService.save(friendRequestMessage);
            System.out.println("新增好友请求信息：" + friendRequestMessage);
        } else channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
    }

    /**
     * @param ctx
     * @description: 当连接断开时，移除该客户及其channel todo 移除redis相关信息
     * @return: void
     * @author: 杜科
     * @date: 2020/4/20
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 已断开连接！");
        Integer removeUserId = channelUserIdMap.remove(ctx.channel());
        //用户下线，更新群在线人数
//        handlerUserLogout(removeUserId);
        userIdChannelMap.remove(removeUserId);
        super.channelInactive(ctx);
    }

    /**
     * @param ctx
     * @param cause
     * @description: 异常时关闭连接
     * @return: void
     * @author: 杜科
     * @date: 2020/4/20
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }


    /***
     * @Description: undo 用户登录，更新在线人数
     *                  用于更新活跃人数
     * @Author: hermanCho
     * @Date: 2020-05-05
     * @Param userId:
     * @return: void
     **/
    public static void handlerUserLogin(Integer userId) {

//        GroupMemService groupMemService = SpringUtil.getBean(GroupMemServiceImpl.class);
//        GroupService groupService = SpringUtil.getBean(GroupServiceImpl.class);
//        List<Group> groupIds = groupMemService.selectGroupsByUserId(userId);
//
//        for (Group group : groupIds) {
//            int beforeLoginNum = groupService.getGroupMemOnlineNum(group);
//            groupService.updateGroupMemOnlineNum(group, beforeLoginNum + 1);
//        }

//        // 记录用户登录，用于统计人数 。
//        // 用户退出时，并无相关逻辑
//        // todo new 会导致属性无法自动注入，不由spring管理
//        UVCounterService uvCounterService = new UVCounterService();
//        uvCounterService.addLoginUser(userId);
    }

    // 同上，用户退出
    public void handlerUserLogout(Integer userId) {
        GroupMemService groupMemService = (GroupMemService) RPCClient.getServiceImpl(GroupMemService.class);
        GroupService groupService = (GroupService) RPCClient.getServiceImpl(GroupService.class);
        List<Group> groupIds = groupMemService.selectGroupsByUserId(userId);
        for (Group group : groupIds) {
            int beforeLogOutNum = groupService.getGroupMemOnlineNum(group);
            groupService.updateGroupMemOnlineNum(group, beforeLogOutNum - 1);
        }
    }


}
package com.github.AllenDuke.config;

import com.github.AllenDuke.netty.WebSocketClientHandler;
import com.github.AllenDuke.spring.TrivialSpringUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.dozer.DozerBeanMapperBuilder;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 杜科
 * @description 一些单例
 * @contact AllenDuke@163.com
 * @date 2020/4/24
 */
@Configuration
public class SingletonBeansConfig {

    @Bean
    public ThreadPoolExecutor executor(){
        return new ThreadPoolExecutor(2,4,
                30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    @Bean
    public Mapper mapper() {
        return DozerBeanMapperBuilder.buildDefault();
    }

    @Bean
    public Channel channel() throws InterruptedException, URISyntaxException {
        /**
         * 一般情况下，channel只用作发送数据，不接收数据
         */
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(
                        new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline pipeline = ch.pipeline();
                                pipeline.addLast("http-codec",new HttpClientCodec());
                                pipeline.addLast("aggregator",new HttpObjectAggregator(1024*1024*10));
                                pipeline.addLast("hookedHandler", new WebSocketClientHandler());
                            }
                        }
                );
        //websocke连接的地址
        URI websocketURI = new URI("ws://localhost:9001/ws");
        HttpHeaders httpHeaders = new DefaultHttpHeaders();
        //进行握手
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(websocketURI, WebSocketVersion.V13, (String) null, true, httpHeaders);
        //客户端与服务端连接的通道，final修饰表示只会有一个
        final Channel channel = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();
        WebSocketClientHandler handler = (WebSocketClientHandler) channel.pipeline().get("hookedHandler");
        handler.setHandshaker(handshaker);
        handshaker.handshake(channel);
        //阻塞等待是否握手成功
        handler.handshakeFuture().sync();
        return channel;
    }

    //使得trivial可以从当前ioc容器中获取到bean，在当前接受rpc，进行方法反射调用时用到
    @Bean
    public TrivialSpringUtil trivialSpringUtil(){
        return new TrivialSpringUtil();
    }
}

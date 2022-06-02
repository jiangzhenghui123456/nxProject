package com.nx.netty.chat;

import com.nx.netty.chat.hander.ChatServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ChatServer {

    private int port = 20111;

    public void start(int port) {
        //主从线程模型的线程池
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(1);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 最大堵塞长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChatServerChannelInitializer());
            // 绑定端口，因为这个代码是封装的，不仅仅是绑定端口号，更是建立连接，server.accpet()；这个是阻塞的，所以是异步；
            ChannelFuture f = b.bind(this.port).sync();
            log.info("服务已启动,监听端口" + this.port);
            // 起一个阻塞的作用，netty初始化完之前暂时不执行到finally代码块中
            f.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public void start() {
        start(this.port);
    }


    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

}

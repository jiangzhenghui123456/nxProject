package com.nx.netty.chat;

import com.nx.netty.chat.hander.ChatServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class ChatServer implements DisposableBean {
    private int port = 20111;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ServerBootstrap server;

    // 保证spring容器初始化的时候作用这段代码
    public ChatServer() {
        // 主从线程模式的线程池
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        // 主从线程模式的线程池
        NioEventLoopGroup bossgroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        server = new ServerBootstrap();
        server.group(bossgroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childHandler(new ChatServerChannelInitializer());
    }

    //    public void start(int port) {
//        //主从线程模型的线程池
//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    // 最大堵塞长度
//                    .option(ChannelOption.SO_BACKLOG, 1024)
//                    .childHandler(new ChatServerChannelInitializer());
//            // 绑定端口，因为这个代码是封装的，不仅仅是绑定端口号，更是建立连接，server.accpet()；这个是阻塞的，所以是异步；
//            ChannelFuture f = b.bind(this.port).sync();
//            log.info("服务已启动,监听端口" + this.port);
//            // 起一个阻塞的作用，netty初始化完之前暂时不执行到finally代码块中
//            f.channel().closeFuture().sync();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            workerGroup.shutdownGracefully();
//            bossGroup.shutdownGracefully();
//        }
//    }
    public void start() {
        try {
            server.bind(this.port).sync();
            log.info("netty websocket server 启动完毕 对应端口：{}", this.port);
        } catch (Exception e) {
            e.printStackTrace();
            close();
            log.error("netty websocket server 启动失败.....");
        }
    }

    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }


    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.start();
    }

    @Override
    public void destroy() throws Exception {
        close();
    }
}

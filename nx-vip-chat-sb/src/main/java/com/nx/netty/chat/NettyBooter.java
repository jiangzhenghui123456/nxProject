package com.nx.netty.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author 江江江
 * @version 1.0
 * @description: TODO
 * @date 2022/6/3 10:13
 */
@Slf4j
@Component
public class NettyBooter implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private ChatServer chatServer;

    // 当所有的bean都已经处理完成之后，Spring ioc容器会有一个发布事件的动作
    // 我们再这里可以做一些扩展
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            log.info("启动netty");
            chatServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

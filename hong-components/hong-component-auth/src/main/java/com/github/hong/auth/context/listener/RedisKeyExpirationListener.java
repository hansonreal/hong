package com.github.hong.auth.context.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * 监听所有db（redis 默认有16个db(0-15)）的过期事件__keyevent@*__:expired"
 */
@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {


    /**
     * Creates new {@link MessageListener} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
        // 解决控制台 ERR unknown command `CONFIG`, with args beginning with: `GET`, `notify-keyspace-events` 错误
        setKeyspaceNotificationsConfigParameter(null);
    }


    /**
     * 自定义实现
     *
     * @param message message must not be {@literal null}.
     * @param pattern pattern matching the channel (if specified) - can be {@literal null}.
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //  获取到失效的 key，进行失效业务处理
        //  例如 登录的accessToken失效，密码连续错误导致账号被锁定处理等业务

        //1、注意点 如果需要集群部署 需要使用分布式锁
        //2、Redis 的KEY 需要有规则 可利用策略模式将具体业务封装为特定的策略 实现业务处理
        // 例如 captcha 开头的代表验证码业务

        String expiredKey = message.toString();
        log.warn("Redis key expired event: {}", expiredKey);
    }
}

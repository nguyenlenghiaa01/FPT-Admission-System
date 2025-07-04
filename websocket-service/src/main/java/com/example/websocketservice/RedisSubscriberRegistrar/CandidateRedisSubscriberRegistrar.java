package com.example.websocketservice.RedisSubscriberRegistrar;

import com.example.websocketservice.subscriber.CandidateSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidateRedisSubscriberRegistrar implements InitializingBean {
    private final RedisMessageListenerContainer container;
    private final CandidateSubscriber subscriber;

    @Override
    public void afterPropertiesSet() {
        container.addMessageListener(subscriber, new ChannelTopic("new-application"));
    }
}


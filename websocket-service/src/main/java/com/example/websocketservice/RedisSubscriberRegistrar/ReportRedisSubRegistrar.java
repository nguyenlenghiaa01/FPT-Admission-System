package com.example.websocketservice.RedisSubscriberRegistrar;

import com.example.websocketservice.subscriber.ConsultantSubscriber;
import com.example.websocketservice.subscriber.ReportSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportRedisSubRegistrar implements InitializingBean {
    private final RedisMessageListenerContainer container;
    private final ReportSubscriber subscriber;

    @Override
    public void afterPropertiesSet() {
        container.addMessageListener(subscriber, new ChannelTopic("report-channel"));
    }
}

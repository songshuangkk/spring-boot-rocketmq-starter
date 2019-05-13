package com.github.songshuangkk.consumer;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

public interface RocketConsumerReceiveMessage<T> {

  ConsumeConcurrentlyStatus dealWithMessage(T message);
}

package com.github.songshuangkk.consumer;

import com.github.songshuangkk.serialize.Serializer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class ConsumerRegister {

  private static final Logger logger = LoggerFactory.getLogger(ConsumerRegister.class);

  static void register(ClassLoader classLoader) throws MQClientException, ClassNotFoundException {
    List<ConsumerInfo> list = ConsumerStore.getConsumerList();

    for (ConsumerInfo consumerInfo : list) {
      DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(consumerInfo.getGroupId());
      defaultMQPushConsumer.setNamesrvAddr(consumerInfo.getNameServerAddr());
      defaultMQPushConsumer.subscribe(consumerInfo.getTopic(), consumerInfo.getTags());

      Serializer serializer = consumerInfo.getSerializer();

      Class<?> messageClazz = classLoader.loadClass(consumerInfo.getMessageClazz());

      RocketConsumerReceiveMessage rocketConsumerReceiveMessage = consumerInfo.getRocketConsumerReceiveMessage();

      switch (consumerInfo.getMessageType()) {
        case Tran:
          break;
        case Delay:
          break;
        case Order:
          break;
        default:
          defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

              msgs.forEach(msg -> {
                Object message = serializer.deserialize(msg.getBody(), messageClazz);
                rocketConsumerReceiveMessage.dealWithMessage(message);
              });

              return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
          });
          break;
      }

      defaultMQPushConsumer.start();
      logger.info("Consumer Listen Message On: [topic = {}, group = {}, tags = {}, type = {}]",
          consumerInfo.getTopic(), consumerInfo.getGroupId(), consumerInfo.getTags(), consumerInfo.getMessageType());
    }
  }
}

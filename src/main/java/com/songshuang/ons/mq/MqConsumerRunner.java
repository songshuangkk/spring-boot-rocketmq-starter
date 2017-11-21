package com.songshuang.ons.mq;

import com.aliyun.openservices.ons.api.*;
import com.songshuang.ons.mq.annotationcs.MessageConsumer;
import com.songshuang.ons.mq.util.MessageApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 消息监听启动对象.
 */
public class MqConsumerRunner implements CommandLineRunner, DisposableBean {

  private static final Logger logger = LoggerFactory.getLogger(MqConsumerRunner.class);

  private List<Consumer> consumerList;

  @Autowired
  private AbstractApplicationContext applicationContext;

  @Autowired
  private MqProperties mqProperties;

  /**
   * 注销关闭消息的消费.
   *
   * @throws Exception
   */
  @Override
  public void destroy() throws Exception {

    logger.info("Shutting down MessageConsumer server ...");

    if (!CollectionUtils.isEmpty(consumerList)) {

      consumerList.stream().forEach(consumer -> {

        consumer.shutdown();

      });

    }

    logger.info("MessageConsumer server stopped.");

  }

  @Override
  public void run(String... strings) throws Exception {

    Map<String, ConsumerServiceWarp> consumerServiceMap = new HashMap<>();

    MessageApplicationUtils.getBeanNamesByTypeWithAnnotation(MessageConsumerService.class,
            MessageConsumer.class,
            applicationContext).forEach(name -> {

      MessageConsumerService messageConsumerService = applicationContext.getBeanFactory().getBean(name, MessageConsumerService.class);

      MessageConsumer messageConsumerAnn = applicationContext.findAnnotationOnBean(name, MessageConsumer.class);

      String messageGroup = messageConsumerAnn.group();

      String topIc = messageConsumerAnn.topic();

      topIc = mqProperties.getConfig().get(topIc).toString();

      String tag = messageConsumerAnn.tag();

      if (!StringUtils.isEmpty(tag) && mqProperties.getConfig().get(tag) != null) {
        tag = mqProperties.getConfig().get(tag).toString();
      }

      String consumerId = messageConsumerAnn.consumerId();

      consumerId = mqProperties.getConfig().get(consumerId).toString();

      consumerServiceMap.put(topIc, createConsumerServiceWarp(messageConsumerService, topIc, tag, consumerId));

      // 获取topic , group 进行创建消息监听者

      logger.info("'{}' message consumer has been registered.", messageConsumerService.getClass().getName());

    });

    // 判断topic是否为空，不为空，就进行创建监听
    if (!CollectionUtils.isEmpty(consumerServiceMap)) {

      consumerList = new ArrayList<>();

      // 进行创建消息消费者服务
      for (Map.Entry<String, ConsumerServiceWarp> entry: consumerServiceMap.entrySet()) {

        Properties properties = new Properties();
        // mq accessKey
        properties.setProperty(PropertyKeyConst.AccessKey, mqProperties.getConfig().get(PropertyKeyConst.AccessKey).toString());
        // mq secretKey
        properties.setProperty(PropertyKeyConst.SecretKey, mqProperties.getConfig().get(PropertyKeyConst.SecretKey).toString());
        // ons addr
        if (mqProperties.getConfig().get(PropertyKeyConst.ONSAddr) != null) {
          properties.setProperty(PropertyKeyConst.ONSAddr, mqProperties.getConfig().get(PropertyKeyConst.ONSAddr).toString());
        }
        // consumerID
        properties.setProperty(PropertyKeyConst.ConsumerId, entry.getValue().consumerId);

        Consumer consumer = ONSFactory.createConsumer(properties);

        consumer.subscribe(entry.getKey(), entry.getValue().tag, new MessageListener() { //订阅多个Tag

          public Action consume(Message message, ConsumeContext context) {

            if (logger.isDebugEnabled()) {
              logger.debug("Receive: " + message);
            }

            // TODO 调用我们的消息消费服务处理消费业务
            entry.getValue().messageConsumerService.receiveMessage(SerializationUtils.deserialize(message.getBody()));

            return Action.CommitMessage;

          }

        });

        consumer.start();



      }

    }
  }

  private ConsumerServiceWarp createConsumerServiceWarp(MessageConsumerService MessageConsumerService, String topic, String tag, String consumerId) {

    ConsumerServiceWarp consumerServiceWarp = new ConsumerServiceWarp();

    consumerServiceWarp.setMessageConsumerService(MessageConsumerService);

    consumerServiceWarp.setTag(tag);

    consumerServiceWarp.setTopic(topic);

    consumerServiceWarp.setConsumerId(consumerId);

    return consumerServiceWarp;

  }

  protected class ConsumerServiceWarp {

    private MessageConsumerService messageConsumerService;

    private String topic;

    private String tag;

    private String consumerId;

    public MessageConsumerService getMessageConsumerService() {
      return messageConsumerService;
    }

    public void setMessageConsumerService(MessageConsumerService messageConsumerService) {
      this.messageConsumerService = messageConsumerService;
    }

    public String getTopic() {
      return topic;
    }

    public void setTopic(String topic) {
      this.topic = topic;
    }

    public String getTag() {
      return tag;
    }

    public void setTag(String tag) {
      this.tag = tag;
    }

    public String getConsumerId() {
      return consumerId;
    }

    public void setConsumerId(String consumerId) {
      this.consumerId = consumerId;
    }
  }

}

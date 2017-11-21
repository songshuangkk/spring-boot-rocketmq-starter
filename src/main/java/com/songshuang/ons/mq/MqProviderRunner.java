package com.songshuang.ons.mq;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.songshuang.ons.mq.annotationcs.MessageProvider;
import com.songshuang.ons.mq.constants.ProviderTypeConstants;
import com.songshuang.ons.mq.util.MessageApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MqProviderRunner implements CommandLineRunner, DisposableBean {

  private static final Logger logger = LoggerFactory.getLogger(MqProviderRunner.class);

  private List<Producer> producerList = new ArrayList<>();

  private List<OrderProducer> orderProducerList = new ArrayList<>();

  @Autowired
  private AbstractApplicationContext applicationContext;

  @Autowired
  private MqProperties mqProperties;

  @Override
  public void destroy() throws Exception {

    if (!CollectionUtils.isEmpty(producerList)) {

      producerList.stream().forEach(producer -> {

        producer.shutdown();

      });

    }

    if (!CollectionUtils.isEmpty(orderProducerList)) {

      orderProducerList.stream().forEach(orderProducer -> {

        orderProducer.shutdown();

      });

    }

  }

  @Override
  public void run(String... strings) throws Exception {

    MessageApplicationUtils.getBeanNamesByTypeWithAnnotation(MessageProducerService.class, MessageProvider.class, applicationContext).forEach(name -> {

      MessageBaseProducer messageProducerService = applicationContext.getBeanFactory().getBean(name, MessageBaseProducer.class);

      MessageProvider messageProvider = applicationContext.findAnnotationOnBean(name, MessageProvider.class);

      String group = messageProvider.group();

      String topic = messageProvider.topic();

      String tag = messageProvider.tag();

      String producerId = messageProvider.producerId();

      // 获取配置中的topic
      topic = mqProperties.getConfig().get(topic).toString();

      messageProducerService.setTopIc(topic);

      // 获取配置中的topic
      if (!StringUtils.isEmpty(tag) && mqProperties.getConfig().get(tag) != null) {
        tag = mqProperties.getConfig().get(tag).toString();
      }

      messageProducerService.setTag(tag);

      int providerType = messageProvider.providerType();

      Properties properties = new Properties();
      // mq accessKey
      properties.setProperty(PropertyKeyConst.AccessKey, mqProperties.getConfig().get(PropertyKeyConst.AccessKey).toString());
      // mq secretKey
      properties.setProperty(PropertyKeyConst.SecretKey, mqProperties.getConfig().get(PropertyKeyConst.SecretKey).toString());
      // ons addr
      if (mqProperties.getConfig().get(PropertyKeyConst.ONSAddr) != null) {
        properties.setProperty(PropertyKeyConst.ONSAddr, mqProperties.getConfig().get(PropertyKeyConst.ONSAddr).toString());
      }
      // producer id
      properties.setProperty(PropertyKeyConst.ProducerId, mqProperties.getConfig().get(producerId).toString());

      if (providerType == ProviderTypeConstants.DELAY) {        // 延迟消息生产者

        Producer producer = ONSFactory.createProducer(properties);

        messageProducerService.setProducer(producer);

        producer.start();

        producerList.add(producer);

      }

      if (providerType == ProviderTypeConstants.NORMAL) {        // 普通消息生产者

        OrderProducer orderProducer = ONSFactory.createOrderProducer(properties);

        messageProducerService.setOrderProducer(orderProducer);

        orderProducer.start();

        orderProducerList.add(orderProducer);

      }

      if (providerType == ProviderTypeConstants.TIMER) {         // 定时消息生产者

        Producer producer = ONSFactory.createProducer(properties);

        messageProducerService.setProducer(producer);

        producer.start();

        producerList.add(producer);

      }

      logger.info("'{}' message producer has been registered.", messageProducerService.getClass().getName());

      if (providerType == ProviderTypeConstants.TRANSACTION) {         // 事务消息生产者

        throw new RuntimeException("事务消息暂时不支持");

      }
    });
  }
}

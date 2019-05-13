package com.github.songshuangkk.consumer;

import com.github.songshuangkk.annotations.RocketConsumer;
import com.github.songshuangkk.properties.RocketConfigProperties;
import com.github.songshuangkk.serialize.Serializer;
import com.github.songshuangkk.serialize.SerializerLoader;
import org.apache.rocketmq.client.exception.MQClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.GenericApplicationContext;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;


/**
 * 用于创建Consumer的Bean
 */
public class ConsumerFactory implements ApplicationContextAware {

  private RocketConfigProperties rocketConfigProperties;

  private static final Logger logger = LoggerFactory.getLogger(ConsumerFactory.class);

  private static GenericApplicationContext context;

  public ConsumerFactory(RocketConfigProperties rocketConfigProperties) {
    this.rocketConfigProperties = rocketConfigProperties;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    context = (GenericApplicationContext) applicationContext;
  }

  /**
   * Register Customer For Spring.
   */
  @PostConstruct
  public void initConsumer() {
    SerializerLoader serializerLoader = new SerializerLoader();
    Serializer serializer = serializerLoader.getSerializer();

    Map<String, Object> beans = context.getBeansWithAnnotation(RocketConsumer.class);

    beans.forEach((key, bean) -> {
      Class<?>[] interfaces = bean.getClass().getInterfaces();

      boolean hasReceiveMessageInterface = false;
      String messageTypeClassName = "";
      for (Class<?> anInterface : interfaces) {
        if (RocketConsumerReceiveMessage.class.getName().equals(anInterface.getName())) {
          hasReceiveMessageInterface = true;
          Type[] types = bean.getClass().getGenericInterfaces();
          for (Type type: types) {
            messageTypeClassName = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
          }
        }
      }

      if (!hasReceiveMessageInterface) {
        throw new RuntimeException("You have to implementing interface RocketConsumerReceiveMessage");
      }

      RocketConsumer annotation = bean.getClass().getAnnotation(RocketConsumer.class);
      storeConsumer(bean, annotation, messageTypeClassName, serializer);
    });

    try {
      ConsumerRegister.register(ConsumerFactory.class.getClassLoader());
    } catch (MQClientException e) {
      logger.error("Rocket Consumer Register Exception.", e);
    } catch (ClassNotFoundException e) {
      logger.error("Message Type Not Found.", e);
    }
  }

  private void storeConsumer(Object bean, RocketConsumer annotation, String messageTypeClassName, Serializer serializer) {
    String groupId = annotation.groupId();
    String topic = annotation.topic();
    String tags = annotation.tags();

    ConsumerInfo consumerInfo = new ConsumerInfo();
    consumerInfo.setTopic(topic);
    consumerInfo.setTags(tags);
    consumerInfo.setGroupId(groupId);
    consumerInfo.setClazz(bean.getClass());
    consumerInfo.setSerializer(serializer);
    consumerInfo.setMessageClazz(messageTypeClassName);
    consumerInfo.setNameServerAddr(rocketConfigProperties.getNameServerAddress());
    consumerInfo.setMessageType(annotation.messageType());
    ConsumerStore.addConsumerInfo(consumerInfo);
  }
}

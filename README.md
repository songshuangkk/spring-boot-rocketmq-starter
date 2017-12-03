# 阿里云ONS Spring boot starter 

## 使用方法

在application.yml文件中进行配置:
```javascript
mq:
  application:
      consumer:
        enable: true              // 是否开启消息消费
      provider:
        enable: true              // 是否开启消息发送
    config:
      ONSAddr: xxx (选填)          // ONS服务地址
      AccessKey: xxx (必填)        // ONS用户的access Key
      SecretKey: xxx (必填)        // ONS用户的secret Key
      myConsumerId: CID-ONS       // 可以在这里随便创建一个key名字并与ons上创建consumer ID对应作为value
      myProviderId: PID-ONS       // 可以在这里随便创建一个key名字并与ons上创建provider ID对应作为value
      myTopIc: TOPIC-ONS          // 可以在这里随便创建一个key名字并与ons上创建topic ID对应作为value
```

###使用注解
```java
  @MessageConsumer(topic=xxx, consumerId=xxx)
```

```java
  @MessageProvider(topic=xxx, producerId=xxx)
```

在使用的时候，服务消费放需要实现MessageConsumerService接口。服务消费放需要实现MessageProducerService接口并基础MessageBaseProducer类。

使用方式如下:

```java
@MessageConsumer(topic=xxx, consumerId=xxx)
@Component
public class Comsumer implements MessageConsumerService


@MessageProvider(topic=xxx, producerId=xxx)
@Component
public class Provider extends MessageBaseProducer implements MessageProducerService

```

服务消费放在使用的时候，只要调用sendMessage方法就能发送消息.


### 关于消息体
消息体的Bean我们可以自己定义。但是需要使用MessageBase进行包装。
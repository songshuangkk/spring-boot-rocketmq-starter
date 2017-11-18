package com.songshuang.ons.mq;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.songshuang.ons.mq.constants.MessageType;
import com.songshuang.ons.mq.message.MessageBase;

import java.util.UUID;

/**
 * 基本发送消息功能模块(暂时不支持事务消息发送).
 */
public class MessageBaseProducer {

  private Producer producer;

  private OrderProducer orderProducer;

  private String tag;

  private String topIc;

  /**
   * 发送消息.
   * @param message
   * @return
   */
  public SendResult sendMessage(MessageBase message) {

    String uuid = UUID.randomUUID().toString();

    String key = uuid.replace("-", "").toUpperCase();


    // 延时发送/定时发送
    if (message.getMessageType() == MessageType.TIMER.getCode() || message.getMessageType() == MessageType.DELAY.getCode()) {

      Message timerMessage = new Message(this.getTopIc(), this.getTag(), message.getSerializeMessage());

      timerMessage.setStartDeliverTime(message.getStartDeliverTime());

      timerMessage.setKey(key);

      return sendTimeOrDelayMessage(timerMessage);

    }

    // 正常顺序发送消息
    Message normalMessage = new Message(this.getTopIc(), this.getTag(), message.getSerializeMessage());

    normalMessage.setKey(key);

    return sendOrderMessage(normalMessage, key);

  }

  /**
   * 发送消息模块函数.
   * @param message         消息体
   * @return
   */
  private SendResult sendTimeOrDelayMessage(Message message) {

    return producer.send(message);

  }

  private SendResult sendOrderMessage(Message message, String shardingKey) {

    return orderProducer.send(message, shardingKey);

  }

  public Producer getProducer() {
    return producer;
  }

  public void setProducer(Producer producer) {
    this.producer = producer;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public String getTopIc() {
    return topIc;
  }

  public void setTopIc(String topIc) {
    this.topIc = topIc;
  }

  public OrderProducer getOrderProducer() {
    return orderProducer;
  }

  public void setOrderProducer(OrderProducer orderProducer) {
    this.orderProducer = orderProducer;
  }
}

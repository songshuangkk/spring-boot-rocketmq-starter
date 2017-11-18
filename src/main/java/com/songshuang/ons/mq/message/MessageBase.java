package com.songshuang.ons.mq.message;

import org.springframework.util.SerializationUtils;

/**
 *  发送消息封装对象.
 */
public class MessageBase<T> {

  /**
   * 发送消息的topic.
   */
  private String key;

  /**
   * 延迟的时间.
   */
  private Long delayTime;

  private T message;

  /**
   * 开始发送消息的时间
   */
  private Long startDeliverTime;

  private Integer messageType;

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public Long getDelayTime() {
    return delayTime;
  }

  public void setDelayTime(Long delayTime) {
    this.delayTime = delayTime;

    Long startDeliverTime = System.currentTimeMillis() + delayTime;
    // 设置延迟发送消息的时间
    setStartDeliverTime(startDeliverTime);
  }

  public Long getStartDeliverTime() {
    return startDeliverTime;
  }

  /**
   * 设置定时/延时发送消息时间
   * @param startDeliverTime
   */
  public void setStartDeliverTime(Long startDeliverTime) {
    this.startDeliverTime = startDeliverTime;
  }

  public Integer getMessageType() {
    return messageType;
  }

  public void setMessageType(Integer messageType) {
    this.messageType = messageType;
  }

  public T getMessage() {
    return message;
  }

  public void setMessage(T message) {
    this.message = message;
  }

  public byte[] getSerializeMessage() {
    return SerializationUtils.serialize(message);
  }
}

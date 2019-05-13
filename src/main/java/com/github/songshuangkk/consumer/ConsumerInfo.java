package com.github.songshuangkk.consumer;

import com.github.songshuangkk.constants.RocketMessageType;
import com.github.songshuangkk.serialize.Serializer;

import java.io.Serializable;

public class ConsumerInfo implements Serializable {

  private static final long serialVersionUID = 6985777849050163663L;

  private String nameServerAddr;

  private Class clazz;

  private String groupId;

  private String tags;

  private String topic;

  private RocketConsumerReceiveMessage rocketConsumerReceiveMessage;

  private Serializer serializer;

  private String messageClazz;

  private RocketMessageType messageType;

  public String getNameServerAddr() {
    return nameServerAddr;
  }

  public void setNameServerAddr(String nameServerAddr) {
    this.nameServerAddr = nameServerAddr;
  }

  public Class getClazz() {
    return clazz;
  }

  public void setClazz(Class clazz) {
    this.clazz = clazz;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getTags() {
    return tags;
  }

  public void setTags(String tags) {
    this.tags = tags;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Serializer getSerializer() {
    return serializer;
  }

  public void setSerializer(Serializer serializer) {
    this.serializer = serializer;
  }

  public RocketConsumerReceiveMessage getRocketConsumerReceiveMessage() {
    return rocketConsumerReceiveMessage;
  }

  public void setRocketConsumerReceiveMessage(RocketConsumerReceiveMessage rocketConsumerReceiveMessage) {
    this.rocketConsumerReceiveMessage = rocketConsumerReceiveMessage;
  }

  public String getMessageClazz() {
    return messageClazz;
  }

  public void setMessageClazz(String messageClazz) {
    this.messageClazz = messageClazz;
  }

  public RocketMessageType getMessageType() {
    return messageType;
  }

  public void setMessageType(RocketMessageType messageType) {
    this.messageType = messageType;
  }

  @Override
  public String toString() {
    return "consumer = "+clazz+", topic - "+topic+"; groupId - "+groupId+" tags - "+tags;
  }
}

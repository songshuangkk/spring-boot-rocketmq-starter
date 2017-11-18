package com.songshuang.ons.mq.constants;

/**
 * 消息类型.
 */
public enum MessageType {

  NORMAL(1, "普通消息"),
  DELAY(2, "延迟消息"),
  TIMER(3, "定时消息"),
  TRANSACTION(4, "事务消息");

  private int code;

  private String desc;

  MessageType(int code, String desc) {

    this.code = code;

    this.desc = desc;

  }

  public int getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

}

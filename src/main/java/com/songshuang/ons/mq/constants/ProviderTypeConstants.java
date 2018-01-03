package com.songshuang.ons.mq.constants;

public interface ProviderTypeConstants {

  // 普通正常消息
  Integer NORMAL = 1;
  // 延迟消息
  Integer DELAY = 2;
  // 定时消息
  Integer TIMER = 3;
  // 事务消息
  Integer TRANSACTION = 4;
}

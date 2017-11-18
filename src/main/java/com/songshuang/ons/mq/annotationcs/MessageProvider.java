package com.songshuang.ons.mq.annotationcs;

import java.lang.annotation.*;

/**
 * 消息生产者注解.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageProvider {

  /**
   * 队列名字.
   * @return
   */
  String topic();

  /**
   * 消息组.
   * @return
   */
  String group() default "";

  /**
   * 消息生产者类型.
   * @return
   */
  int providerType();

  /**
   * 消息tag.
   * @return
   */
  String tag();

  /**
   * 消息发送者ID.
   * @return
   */
  String producerId();
}

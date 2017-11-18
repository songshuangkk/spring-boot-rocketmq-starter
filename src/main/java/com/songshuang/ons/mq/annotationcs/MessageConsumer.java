package com.songshuang.ons.mq.annotationcs;

import java.lang.annotation.*;

/**
 * 消息消费者注解.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MessageConsumer {

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
   * 消息tag.
   * @return
   */
  String tag() default "";

  /**
   * 消息消费ID.
   * @return
   */
  String consumerId();
}

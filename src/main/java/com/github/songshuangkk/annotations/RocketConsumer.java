package com.github.songshuangkk.annotations;

import com.github.songshuangkk.constants.RocketMessageType;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RocketConsumer {

  String groupId();

  String tags() default "";

  String topic();

  RocketMessageType messageType();
}

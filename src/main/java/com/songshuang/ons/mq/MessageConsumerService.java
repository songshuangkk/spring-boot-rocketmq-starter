package com.songshuang.ons.mq;

public interface MessageConsumerService<T> {


  void receiveMessage(T message);
}

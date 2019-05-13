package com.github.songshuangkk.consumer;


import java.util.ArrayList;
import java.util.List;

public class ConsumerStore {

  private static List<ConsumerInfo> store = new ArrayList<>();

  static void addConsumerInfo(ConsumerInfo consumerInfo) {
    store.add(consumerInfo);
  }

  static List<ConsumerInfo> getConsumerList() {
    return store;
  }
}

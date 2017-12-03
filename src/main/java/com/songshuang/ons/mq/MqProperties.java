package com.songshuang.ons.mq;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Properties;

/**
 * 消息队列配置信息.
 */
@ConfigurationProperties(prefix = "mq.application")
public class MqProperties {

  private String[] consumer;

  private String[] provider;

  private Properties config;

  public String[] getConsumer() {
    return consumer;
  }

  public void setConsumer(String[] consumer) {
    this.consumer = consumer;
  }

  public String[] getProvider() {
    return provider;
  }

  public void setProvider(String[] provider) {
    this.provider = provider;
  }

  public Properties getConfig() {
    return config;
  }

  public void setConfig(Properties config) {
    this.config = config;
  }
}

package com.github.songshuangkk.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rocket.mq")
public class RocketConfigProperties {

  private String nameServerAddress;

  private String serialize;

  public String getNameServerAddress() {
    return nameServerAddress;
  }

  public void setNameServerAddress(String nameServerAddress) {
    this.nameServerAddress = nameServerAddress;
  }

  public String getSerialize() {
    return serialize;
  }

  public void setSerialize(String serialize) {
    this.serialize = serialize;
  }
}

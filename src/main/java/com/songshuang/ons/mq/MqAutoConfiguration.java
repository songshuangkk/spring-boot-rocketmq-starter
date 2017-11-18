package com.songshuang.ons.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
@EnableConfigurationProperties(MqProperties.class)
public class MqAutoConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(MqAutoConfiguration.class);

  @Autowired
  private MqProperties mqProperties;

  @Bean
  @ConditionalOnProperty(value = "mq.meipingmi.consumer.enable", havingValue = "true")
  public MqConsumerRunner mqConsumerRunner() {

    return new MqConsumerRunner();

  }

  @Bean
  @ConditionalOnProperty(value = "mq.meipingmi.provider.enable", havingValue = "true")
  public MqProviderRunner mqProviderRunner() {

    return new MqProviderRunner();

  }

}

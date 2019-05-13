package com.github.songshuangkk.autoconfig;

import com.github.songshuangkk.consumer.ConsumerFactory;
import com.github.songshuangkk.properties.RocketConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@EnableConfigurationProperties(RocketConfigProperties.class)
@Configuration
public class RocketAutoConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(RocketAutoConfiguration.class);

  @Resource
  private RocketConfigProperties rocketConfigProperties;

  @Bean
  public ConsumerFactory initConsumerFactory() {
    logger.info("Initializing Rocket Consumer Factory...");
    return new ConsumerFactory(rocketConfigProperties);
  }

}

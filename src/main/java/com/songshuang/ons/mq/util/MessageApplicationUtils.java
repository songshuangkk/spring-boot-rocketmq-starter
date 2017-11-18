package com.songshuang.ons.mq.util;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.StandardMethodMetadata;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.stream.Stream;

public class MessageApplicationUtils {

  public static <T> Stream<String> getBeanNamesByTypeWithAnnotation (Class<T> beanType,
                                                                     Class<? extends Annotation> annotationType,
                                                                     AbstractApplicationContext applicationContext) {

    return Stream.of(applicationContext.getBeanNamesForType(beanType))
            .filter(name -> {
              final BeanDefinition beanDefinition = applicationContext.getBeanFactory().getBeanDefinition(name);
              final Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(annotationType);

              if (!beansWithAnnotation.isEmpty()) {
                return beansWithAnnotation.containsKey(name);
              } else if (beanDefinition.getSource() instanceof StandardMethodMetadata) {
                StandardMethodMetadata metadata = (StandardMethodMetadata) beanDefinition.getSource();
                return metadata.isAnnotated(annotationType.getName());
              }

              return false;
            });
  }

  public static <T> Stream<String> getBeanNamesByAnnotation (Class<? extends Annotation> annotationType,
                                                             AbstractApplicationContext applicationContext) {
    return Stream.of(applicationContext.getBeanNamesForAnnotation(annotationType))
            .filter(name -> {

              final Map<String, Object> annotation = applicationContext.getBeansWithAnnotation(annotationType);

              if (!annotation.isEmpty()) {
                return annotation.containsKey(name);
              }

              return false;
            });
  }
}

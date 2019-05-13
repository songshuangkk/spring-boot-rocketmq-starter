package com.github.songshuangkk.annotations;


import com.github.songshuangkk.consumer.ConsumerInfo;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  Change RocketConsumer Annotations Retention To RetentionPolicy.RUNTIME.
 *
 *  Change if you want execute RetentionPolicy.SOURCE.
 */

@Deprecated
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.github.songshuangkk.annotations.RocketConsumer"})
public class RocketAnnotationsProcessor extends AbstractProcessor {

  @Override
  public SourceVersion getSupportedSourceVersion() {
    if (SourceVersion.latest().compareTo(SourceVersion.RELEASE_8) > 0) {
      return SourceVersion.latest();
    } else {
      return SourceVersion.RELEASE_8;
    }
  }


  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    printRoundEnv(roundEnv);
    return true;
  }

  private void printRoundEnv(RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(RocketConsumer.class);
    List<ConsumerInfo> list = new ArrayList<>();
    for (Element item : elements) {
      ConsumerInfo consumerInfo = buildConsumerInfo(item);
      list.add(consumerInfo);
    }

    list.forEach(item -> {
      System.out.printf("[INFO] RocketMQ Consumer: %s \n", item);
    });
  }

  private ConsumerInfo buildConsumerInfo(Element item) {
    RocketConsumer annotation = item.getAnnotation(RocketConsumer.class);
    ConsumerInfo consumerInfo = new ConsumerInfo();
    consumerInfo.setGroupId(annotation.groupId());
    consumerInfo.setTags(annotation.tags());
    consumerInfo.setTopic(annotation.topic());
    return consumerInfo;
  }
}

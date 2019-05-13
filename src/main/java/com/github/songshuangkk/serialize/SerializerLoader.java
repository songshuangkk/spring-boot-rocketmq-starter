package com.github.songshuangkk.serialize;

import java.util.ServiceLoader;

public class SerializerLoader {

  private static final String SERVICES_DIRECTORY = "META-INF/services/";

  public Serializer getSerializer() {

    Serializer serializer = null;

    ServiceLoader<Serializer> serviceLoader = ServiceLoader.load(Serializer.class);

    for (Serializer value : serviceLoader) {
      serializer = value;
    }

    return serializer;
  }
}

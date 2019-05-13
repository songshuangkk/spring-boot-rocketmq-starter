package com.github.songshuangkk.serialize;

public interface Serializer {

  byte[] serialize(Object serializedObject);

  <T> T deserialize(byte[] bytes, Class<T> tClass);
}

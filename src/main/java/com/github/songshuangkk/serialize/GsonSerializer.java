package com.github.songshuangkk.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonSerializer implements Serializer {

  public byte[] serialize(Object serializedObject) {
    Gson gson = new GsonBuilder().create();
    return gson.toJson(serializedObject).getBytes();
  }

  @Override
  public <T> T deserialize(byte[] bytes, Class<T> tClass) {
    String str = new String(bytes);
    Gson gson = new GsonBuilder().create();
    return gson.fromJson(str, tClass);
  }
}

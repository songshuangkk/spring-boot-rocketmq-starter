package com.github.songshuangkk.serialize;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoFactory;
import com.esotericsoftware.kryo.pool.KryoPool;

import java.io.ByteArrayOutputStream;

public class KryoSerializer implements Serializer {


  private static KryoPool pool;
  static {
    KryoFactory factory = new KryoFactory() {
      public Kryo create() {
        return new Kryo();
      }
    };
    pool = new KryoPool.Builder(factory).softReferences().build();
  }

  @Override
  public byte[] serialize(Object serializedObject) {
    Kryo kryo = pool.borrow();
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      Output output = new Output(byteArrayOutputStream);

      kryo.writeObject(output, serializedObject);

      output.close();
      return byteArrayOutputStream.toByteArray();
    } finally {
      pool.release(kryo);
    }
  }

  @Override
  public <T> T deserialize(byte[] bytes, Class<T> tClass) {
    Kryo kryo = pool.borrow();
    try {
      Input input = new Input(bytes);
      return kryo.readObject(input, tClass);
    } finally {
      pool.release(kryo);
    }
  }

}

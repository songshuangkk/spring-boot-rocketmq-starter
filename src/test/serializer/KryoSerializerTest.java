package serializer;

import com.github.songshuangkk.serialize.KryoSerializer;
import com.github.songshuangkk.serialize.Serializer;

public class KryoSerializerTest {

  public static void main(String[] args) {

    Serializer serializer = new KryoSerializer();

    KryoTestMessage kryoTestMessage = new KryoTestMessage();
    kryoTestMessage.setMessage("12121s+sfs{}");

    byte[] bytes = serializer.serialize(kryoTestMessage);

    KryoTestMessage message = serializer.deserialize(bytes, KryoTestMessage.class);

    System.out.printf("%s\n", message.toString());
  }

  static class KryoTestMessage {

    private String message;

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return "message = " + message;
    }
  }
}

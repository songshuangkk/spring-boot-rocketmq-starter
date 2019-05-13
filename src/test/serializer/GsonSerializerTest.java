package serializer;

import com.github.songshuangkk.serialize.GsonSerializer;
import com.github.songshuangkk.serialize.Serializer;

public class GsonSerializerTest {

  public static void main(String[] args) {
    Serializer serializer = new GsonSerializer();
    GsonTestMessage gsonTestMessage = new GsonTestMessage();
    gsonTestMessage.setMessage("sdfsdfsdfsdfsdf{}");
    byte[] bytes = serializer.serialize(gsonTestMessage);

    GsonTestMessage result = serializer.deserialize(bytes, GsonTestMessage.class);
    System.out.printf("Result = %s\n", result.toString());
  }

  static class GsonTestMessage {

    private String message;

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }
  }
}

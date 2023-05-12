import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Executable class for demonstrating this simple weather application.
 */
public class Main {
  private static final OkHttpClient client = new OkHttpClient();
  private static final String CREDENTIALS_FILEPATH = "/credentials.json";


  /**
   * Main executable method.
   *
   * @param args command line arguments
   */
  public static void main(String[] args) {
    try {
      run("Phoenix");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Executes a GET request with the parameter city name.
   *
   * @param cityName name of the city being requested
   * @throws IOException in the case of an input/output error
   */
  public static void run(String cityName) throws IOException {
    Request request = new Request.Builder()
        .url("https://api.openweathermap.org/data/2.5/weather?units=imperial&q=" + cityName
            + "&appid=" + getKey())
        .build();

    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

      Headers responseHeaders = response.headers();
      for (int i = 0; i < responseHeaders.size(); i++) {
        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
      }

      assert response.body() != null;
      //System.out.println(response.body().string());
      prettyPrint(response.body().string());
    }
  }

  /**
   * Retrieves the API key from a credentials.json file located in resources directory.
   *
   * @return the API key as a string
   */
  private static String getKey() {
    String key = "";

    try (InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILEPATH)) {
      assert in != null;
      Scanner s = new Scanner(in).useDelimiter("\\A");

      String result = s.hasNext() ? s.next() : "";
      JsonElement jsonElement = JsonParser.parseString(result);
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      key = jsonObject.get("key").getAsString();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return key;
  }

  /**
   * Pretty prints the data retrieved from the request.
   *
   * @param data the GET request data in String format.
   */
  private static void prettyPrint(String data) {
    JsonElement jsonElement = JsonParser.parseString(data);
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    JsonArray weather = jsonObject.getAsJsonArray("weather");
    JsonObject main = jsonObject.getAsJsonObject("main");

    String cityName = jsonObject.get("name").getAsString();
    String description = weather.get(0).getAsJsonObject().get("description").getAsString();
    String temperature = main.get("temp").getAsString();

    System.out.printf("City: %s%nDescription: %s%nTemperature (F): %s%n", cityName, description,
        temperature);
  }
}

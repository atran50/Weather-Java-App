import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import okhttp3.*;

import java.io.IOException;

public class Main {
    private static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private static final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run() throws IOException {
        Request request = new Request.Builder()
                .url("https://publicobject.com/helloworld.txt")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }

            assert response.body() != null;
            System.out.println(response.body().string());
        }
    }

    public static String getKey() {
        String key = "";

        return key;
    }
}

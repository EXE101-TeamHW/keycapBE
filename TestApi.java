import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.util.Scanner;

public class TestApi {
    public static void main(String[] args) {
        try {
            URL url = new URL("http://localhost:8080/api/admin/orders");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);
            
            InputStream stream = responseCode >= 400 ? conn.getErrorStream() : conn.getInputStream();
            if (stream != null) {
                try (Scanner scanner = new Scanner(stream, "UTF-8").useDelimiter("\\A")) {
                    String response = scanner.hasNext() ? scanner.next() : "";
                    System.out.println("Response Body:");
                    System.out.println(response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

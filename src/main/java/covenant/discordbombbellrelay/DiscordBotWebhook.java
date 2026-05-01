package covenant.discordbombbellrelay;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscordBotWebhook {

    // ✅ prevents lag + avoids infinite threads
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);

    // ✅ CALL THIS FROM YOUR CLIENT
    public static void sendAsync(String webhookUrl, String type, String server) {
        EXECUTOR.submit(() -> {
            try {
                send(webhookUrl, type, server);
            } catch (Exception e) {
                System.out.println("❌ Webhook failed: " + e.getMessage());
            }
        });
    }

    // 🔒 internal method (do NOT call directly)
    private static void send(String webhookUrl, String type, String server) throws Exception {

        URL url = new URL(webhookUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        // ✅ matches your Node bot
        String json = String.format(
                "{\"type\":\"%s\",\"server\":\"%s\",\"duration\":10}",
                type,
                server
        );

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = json.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();

        if (responseCode != 200 && responseCode != 204) {
            System.out.println("❌ Bad response: " + responseCode);
        } else {
            System.out.println("✅ Sent bomb → bot (" + type + " " + server + ")");
        }

        conn.disconnect();
    }
}

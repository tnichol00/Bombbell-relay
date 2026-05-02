package covenant.discordbombbellrelay;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DiscordBotWebhook {

    public static void sendAsync(String webhookUrl, String type, String server) {

        System.out.println("📤 Queuing webhook send: " + type + " " + server);

        Thread thread = new Thread(() -> {
            try {
                System.out.println("🚀 Sending webhook NOW...");

                send(webhookUrl, type, server);

            } catch (Exception e) {
                System.out.println("❌ Webhook thread crashed:");
                e.printStackTrace();
            }
        });

        thread.setDaemon(true); // prevents hanging threads
        thread.start();
    }

    private static void send(String webhookUrl, String type, String server) {
        try {
            if (type == null || server == null) {
                System.out.println("❌ NULL DATA BLOCKED SEND");
                return;
            }

            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = String.format(
                    "{\"type\":\"%s\",\"server\":\"%s\",\"duration\":10}",
                    type,
                    server
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            System.out.println("📡 Response: " + code);

            conn.disconnect();

        } catch (Exception e) {
            System.out.println("❌ SEND FAILED:");
            e.printStackTrace();
        }
    }
}

package covenant.discordbombbellrelay;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class DiscordWebhook {

    public static void send(String webhookUrl, String username, String bombType, String server, String startTime, String endTime) {
        try {
            // escape strings
            String safeUsername = escape(username);
            String safeBomb = escape(bombType);
            String safeServer = escape(server);
            String safeStart = escape(startTime);
            String safeEnd = escape(endTime);

            // 🔥 Embed JSON (clean + nice)
            String jsonPayload = String.format("""
            {
              "username": "%s",
              "embeds": [
                {
                  "title": "💣 %s",
                  "color": 16753920,
                  "fields": [
                    { "name": "Server", "value": "%s", "inline": true },
                    { "name": "Started", "value": "%s", "inline": true },
                    { "name": "Expires", "value": "%s", "inline": true }
                  ]
                }
              ]
            }
            """, safeUsername, safeBomb, safeServer, safeStart, safeEnd);

            HttpURLConnection connection = (HttpURLConnection)
                    URI.create(webhookUrl).toURL().openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();

            if (responseCode != 204) {
                DiscordBombbellRelay.LOGGER.warn("Webhook failed: {}", responseCode);
            }

            connection.disconnect();

        } catch (Exception e) {
            DiscordBombbellRelay.LOGGER.error("Webhook error: {}", e.getMessage());
        }
    }

    // 🔐 escape helper
    private static String escape(String input) {
        return input.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
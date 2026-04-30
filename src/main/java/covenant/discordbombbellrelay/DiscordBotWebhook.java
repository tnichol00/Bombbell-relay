package covenant.discordbombbellrelay;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class DiscordBotWebhook {

    public static void send(String type, String server, int duration) {
        try {
            String json = String.format(
                    "{\"type\":\"%s\",\"server\":\"%s\",\"duration\":%d}",
                    type, server, duration
            );

            HttpURLConnection conn = (HttpURLConnection)
                    URI.create(ModConfig.getBotUrl()).toURL().openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            DiscordBombbellRelay.LOGGER.info("Sent bomb → bot ({})", code);

            conn.disconnect();

        } catch (Exception e) {
            DiscordBombbellRelay.LOGGER.error("Failed to send bomb: {}", e.getMessage());
        }
    }
}

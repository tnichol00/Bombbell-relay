package covenant.discordbombbellrelay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class DiscordBotWebhook {

    private static final Logger LOGGER = LoggerFactory.getLogger("discordbombbellrelay");

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
            LOGGER.info("Sent bomb → bot ({})", code);

            conn.disconnect();

        } catch (Exception e) {
            LOGGER.error("Failed to send bomb: {}", e.getMessage());
        }
    }
}

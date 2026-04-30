package covenant.discordbombbellrelay;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {

    private static final Path CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("discord-bombbell-relay.txt");

    private static String webhookUrl = "";

    public static String getWebhookUrl() {
        return webhookUrl;
    }

    public static void setWebhookUrl(String url) {
        webhookUrl = url;
        save();
    }

    public static void load() {
        if (Files.exists(CONFIG_FILE)) {
            try {
                webhookUrl = Files.readString(CONFIG_FILE).trim();
                DiscordBombbellRelay.LOGGER.info("Loaded webhook URL from config.");
            } catch (IOException e) {
                DiscordBombbellRelay.LOGGER.error("Failed to load config: {}", e.getMessage());
            }
        }
    }

    private static void save() {
        try {
            Files.writeString(CONFIG_FILE, webhookUrl);
            DiscordBombbellRelay.LOGGER.info("Saved webhook URL to config.");
        } catch (IOException e) {
            DiscordBombbellRelay.LOGGER.error("Failed to save config: {}", e.getMessage());
        }
    }
}
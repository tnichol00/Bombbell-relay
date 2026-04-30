package covenant.discordbombbellrelay;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordBombbellRelay implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("discordbombbellrelay");

    private static final Pattern SERVER_PATTERN = Pattern.compile("(NA|EU|AS)\\d+");
    private static final Pattern BOMB_PATTERN = Pattern.compile("has thrown a (.+?) on");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Bombbell Relay started.");

        ClientReceiveMessageEvents.CHAT.register((message, signed, sender, params, ts) ->
                handleMessage(message.getString())
        );

        ClientReceiveMessageEvents.GAME.register((message, overlay) ->
                handleMessage(message.getString())
        );
    }

    private void handleMessage(String msg) {

        if (!msg.contains("has thrown")) return;

        Matcher serverMatcher = SERVER_PATTERN.matcher(msg);
        if (!serverMatcher.find()) return;
        String server = serverMatcher.group();

        Matcher bombMatcher = BOMB_PATTERN.matcher(msg);

        String bombType = "Unknown Bomb";
        if (bombMatcher.find()) {
            bombType = bombMatcher.group(1).trim();
        }

        int duration = 20;

        // special cases
        if (bombType.contains("Profession Speed") || bombType.contains("Dungeon")) {
            duration = 10;
        }

        LOGGER.info("Detected bomb: {} on {}", bombType, server);

        DiscordBotWebhook.send(bombType, server, duration);
    }
}

package covenant.discordbombbellrelay.client;

import covenant.discordbombbellrelay.DiscordBombbellRelay;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.io.OutputStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DiscordBombbellRelayClient implements ClientModInitializer {

	private static final Pattern SERVER_PATTERN = Pattern.compile("(NA|EU|AS)\\d+");
	private static final Pattern BOMB_PATTERN = Pattern.compile("has thrown a (.+?) on");

	@Override
	public void onInitializeClient() {
		DiscordBombbellRelay.LOGGER.info("Bomb relay client started.");

		ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, ts) ->
				handleMessage(message.getString())
		);

		ClientReceiveMessageEvents.GAME.register((message, overlay) ->
				handleMessage(message.getString())
		);
	}

	private void handleMessage(String content) {

		if (!content.contains("has thrown")) return;

		Matcher serverMatcher = SERVER_PATTERN.matcher(content);
		if (!serverMatcher.find()) return;
		String server = serverMatcher.group();

		Matcher bombMatcher = BOMB_PATTERN.matcher(content);

		String bombType = "Unknown Bomb";
		if (bombMatcher.find()) {
			bombType = bombMatcher.group(1).trim();
		}

		int duration = 20;
		if (bombType.contains("Profession Speed") || bombType.contains("Dungeon")) {
			duration = 10;
		}

		sendToBot(bombType, server, duration);
	}

	private void sendToBot(String type, String server, int duration) {
		try {
			String json = String.format(
					"{\"type\":\"%s\",\"server\":\"%s\",\"duration\":%d}",
					type, server, duration
			);

			HttpURLConnection conn = (HttpURLConnection)
					URI.create("http://fi3.bot-hosting.net:22139/bomb").toURL().openConnection();

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
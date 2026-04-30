package covenant.discordbombbellrelay;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscordBombbellRelay implements ModInitializer {
	public static final String MOD_ID = "discord-bombbell-relay";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfig.load();
		LOGGER.info("Discord Bombbell Relay initialized.");
	}
}
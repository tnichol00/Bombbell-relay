package covenant.discordbombbellrelay;

public class ModConfig {

    private static String botUrl = "http://fi3.bot-hosting.net:22139/bomb";

    public static String getBotUrl() {
        return botUrl;
    }

    public static void setBotUrl(String url) {
        botUrl = url;
    }
}

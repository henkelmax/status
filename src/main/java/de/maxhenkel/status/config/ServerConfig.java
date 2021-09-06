package de.maxhenkel.status.config;

public class ServerConfig {

    public final ConfigBuilder.ConfigEntry<String> noSleepTitle;
    public final ConfigBuilder.ConfigEntry<String> noSleepPlayerSubtitle;
    public final ConfigBuilder.ConfigEntry<String> noSleepMultipleSubtitle;

    public ServerConfig(ConfigBuilder builder) {
        noSleepTitle = builder.stringEntry("no_sleep_title", "No Sleep");
        noSleepPlayerSubtitle = builder.stringEntry("no_sleep_player_subtitle", "%s does not want you to sleep");
        noSleepMultipleSubtitle = builder.stringEntry("no_sleep_multiple_subtitle", "Some players do not want you to sleep");
    }

}
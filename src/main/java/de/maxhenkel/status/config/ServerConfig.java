package de.maxhenkel.status.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.ConfigEntry;

public class ServerConfig {

    public final ConfigEntry<String> noSleepTitle;
    public final ConfigEntry<String> noSleepPlayerSubtitle;
    public final ConfigEntry<String> noSleepMultipleSubtitle;

    public ServerConfig(ConfigBuilder builder) {
        noSleepTitle = builder.stringEntry("no_sleep_title", "No Sleep");
        noSleepPlayerSubtitle = builder.stringEntry("no_sleep_player_subtitle", "%s does not want you to sleep");
        noSleepMultipleSubtitle = builder.stringEntry("no_sleep_multiple_subtitle", "Some players do not want you to sleep");
    }

}
package de.maxhenkel.status.config;

import de.maxhenkel.status.playerstate.Availability;

public class ClientConfig {

    public final ConfigBuilder.ConfigEntry<Availability> availability;
    public final ConfigBuilder.ConfigEntry<String> status;
    public final ConfigBuilder.ConfigEntry<Boolean> noSleep;
    public final ConfigBuilder.ConfigEntry<Boolean> persistState;
    public final ConfigBuilder.ConfigEntry<Boolean> showJoinMessage;

    public ClientConfig(ConfigBuilder builder) {
        availability = builder.enumEntry("availability", Availability.NONE);
        status = builder.stringEntry("status", "");
        noSleep = builder.booleanEntry("no_sleep", false);
        persistState = builder.booleanEntry("persist_state", false);
        showJoinMessage = builder.booleanEntry("show_join_message", true);
    }

}

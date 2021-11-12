package de.maxhenkel.status.config;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.configbuilder.ConfigEntry;
import de.maxhenkel.status.playerstate.Availability;

public class ClientConfig {

    public final ConfigEntry<Availability> availability;
    public final ConfigEntry<String> status;
    public final ConfigEntry<Boolean> noSleep;
    public final ConfigEntry<Boolean> persistState;
    public final ConfigEntry<Boolean> showJoinMessage;

    public ClientConfig(ConfigBuilder builder) {
        availability = builder.enumEntry("availability", Availability.NONE);
        status = builder.stringEntry("status", "");
        noSleep = builder.booleanEntry("no_sleep", false);
        persistState = builder.booleanEntry("persist_state", false);
        showJoinMessage = builder.booleanEntry("show_join_message", true);
    }

}

package de.maxhenkel.status.config;

import de.maxhenkel.status.playerstate.Availability;

public class ClientConfig {

    public final ConfigBuilder.ConfigEntry<Availability> availability;
    public final ConfigBuilder.ConfigEntry<String> status;

    public ClientConfig(ConfigBuilder builder) {
        availability = builder.enumEntry("availability", Availability.NONE);
        status = builder.stringEntry("status", "");
    }

}

package de.maxhenkel.status.playerstate;

import net.minecraft.ChatFormatting;

public enum Availability {

    NONE("", "message.status.no_availability", ChatFormatting.WHITE),
    DO_NOT_DISTURB("do_not_disturb", "message.status.do_not_disturb", ChatFormatting.RED),
    OPEN("open", "message.status.open", ChatFormatting.GREEN);

    private final String name;
    private final String translationKey;
    private final ChatFormatting color;

    Availability(String name, String translationKey, ChatFormatting color) {
        this.name = name;
        this.translationKey = translationKey;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public ChatFormatting getColor() {
        return color;
    }

    public static Availability byName(String name) {
        for (Availability availability : values()) {
            if (availability.getName().equals(name)) {
                return availability;
            }
        }
        return NONE;
    }
}

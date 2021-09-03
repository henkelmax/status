package de.maxhenkel.status.playerstate;

public enum Availability {

    NONE(""), DO_NOT_DISTURB("do_not_disturb"), OPEN("open");

    private final String name;

    Availability(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

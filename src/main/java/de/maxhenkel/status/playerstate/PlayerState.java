package de.maxhenkel.status.playerstate;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class PlayerState {

    private UUID player;
    private Availability availability;
    private String state;

    public PlayerState(UUID player) {
        this(player, Availability.NONE, "");
    }

    public PlayerState(UUID player, Availability availability, String state) {
        this.player = player;
        this.availability = availability;
        this.state = state;
    }

    public UUID getPlayer() {
        return player;
    }

    public void setPlayer(UUID player) {
        this.player = player;
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static PlayerState fromBytes(FriendlyByteBuf buf) {
        PlayerState state = new PlayerState(buf.readUUID());
        state.availability = Availability.byName(buf.readUtf(128));
        state.state = buf.readUtf(128);
        return state;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(player);
        buf.writeUtf(availability.getName(), 128);
        buf.writeUtf(state, 128);
    }

}

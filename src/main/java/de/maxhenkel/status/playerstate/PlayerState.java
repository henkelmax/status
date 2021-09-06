package de.maxhenkel.status.playerstate;

import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class PlayerState {

    private UUID player;
    private Availability availability;
    private String state;
    private boolean noSleep;

    public PlayerState(UUID player) {
        this(player, Availability.NONE, "", false);
    }

    public PlayerState(UUID player, Availability availability, String state, boolean noSleep) {
        this.player = player;
        this.availability = availability;
        this.state = state;
        this.noSleep = noSleep;
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

    public boolean isNoSleep() {
        return noSleep;
    }

    public void setNoSleep(boolean noSleep) {
        this.noSleep = noSleep;
    }

    public static PlayerState fromBytes(FriendlyByteBuf buf) {
        PlayerState state = new PlayerState(buf.readUUID());
        state.availability = Availability.byName(buf.readUtf(128));
        state.state = buf.readUtf(128);
        state.noSleep = buf.readBoolean();
        return state;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUUID(player);
        buf.writeUtf(availability.getName(), 128);
        buf.writeUtf(state, 128);
        buf.writeBoolean(noSleep);
    }

}

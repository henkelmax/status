package de.maxhenkel.status.net;

import de.maxhenkel.status.Status;
import de.maxhenkel.status.playerstate.PlayerState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerStatesPacket implements Packet<PlayerStatesPacket> {

    public static final Type<PlayerStatesPacket> PLAYER_STATES = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Status.MODID, "states"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerStatesPacket> CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, PlayerStatesPacket packet) {
            packet.toBytes(buf);
        }

        @Override
        public PlayerStatesPacket decode(RegistryFriendlyByteBuf buf) {
            PlayerStatesPacket packet = new PlayerStatesPacket();
            packet.fromBytes(buf);
            return packet;
        }
    };

    private Map<UUID, PlayerState> playerStates;

    public PlayerStatesPacket() {

    }

    public PlayerStatesPacket(Map<UUID, PlayerState> playerStates) {
        this.playerStates = playerStates;
    }

    public Map<UUID, PlayerState> getPlayerStates() {
        return playerStates;
    }

    @Override
    public PlayerStatesPacket fromBytes(RegistryFriendlyByteBuf buf) {
        playerStates = new HashMap<>();
        int count = buf.readInt();
        for (int i = 0; i < count; i++) {
            PlayerState playerState = PlayerState.fromBytes(buf);
            playerStates.put(playerState.getPlayer(), playerState);
        }

        return this;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        buf.writeInt(playerStates.size());
        for (Map.Entry<UUID, PlayerState> entry : playerStates.entrySet()) {
            entry.getValue().toBytes(buf);
        }
    }

    @Override
    public Type<PlayerStatesPacket> type() {
        return PLAYER_STATES;
    }

}

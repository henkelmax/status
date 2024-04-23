package de.maxhenkel.status.net;

import de.maxhenkel.status.Status;
import de.maxhenkel.status.playerstate.PlayerState;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class PlayerStatePacket implements Packet<PlayerStatePacket> {

    public static final Type<PlayerStatePacket> PLAYER_STATE = new CustomPacketPayload.Type<>(new ResourceLocation(Status.MODID, "state"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerStatePacket> CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, PlayerStatePacket packet) {
            packet.toBytes(buf);
        }

        @Override
        public PlayerStatePacket decode(RegistryFriendlyByteBuf buf) {
            PlayerStatePacket packet = new PlayerStatePacket();
            packet.fromBytes(buf);
            return packet;
        }
    };

    private PlayerState playerState;

    public PlayerStatePacket() {

    }

    public PlayerStatePacket(PlayerState playerState) {
        this.playerState = playerState;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    @Override
    public PlayerStatePacket fromBytes(RegistryFriendlyByteBuf buf) {
        playerState = PlayerState.fromBytes(buf);
        return this;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buf) {
        playerState.toBytes(buf);
    }

    @Override
    public Type<PlayerStatePacket> type() {
        return PLAYER_STATE;
    }

}

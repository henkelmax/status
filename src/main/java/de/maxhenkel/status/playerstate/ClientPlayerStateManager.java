package de.maxhenkel.status.playerstate;

import de.maxhenkel.status.Status;
import de.maxhenkel.status.StatusClient;
import de.maxhenkel.status.events.ClientWorldEvents;
import de.maxhenkel.status.net.NetManager;
import de.maxhenkel.status.net.PlayerStatePacket;
import de.maxhenkel.status.net.PlayerStatesPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ClientPlayerStateManager {

    private PlayerState state;
    private boolean stateChanged;
    private Map<UUID, PlayerState> states;

    public ClientPlayerStateManager() {
        state = getDefaultState();
        states = new HashMap<>();
        NetManager.registerClientReceiver(PlayerStatePacket.class, (client, handler, responseSender, packet) -> {
            states.put(packet.getPlayerState().getPlayer(), packet.getPlayerState());
        });
        NetManager.registerClientReceiver(PlayerStatesPacket.class, (client, handler, responseSender, packet) -> {
            states = packet.getPlayerStates();
        });
        ClientWorldEvents.DISCONNECT.register(this::onDisconnect);
        ClientWorldEvents.JOIN_SERVER.register(this::onConnect);
    }

    public String getState() {
        return state.getState();
    }

    public void setState(String s) {
        state.setState(s);
        syncOwnState();
        StatusClient.CLIENT_CONFIG.status.set(s);
        StatusClient.CLIENT_CONFIG.status.save();
        stateChanged = true;
    }

    public void setAvailability(Availability availability) {
        state.setAvailability(availability);
        syncOwnState();
        StatusClient.CLIENT_CONFIG.availability.set(availability);
        StatusClient.CLIENT_CONFIG.availability.save();
        stateChanged = true;
    }

    public Availability getAvailability() {
        return state.getAvailability();
    }

    public boolean getNoSleep() {
        return state.isNoSleep();
    }

    public void setNoSleep(boolean noSleep) {
        state.setNoSleep(noSleep);
        syncOwnState();
        StatusClient.CLIENT_CONFIG.noSleep.set(noSleep);
        StatusClient.CLIENT_CONFIG.noSleep.save();
        stateChanged = true;
    }

    private PlayerState getDefaultState() {
        if (StatusClient.CLIENT_CONFIG.persistState.get()) {
            return new PlayerState(Minecraft.getInstance().getUser().getGameProfile().getId(), StatusClient.CLIENT_CONFIG.availability.get(), StatusClient.CLIENT_CONFIG.status.get(), StatusClient.CLIENT_CONFIG.noSleep.get());
        } else {
            return new PlayerState(Minecraft.getInstance().getUser().getGameProfile().getId());
        }
    }

    private void onDisconnect() {
        clearStates();
    }

    private void onConnect() {
        syncOwnState();
        if (StatusClient.CLIENT_CONFIG.showJoinMessage.get() && !stateChanged) {
            showChangeStatusMessage();
        }
    }

    private void showChangeStatusMessage() {
        Minecraft.getInstance().player.sendSystemMessage(ComponentUtils.wrapInSquareBrackets(Component.translatable("message.status.mod_name"))
                .withStyle(ChatFormatting.GREEN)
                .append(" ")
                .append(Component.translatable("message.status.change_status")
                        .withStyle(style -> style
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("message.status.set_status")))
                        ).withStyle(ChatFormatting.WHITE)
                )
        );
    }

    public void syncOwnState() {
        NetManager.sendToServer(new PlayerStatePacket(state));
    }

    @Nullable
    public PlayerState getState(UUID player) {
        if (player.equals(Minecraft.getInstance().player.getGameProfile().getId())) {
            return state;
        }
        return states.get(player);
    }

    private static final ResourceLocation DND = new ResourceLocation(Status.MODID, "textures/icons/dnd.png");
    private static final ResourceLocation OPEN = new ResourceLocation(Status.MODID, "textures/icons/open.png");
    private static final ResourceLocation NO_AVAILABILITY = new ResourceLocation(Status.MODID, "textures/icons/no_availability.png");
    private static final ResourceLocation RECORDING = new ResourceLocation(Status.MODID, "textures/icons/recording.png");
    private static final ResourceLocation STREAMING = new ResourceLocation(Status.MODID, "textures/icons/streaming.png");
    private static final ResourceLocation NEUTRAL = new ResourceLocation(Status.MODID, "textures/icons/neutral.png");

    @Nullable
    public ResourceLocation getIcon(UUID player) {
        PlayerState state = getState(player);
        if (state == null) {
            return null;
        }
        if (state.getState().equals("recording")) {
            return RECORDING;
        } else if (state.getState().equals("streaming")) {
            return STREAMING;
        } else {
            return NEUTRAL;
        }
    }

    @Nullable
    public ResourceLocation getOverlay(UUID player) {
        PlayerState state = getState(player);
        if (state == null) {
            return null;
        }
        if (state.getAvailability().equals(Availability.DO_NOT_DISTURB)) {
            return DND;
        } else if (state.getAvailability().equals(Availability.OPEN)) {
            return OPEN;
        }
        return NO_AVAILABILITY;
    }

    public void clearStates() {
        states.clear();
    }
}

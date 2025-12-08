package de.maxhenkel.status;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.status.config.ServerConfig;
import de.maxhenkel.status.net.PlayerStatePacket;
import de.maxhenkel.status.net.PlayerStatesPacket;
import de.maxhenkel.status.playerstate.PlayerStateManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class Status implements ModInitializer {

    public static final String MODID = "status";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    @Nullable
    public static ServerConfig SERVER_CONFIG;
    public static PlayerStateManager STATE_MANAGER;

    public static final Identifier INIT = Identifier.fromNamespaceAndPath(Status.MODID, "init");
    public static int COMPATIBILITY_VERSION = -1;

    @Override
    public void onInitialize() {
        try {
            COMPATIBILITY_VERSION = readCompatibilityVersion();
            LOGGER.info("Compatibility version {}", COMPATIBILITY_VERSION);
        } catch (Exception e) {
            LOGGER.error("Failed to read compatibility version");
        }

        PayloadTypeRegistry.playC2S().register(PlayerStatePacket.PLAYER_STATE, PlayerStatePacket.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerStatePacket.PLAYER_STATE, PlayerStatePacket.CODEC);
        PayloadTypeRegistry.playS2C().register(PlayerStatesPacket.PLAYER_STATES, PlayerStatesPacket.CODEC);

        STATE_MANAGER = new PlayerStateManager();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            SERVER_CONFIG = ConfigBuilder
                    .builder(ServerConfig::new)
                    .path(server.getServerDirectory().resolve("config").resolve(MODID).resolve("status-server.properties"))
                    .build();
        });

        ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            buffer.writeInt(COMPATIBILITY_VERSION);
            sender.sendPacket(INIT, buffer);
        });
        ServerLoginNetworking.registerGlobalReceiver(INIT, (server, handler, understood, buf, synchronizer, responseSender) -> {
            if (!understood) {
                //Let vanilla clients pass, but not incompatible status clients
                return;
            }

            int clientCompatibilityVersion = buf.readInt();

            if (clientCompatibilityVersion != Status.COMPATIBILITY_VERSION) {
                Status.LOGGER.warn("Client {} has incompatible mod version (server={}, client={})", handler.getUserName(), Status.COMPATIBILITY_VERSION, clientCompatibilityVersion);
                handler.disconnect(Component.translatable("message.status.incompatible_version",
                        Component.literal(getModVersion()).withStyle(ChatFormatting.BOLD),
                        Component.literal(getModName()).withStyle(ChatFormatting.BOLD)));
            }
        });
    }

    public String getModVersion() {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(MODID).orElse(null);
        if (modContainer == null) {
            return "N/A";
        }
        return modContainer.getMetadata().getVersion().getFriendlyString();
    }

    public String getModName() {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(MODID).orElse(null);
        if (modContainer == null) {
            return MODID;
        }
        return modContainer.getMetadata().getName();
    }

    public static int readCompatibilityVersion() {
        ModContainer modContainer = FabricLoader.getInstance().getModContainer(MODID).orElse(null);
        if (modContainer == null) {
            return -1;
        }
        return Integer.parseInt(modContainer.getMetadata().getCustomValue(MODID).getAsObject().get("compatibilityVersion").getAsString());
    }

}

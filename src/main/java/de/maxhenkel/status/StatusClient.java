package de.maxhenkel.status;

import de.maxhenkel.configbuilder.ConfigBuilder;
import de.maxhenkel.status.config.ClientConfig;
import de.maxhenkel.status.gui.StatusScreen;
import de.maxhenkel.status.playerstate.ClientPlayerStateManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
public class StatusClient implements ClientModInitializer {

    public static KeyMapping KEY_STATUS_GUI;

    public static ClientConfig CLIENT_CONFIG;
    public static ClientPlayerStateManager STATE_MANAGER;

    @Override
    public void onInitializeClient() {
        CLIENT_CONFIG = ConfigBuilder.build(Minecraft.getInstance().gameDirectory.toPath().resolve("config").resolve(Status.MODID).resolve("state-client.properties"), ClientConfig::new);

        STATE_MANAGER = new ClientPlayerStateManager();
        ClientLoginNetworking.registerGlobalReceiver(Status.INIT, (client, handler, buf, listenerAdder) -> {
            int serverCompatibilityVersion = buf.readInt();

            if (serverCompatibilityVersion != Status.COMPATIBILITY_VERSION) {
                Status.LOGGER.warn("Incompatible status version (server={}, client={})", serverCompatibilityVersion, Status.COMPATIBILITY_VERSION);
            }

            FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
            buffer.writeInt(Status.COMPATIBILITY_VERSION);
            return CompletableFuture.completedFuture(buffer);
        });

        KEY_STATUS_GUI = KeyBindingHelper.registerKeyBinding(new KeyMapping("key.status_gui", GLFW.GLFW_KEY_U, "key.categories.misc"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (KEY_STATUS_GUI.consumeClick()) {
                client.setScreen(new StatusScreen());
            }
        });
    }
}

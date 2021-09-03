package de.maxhenkel.status.mixin;

import de.maxhenkel.status.events.ClientWorldEvents;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(at = @At("RETURN"), method = "handleLogin")
    private void createPlayer(ClientboundLoginPacket clientboundLoginPacket, CallbackInfo info) {
        ClientWorldEvents.JOIN_SERVER.invoker().run();
    }

}

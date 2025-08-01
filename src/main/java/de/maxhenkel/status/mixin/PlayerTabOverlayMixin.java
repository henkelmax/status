package de.maxhenkel.status.mixin;

import com.mojang.authlib.GameProfile;
import de.maxhenkel.status.StatusClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;isEncrypted()Z"))
    private boolean isEncrypted(Connection connection) {
        return true;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)V"))
    private void onDrawShadow(GuiGraphics instance, Font font, Component formattedCharSequence, int i, int j, int k) {
        instance.pose().pushMatrix();
        instance.pose().translate(9F, 0F);
        instance.drawString(font, formattedCharSequence, i, j, k);
        instance.pose().popMatrix();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I", ordinal = 0))
    private int onNameWidth(Font font, FormattedText formattedText) {
        return font.width(formattedText) + 10;
    }

    @Unique
    private UUID playerUUID;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getProfile()Lcom/mojang/authlib/GameProfile;"))
    private GameProfile getProfile(PlayerInfo playerInfo) {
        playerUUID = playerInfo.getProfile().getId();
        return playerInfo.getProfile();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerFaceRenderer;draw(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIZZI)V"))
    private void onRenderHead(GuiGraphics guiGraphics, ResourceLocation resourceLocation, int x, int y, int size, boolean upsideDown, boolean renderHat, int l) {
        PlayerFaceRenderer.draw(guiGraphics, resourceLocation, x, y, size, upsideDown, renderHat, l);

        ResourceLocation availability = StatusClient.STATE_MANAGER.getAvailabilityIcon(playerUUID);
        if (availability != null) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(x + 9F, y);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, availability, 0, 0, 0, 0, 8, 8, 8, 8);
            guiGraphics.pose().popMatrix();
        }

        ResourceLocation activity = StatusClient.STATE_MANAGER.getActivityIcon(playerUUID);
        if (activity != null) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(x + 9F, y);
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, activity, 0, 0, 0, 0, 8, 8, 8, 8);
            guiGraphics.pose().popMatrix();
        }
    }

}

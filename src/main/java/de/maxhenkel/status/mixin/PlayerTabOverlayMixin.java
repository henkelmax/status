package de.maxhenkel.status.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import de.maxhenkel.status.StatusClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public class PlayerTabOverlayMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;isEncrypted()Z"))
    private boolean isEncrypted(Connection connection) {
        return true;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/network/chat/Component;III)I"))
    private int onDrawShadow(GuiGraphics instance, Font font, Component formattedCharSequence, int i, int j, int k) {
        instance.pose().pushPose();
        instance.pose().translate(9D, 0D, 0D);
        int x = instance.drawString(font, formattedCharSequence, i, j, k);
        instance.pose().popPose();
        return x;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Lnet/minecraft/network/chat/FormattedText;)I", ordinal = 0))
    private int onNameWidth(Font font, FormattedText formattedText) {
        return font.width(formattedText) + 10;
    }

    private UUID playerUUID;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/PlayerInfo;getProfile()Lcom/mojang/authlib/GameProfile;"))
    private GameProfile getProfile(PlayerInfo playerInfo) {
        playerUUID = playerInfo.getProfile().getId();
        return playerInfo.getProfile();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerFaceRenderer;draw(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/resources/ResourceLocation;IIIZZ)V"))
    private void onRenderHead(GuiGraphics guiGraphics, ResourceLocation resourceLocation, int x, int y, int size, boolean upsideDown, boolean renderHat) {
        int shaderTexture = RenderSystem.getShaderTexture(0);

        PlayerFaceRenderer.draw(guiGraphics, resourceLocation, x, y, size, upsideDown, renderHat);

        ResourceLocation icon = StatusClient.STATE_MANAGER.getIcon(playerUUID);
        if (icon != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x + 9D, y, 0D);
            guiGraphics.blit(icon, 0, 0, 0, 0, 8, 8, 8, 8);
            guiGraphics.pose().popPose();
        }

        ResourceLocation overlay = StatusClient.STATE_MANAGER.getOverlay(playerUUID);
        if (overlay != null) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x + 9D, y, 0D);
            guiGraphics.blit(overlay, 0, 0, 0, 0, 8, 8, 8, 8);
            guiGraphics.pose().popPose();
        }

        RenderSystem.setShaderTexture(0, shaderTexture);
    }

}

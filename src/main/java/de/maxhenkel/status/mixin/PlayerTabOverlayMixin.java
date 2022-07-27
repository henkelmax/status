package de.maxhenkel.status.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.status.StatusClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
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

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/network/chat/Component;FFI)I"))
    private int onDrawShadow(Font font, PoseStack poseStack, Component component, float f, float g, int i) {
        poseStack.pushPose();
        poseStack.translate(9D, 0D, 0D);
        int x = font.drawShadow(poseStack, component, f, g, i);
        poseStack.popPose();
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

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/PlayerFaceRenderer;draw(Lcom/mojang/blaze3d/vertex/PoseStack;IIIZZ)V"))
    private void onRenderHead(PoseStack poseStack, int x, int y, int size, boolean upsideDown, boolean renderHat) {
        int shaderTexture = RenderSystem.getShaderTexture(0);

        PlayerFaceRenderer.draw(poseStack, x, y, size, upsideDown, renderHat);

        ResourceLocation icon = StatusClient.STATE_MANAGER.getIcon(playerUUID);
        if (icon != null) {
            RenderSystem.setShaderTexture(0, icon);
            poseStack.pushPose();
            poseStack.translate(x + 9D, y, 0D);
            GuiComponent.blit(poseStack, 0, 0, 0, 0, 8, 8, 8, 8);
            poseStack.popPose();
        }

        ResourceLocation overlay = StatusClient.STATE_MANAGER.getOverlay(playerUUID);
        if (overlay != null) {
            RenderSystem.setShaderTexture(0, overlay);
            poseStack.pushPose();
            poseStack.translate(x + 9D, y, 0D);
            GuiComponent.blit(poseStack, 0, 0, 0, 0, 8, 8, 8, 8);
            poseStack.popPose();
        }

        RenderSystem.setShaderTexture(0, shaderTexture);
    }

}

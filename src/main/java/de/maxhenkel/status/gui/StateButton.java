package de.maxhenkel.status.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.status.StatusClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class StateButton extends AbstractButton {

    protected Minecraft mc;
    protected ResourceLocation texture;
    protected TooltipSupplier tooltipSupplier;
    protected String state;

    public StateButton(int x, int y, ResourceLocation texture, String state, TooltipSupplier tooltipSupplier) {
        super(x, y, 20, 20, TextComponent.EMPTY);
        this.mc = Minecraft.getInstance();
        this.texture = texture;
        this.state = state;
        this.tooltipSupplier = tooltipSupplier;
    }

    public StateButton(ResourceLocation texture, String state, TooltipSupplier tooltipSupplier) {
        this(0, 0, texture, state, tooltipSupplier);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public void onPress() {
        StatusClient.STATE_MANAGER.setState(state);
    }

    public boolean isStateActive() {
        return StatusClient.STATE_MANAGER.getState().equals(state);
    }

    protected void renderImage(PoseStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, texture);
        blit(matrices, x + 6, y + 6, 0, 0, 8, 8, 8, 8);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        active = !isStateActive();

        super.renderButton(matrices, mouseX, mouseY, delta);
        renderImage(matrices, mouseX, mouseY, delta);

        if (isHovered()) {
            renderToolTip(matrices, mouseX, mouseY);
        }
    }

    public void renderToolTip(PoseStack matrices, int mouseX, int mouseY) {
        this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

    public interface TooltipSupplier {
        void onTooltip(StateButton button, PoseStack matrices, int mouseX, int mouseY);
    }

}

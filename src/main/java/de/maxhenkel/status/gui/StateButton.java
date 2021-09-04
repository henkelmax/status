package de.maxhenkel.status.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.status.StatusClient;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class StateButton extends AbstractButton {

    protected String state;

    public StateButton(int x, int y, int width, int height, Component text, String state) {
        super(x, y, width, height, text);
        this.state = state;
    }

    @Override
    public void onPress() {
        StatusClient.STATE_MANAGER.setState(state);
    }

    public boolean isStateActive() {
        return StatusClient.STATE_MANAGER.getState().equals(state);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        active = !isStateActive();
        super.renderButton(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

}

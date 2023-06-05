package de.maxhenkel.status.gui;

import de.maxhenkel.status.StatusClient;
import net.minecraft.client.gui.GuiGraphics;
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
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        active = !isStateActive();
        super.renderWidget(guiGraphics, mouseX, mouseY, delta);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }

}

package de.maxhenkel.status.gui;

import de.maxhenkel.status.StatusClient;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

public class StateButton extends AbstractButton {

    protected String state;

    public StateButton(int x, int y, int width, int height, Component text, String state) {
        super(x, y, width, height, text);
        this.state = state;
    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        StatusClient.STATE_MANAGER.setState(state);
    }

    public boolean isStateActive() {
        return StatusClient.STATE_MANAGER.getState().equals(state);
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        active = !isStateActive();
        renderDefaultSprite(guiGraphics);
        renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }

}

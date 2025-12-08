package de.maxhenkel.status.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;

import java.util.function.Supplier;

public class BooleanButton extends AbstractButton {

    protected Supplier<Boolean> value;
    protected Runnable onChange;

    public BooleanButton(int x, int y, int width, int height, Component text, Supplier<Boolean> value, Runnable onChange) {
        super(x, y, width, height, text);
        this.value = value;
        this.onChange = onChange;
    }

    @Override
    public void onPress(InputWithModifiers inputWithModifiers) {
        onChange.run();
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        active = !value.get();
        renderDefaultSprite(guiGraphics);
        renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }

}

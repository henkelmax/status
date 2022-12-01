package de.maxhenkel.status.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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
    public void onPress() {
        onChange.run();
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        active = !value.get();
        super.renderButton(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }

}

package de.maxhenkel.status.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.status.StatusClient;
import de.maxhenkel.status.playerstate.Availability;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class AvailabilityButton extends AbstractButton {

    protected Availability availability;

    public AvailabilityButton(int x, int y, int width, int height, Component text, Availability availability) {
        super(x, y, width, height, text);
        this.availability = availability;
    }

    @Override
    public void onPress() {
        StatusClient.STATE_MANAGER.setAvailability(availability);
    }

    public boolean isAvailabilityActive() {
        return StatusClient.STATE_MANAGER.getAvailability().equals(availability);
    }

    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        active = !isAvailabilityActive();
        super.renderButton(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }

}

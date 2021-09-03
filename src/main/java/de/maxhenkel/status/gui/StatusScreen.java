package de.maxhenkel.status.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.status.Status;
import de.maxhenkel.status.playerstate.Availability;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatusScreen extends StatusScreenBase {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Status.MODID, "textures/gui/gui_status.png");
    private static final ResourceLocation DND = new ResourceLocation(Status.MODID, "textures/icons/dnd.png");
    private static final ResourceLocation OPEN = new ResourceLocation(Status.MODID, "textures/icons/open.png");
    private static final ResourceLocation NO_AVAILABILITY = new ResourceLocation(Status.MODID, "textures/icons/no_availability.png");
    private static final ResourceLocation NEUTRAL = new ResourceLocation(Status.MODID, "textures/icons/neutral.png");
    private static final ResourceLocation RECORDING = new ResourceLocation(Status.MODID, "textures/icons/recording.png");
    private static final ResourceLocation STREAMING = new ResourceLocation(Status.MODID, "textures/icons/streaming.png");
    private static final ResourceLocation NO_SLEEP = new ResourceLocation(Status.MODID, "textures/icons/no_sleep.png");

    private AvailabilityButton none;
    private AvailabilityButton dnd;
    private AvailabilityButton open;
    private List<StateButton> stateButtons;

    public StatusScreen() {
        super(new TranslatableComponent("gui.status.title"), 195, 76);
    }

    @Override
    protected void init() {
        super.init();
        stateButtons = new ArrayList<>();

        none = new AvailabilityButton(guiLeft + xSize / 2 - 20 - 11, guiTop + 7 + font.lineHeight + 7, NO_AVAILABILITY, Availability.NONE, (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Collections.singletonList(new TranslatableComponent("message.status.no_availability").getVisualOrderText()), mouseX, mouseY);
        });
        addRenderableWidget(none);

        dnd = new AvailabilityButton(guiLeft + xSize / 2 - 10, guiTop + 7 + font.lineHeight + 7, DND, Availability.DO_NOT_DISTURB, (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Collections.singletonList(new TranslatableComponent("message.status.do_not_disturb").getVisualOrderText()), mouseX, mouseY);
        });
        addRenderableWidget(dnd);

        open = new AvailabilityButton(guiLeft + xSize / 2 + 11, guiTop + 7 + font.lineHeight + 7, OPEN, Availability.OPEN, (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Collections.singletonList(new TranslatableComponent("message.status.open").getVisualOrderText()), mouseX, mouseY);
        });
        addRenderableWidget(open);

        StateButton neutral = new StateButton(NEUTRAL, "", (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Collections.singletonList(new TranslatableComponent("message.status.neutral").getVisualOrderText()), mouseX, mouseY);
        });
        addRenderableWidget(neutral);
        stateButtons.add(neutral);

        StateButton recording = new StateButton(RECORDING, "recording", (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Collections.singletonList(new TranslatableComponent("message.status.recording").getVisualOrderText()), mouseX, mouseY);
        });
        addRenderableWidget(recording);
        stateButtons.add(recording);

        StateButton streaming = new StateButton(STREAMING, "streaming", (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Collections.singletonList(new TranslatableComponent("message.status.streaming").getVisualOrderText()), mouseX, mouseY);
        });
        addRenderableWidget(streaming);
        stateButtons.add(streaming);

        StateButton noSleep = new StateButton(NO_SLEEP, "no_sleep", (button, matrices, mouseX, mouseY) -> {
            renderTooltip(matrices, Collections.singletonList(new TranslatableComponent("message.status.no_sleep").getVisualOrderText()), mouseX, mouseY);
        });
        addRenderableWidget(noSleep);
        stateButtons.add(noSleep);

        int buttonY = guiTop + 7 + font.lineHeight + 7 + 2 + 20;
        int buttonSectionWidth = 20 + 1;
        int width = stateButtons.size() * buttonSectionWidth - 1;
        int posLeft = guiLeft + xSize / 2 - width / 2;
        for (int i = 0; i < stateButtons.size(); i++) {
            StateButton stateButton = stateButtons.get(i);
            stateButton.setX(posLeft + buttonSectionWidth * i);
            stateButton.setY(buttonY);
        }
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int titleWidth = font.width(getTitle());
        font.draw(matrixStack, getTitle().getVisualOrderText(), (float) (guiLeft + (xSize - titleWidth) / 2), guiTop + 7, FONT_COLOR);
    }

}

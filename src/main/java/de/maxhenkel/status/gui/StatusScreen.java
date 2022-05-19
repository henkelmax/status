package de.maxhenkel.status.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.maxhenkel.status.Status;
import de.maxhenkel.status.StatusClient;
import de.maxhenkel.status.playerstate.Availability;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StatusScreen extends StatusScreenBase {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Status.MODID, "textures/gui/gui_status.png");
    private static final ResourceLocation OUTLINE = new ResourceLocation(Status.MODID, "textures/icons/outline.png");
    private static final ResourceLocation NO_AVAILABILITY = new ResourceLocation(Status.MODID, "textures/icons/no_availability.png");
    private static final ResourceLocation DND = new ResourceLocation(Status.MODID, "textures/icons/dnd.png");
    private static final ResourceLocation OPEN = new ResourceLocation(Status.MODID, "textures/icons/open.png");
    private static final ResourceLocation NEUTRAL = new ResourceLocation(Status.MODID, "textures/icons/neutral.png");
    private static final ResourceLocation RECORDING = new ResourceLocation(Status.MODID, "textures/icons/recording.png");
    private static final ResourceLocation STREAMING = new ResourceLocation(Status.MODID, "textures/icons/streaming.png");

    public StatusScreen() {
        super(Component.translatable("gui.status.title"), 145, 184);
    }

    @Override
    protected void init() {
        super.init();

        int x = guiLeft + 10 + 20 + 5;
        int y = guiTop + 7 + font.lineHeight + 7;
        int width = 100;
        int height = 20;

        AvailabilityButton none = new AvailabilityButton(x, y, width, height, Component.translatable("message.status.no_availability"), Availability.NONE);
        addRenderableWidget(none);
        y += height + 1;

        AvailabilityButton dnd = new AvailabilityButton(x, y, width, height, Component.translatable("message.status.do_not_disturb"), Availability.DO_NOT_DISTURB);
        addRenderableWidget(dnd);
        y += height + 1;

        AvailabilityButton open = new AvailabilityButton(x, y, width, height, Component.translatable("message.status.open"), Availability.OPEN);
        addRenderableWidget(open);
        y += height + 5;

        StateButton neutral = new StateButton(x, y, width, height, Component.translatable("message.status.neutral"), "");
        addRenderableWidget(neutral);
        y += height + 1;

        StateButton recording = new StateButton(x, y, width, height, Component.translatable("message.status.recording"), "recording");
        addRenderableWidget(recording);
        y += height + 1;

        StateButton streaming = new StateButton(x, y, width, height, Component.translatable("message.status.streaming"), "streaming");
        addRenderableWidget(streaming);
        y += height + 5;

        BooleanButton noSleep = new BooleanButton(x, y, width, height, Component.translatable("message.status.no_sleep"), () -> StatusClient.STATE_MANAGER.getNoSleep(), () -> {
            StatusClient.STATE_MANAGER.setNoSleep(true);
        });
        addRenderableWidget(noSleep);

        BooleanButton disableNoSleep = new BooleanButton(guiLeft + 8, y, 20, 20, Component.literal("X"), () -> !StatusClient.STATE_MANAGER.getNoSleep(), () -> {
            StatusClient.STATE_MANAGER.setNoSleep(false);
        });
        addRenderableWidget(disableNoSleep);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);

        super.render(matrixStack, mouseX, mouseY, partialTicks);

        int x = guiLeft + 10;
        int y = guiTop + 7 + font.lineHeight + 7;
        int height = 20;

        renderIcon(matrixStack, NO_AVAILABILITY, x, y + 2);
        y += height + 1;

        renderIcon(matrixStack, DND, x, y + 2);
        y += height + 1;

        renderIcon(matrixStack, OPEN, x, y + 2);
        y += height + 5;

        renderIcon(matrixStack, NEUTRAL, x, y + 2);
        y += height + 1;

        renderIcon(matrixStack, RECORDING, x, y + 2);
        y += height + 1;

        renderIcon(matrixStack, STREAMING, x, y + 2);

        int titleWidth = font.width(getTitle());
        font.draw(matrixStack, getTitle().getVisualOrderText(), (float) (guiLeft + (xSize - titleWidth) / 2), guiTop + 7, FONT_COLOR);
    }

    private void renderIcon(PoseStack poseStack, ResourceLocation texture, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);

        RenderSystem.setShaderTexture(0, OUTLINE);
        blit(poseStack, x - 1, y - 1, 0, 0, 18, 18, 32, 32);

        RenderSystem.setShaderTexture(0, texture);
        blit(poseStack, x, y, 0, 0, 16, 16, 16, 16);
    }

}

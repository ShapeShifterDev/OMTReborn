package omtreborn.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ExpanderInvScreen extends AbstractContainerScreen<ExpanderInvMenu> {

    @SuppressWarnings("deprecation")
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("omtreborn", "textures/gui/expander_inv_gui.png");

    public ExpanderInvScreen(ExpanderInvMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // Placeholder background until Phase 11 textures are added
        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF808080);

        // Ammo slot outlines (3×3)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int sx = leftPos + 62 + col * 18;
                int sy = topPos + 17 + row * 18;
                guiGraphics.fill(sx - 1, sy - 1, sx + 17, sy + 17, 0xFF404040);
                guiGraphics.fill(sx, sy, sx + 16, sy + 16, 0xFF555555);
            }
        }

        // Player inventory area backgrounds
        guiGraphics.fill(leftPos + 7, topPos + 83, leftPos + 169, topPos + 131, 0xFF606060);
        guiGraphics.fill(leftPos + 7, topPos + 141, leftPos + 169, topPos + 159, 0xFF606060);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

package omtreborn.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.lib.permission.EnumAccessLevel;
import omtreborn.network.OMTNetwork;
import omtreborn.network.messages.*;
import omtreborn.tileentity.TurretBaseBlockEntity;

@OnlyIn(Dist.CLIENT)
public class TurretBaseScreen extends AbstractContainerScreen<TurretBaseMenu> {

    @SuppressWarnings("deprecation")
    private static final ResourceLocation TEXTURE =
            new ResourceLocation("omtreborn", "textures/gui/turret_base_gui.png");

    @SuppressWarnings("deprecation")
    private static final ResourceLocation FURNACE_TEXTURE =
            new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    private boolean localMultiTargeting;

    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInv, Component title) {
        super(menu, playerInv, title);
        this.imageWidth = 176;
        this.imageHeight = 222;
        this.inventoryLabelY = 104;
    }

    @Override
    protected void init() {
        super.init();

        TurretBaseBlockEntity base = menu.getBase();
        Player player = Minecraft.getInstance().player;
        if (base == null || player == null) return;

        localMultiTargeting = base.isMultiTargeting();

        BlockPos pos = base.getBlockPos();
        boolean canChange = base.getTrustManager().isTrusted(player, EnumAccessLevel.ADMIN)
                || base.getTrustManager().isTrusted(player, EnumAccessLevel.CHANGE_SETTINGS);

        int bx = leftPos + 98;
        int bw = 72;
        int bh = 16;
        int by = topPos + 6;

        if (canChange) {
            addRenderableWidget(Button.builder(
                    Component.translatable("gui.omtreborn.configure"),
                    btn -> Minecraft.getInstance().setScreen(new ConfigureScreen(pos))
            ).bounds(bx, by, bw, bh).build());
            by += bh + 2;

            addRenderableWidget(Button.builder(
                    localMultiTargeting
                            ? Component.translatable("gui.omtreborn.multi_target")
                            : Component.translatable("gui.omtreborn.single_target"),
                    btn -> {
                        localMultiTargeting = !localMultiTargeting;
                        OMTNetwork.sendToServer(new MessageSetBaseTargetingType(pos));
                        rebuildWidgets();
                    }
            ).bounds(bx, by, bw, bh).build());
            by += bh + 2;

            addRenderableWidget(Button.builder(
                    Component.literal("-"),
                    btn -> OMTNetwork.sendToServer(new MessageAdjustRange(pos, base.getRange() - 1))
            ).bounds(bx, by, 34, bh).build());

            addRenderableWidget(Button.builder(
                    Component.literal("+"),
                    btn -> OMTNetwork.sendToServer(new MessageAdjustRange(pos, base.getRange() + 1))
            ).bounds(bx + 36, by, 34, bh).build());
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF808080);

        // Ammo slot outlines (3×3)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int sx = leftPos + 8 + col * 18;
                int sy = topPos + 17 + row * 18;
                guiGraphics.fill(sx - 1, sy - 1, sx + 17, sy + 17, 0xFF404040);
                guiGraphics.fill(sx, sy, sx + 16, sy + 16, 0xFF555555);
            }
        }

        int tier = menu.getBase() != null ? menu.getBase().getTier() : 0;

        if (tier == 1) {
            // Fuel slot outline — orange tint
            int fsx = leftPos + 80;
            int fsy = topPos + 17;
            guiGraphics.fill(fsx - 1, fsy - 1, fsx + 17, fsy + 17, 0xFF663300);
            guiGraphics.fill(fsx, fsy, fsx + 16, fsy + 16, 0xFF884400);

            // Flame indicator below fuel slot (uses vanilla furnace.png flame sprite)
            int i = menu.getLitProgress(); // 0-13
            if (i > 0) {
                guiGraphics.blit(FURNACE_TEXTURE, leftPos + 81, topPos + 37 + 12 - i, 176, 12 - i, 14, i + 1);
            }
        }

        // Addon and upgrade slot outlines (tier >= 2 only)
        if (tier >= 2) {
            // Addon slots — blue tint (x=62,80; y=18)
            for (int col = 0; col < 2; col++) {
                int sx = leftPos + 62 + col * 18;
                int sy = topPos + 18;
                guiGraphics.fill(sx - 1, sy - 1, sx + 17, sy + 17, 0xFF223366);
                guiGraphics.fill(sx, sy, sx + 16, sy + 16, 0xFF334488);
            }
            guiGraphics.drawString(font, "Add.", leftPos + 63, topPos + 22, 0xAABBFF, false);

            // Upgrade slots — gold tint (x=62 always; x=80 if tier >= 5)
            int upgradeCount = (tier >= 5) ? 2 : 1;
            for (int col = 0; col < upgradeCount; col++) {
                int sx = leftPos + 62 + col * 18;
                int sy = topPos + 52;
                guiGraphics.fill(sx - 1, sy - 1, sx + 17, sy + 17, 0xFF664400);
                guiGraphics.fill(sx, sy, sx + 16, sy + 16, 0xFF886622);
            }
            guiGraphics.drawString(font, "Upg.", leftPos + 63, topPos + 56, 0xFFCC66, false);
        }

        // Player inventory backgrounds
        guiGraphics.fill(leftPos + 7, topPos + 113, leftPos + 169, topPos + 167, 0xFF606060);
        guiGraphics.fill(leftPos + 7, topPos + 171, leftPos + 169, topPos + 189, 0xFF606060);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        TurretBaseBlockEntity base = menu.getBase();
        if (base == null) return;

        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);

        String ownerName = base.getTrustManager().getOwner() != null
                ? base.getTrustManager().getOwner().getName() : "?";

        int stored = base.getEnergyStorage().getEnergyStored();
        int cap = base.getEnergyStorage().getMaxEnergyStored();

        // Left column: ownership / combat stats
        guiGraphics.drawString(font, "Owner: " + ownerName, 8, 75, 0xFFFFFF, false);
        guiGraphics.drawString(font, "Kills: " + base.getKills() + "  PK: " + base.getPlayerKills(), 8, 87, 0xFFFFFF, false);

        // Right column: operational stats
        guiGraphics.drawString(font, "Range: " + base.getRange() + "/" + base.getMaxRange(), 92, 75, 0xFFFFFF, false);
        guiGraphics.drawString(font, stored + "/" + cap + " RF", 92, 87, 0x00AAFF, false);

        // Fuel label for tier 1
        if (base.getTier() == 1) {
            guiGraphics.drawString(font, "Fuel", 81, 8, 0xFF8833, false);
        }

        guiGraphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    public void onServerSync() {
        rebuildWidgets();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}

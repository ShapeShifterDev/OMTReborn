package omtreborn.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import omtreborn.lib.permission.EnumAccessLevel;
import omtreborn.network.OMTNetwork;
import omtreborn.network.messages.*;
import omtreborn.tileentity.TurretBaseBlockEntity;

@OnlyIn(Dist.CLIENT)
public class ConfigureScreen extends Screen {

    private final BlockPos basePos;

    // Optimistic local state — updated on click without waiting for server round-trip
    private boolean localActive;
    private boolean mobs;
    private boolean neutrals;
    private boolean players;
    private boolean stateInitialized = false;

    public ConfigureScreen(BlockPos basePos) {
        super(Component.translatable("gui.omtreborn.configure"));
        this.basePos = basePos;
    }

    @Override
    protected void init() {
        TurretBaseBlockEntity base = getBase();
        Player player = Minecraft.getInstance().player;

        // Read state from BE only on first open; preserve optimistic state on rebuild
        if (!stateInitialized) {
            if (base != null) {
                localActive = base.isActive();
                mobs = base.isAttacksMobs();
                neutrals = base.isAttacksNeutrals();
                players = base.isAttacksPlayers();
            }
            stateInitialized = true;
        }

        boolean isAdmin = player != null && base != null
                && base.getTrustManager().isTrusted(player, EnumAccessLevel.ADMIN);

        int panelW = 200;
        int panelH = isAdmin ? 158 : 130;
        int left = (width - panelW) / 2;
        int top = (height - panelH) / 2;
        int bw = 180;
        int bh = 20;
        int bx = left + (panelW - bw) / 2;
        int by = top + 20;

        if (isAdmin) {
            addRenderableWidget(Button.builder(
                    Component.translatable(localActive ? "gui.omtreborn.active" : "gui.omtreborn.inactive"),
                    btn -> {
                        localActive = !localActive;
                        OMTNetwork.sendToServer(new MessageToggleMode(basePos));
                        rebuildWidgets();
                    }
            ).bounds(bx, by, bw, bh).build());
            by += bh + 8;

            // Hidden: bases and turrets are now directly mineable
            Button dropTurretsBtn = Button.builder(
                    Component.translatable("gui.omtreborn.drop_turrets"),
                    btn -> OMTNetwork.sendToServer(new MessageDropTurrets(basePos))
            ).bounds(bx, by, bw, bh).build();

            Button dropBaseBtn = Button.builder(
                    Component.translatable("gui.omtreborn.drop_base"),
                    btn -> { OMTNetwork.sendToServer(new MessageDropBase(basePos)); onClose(); }
            ).bounds(bx, by, bw, bh).build();
        }

        addRenderableWidget(Button.builder(
                Component.translatable(mobs ? "gui.omtreborn.attack_mobs.on" : "gui.omtreborn.attack_mobs.off"),
                btn -> {
                    mobs = !mobs;
                    OMTNetwork.sendToServer(new MessageToggleAttackMobs(basePos, mobs));
                    rebuildWidgets();
                }
        ).bounds(bx, by, bw, bh).build());
        by += bh + 4;

        addRenderableWidget(Button.builder(
                Component.translatable(neutrals ? "gui.omtreborn.attack_neutrals.on" : "gui.omtreborn.attack_neutrals.off"),
                btn -> {
                    neutrals = !neutrals;
                    OMTNetwork.sendToServer(new MessageToggleAttackNeutralMobs(basePos, neutrals));
                    rebuildWidgets();
                }
        ).bounds(bx, by, bw, bh).build());
        by += bh + 4;

        addRenderableWidget(Button.builder(
                Component.translatable(players ? "gui.omtreborn.attack_players.on" : "gui.omtreborn.attack_players.off"),
                btn -> {
                    players = !players;
                    OMTNetwork.sendToServer(new MessageToggleAttackPlayers(basePos, this.players));
                    rebuildWidgets();
                }
        ).bounds(bx, by, bw, bh).build());
        by += bh + 8;

        addRenderableWidget(Button.builder(
                Component.translatable("gui.back"),
                btn -> OMTNetwork.sendToServer(new MessageOpenTurretGui(basePos))
        ).bounds(bx, by, bw, bh).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);

        TurretBaseBlockEntity base = getBase();
        Player player = Minecraft.getInstance().player;
        boolean isAdmin = player != null && base != null
                && base.getTrustManager().isTrusted(player, EnumAccessLevel.ADMIN);

        int panelW = 200;
        int panelH = isAdmin ? 158 : 130;
        int left = (width - panelW) / 2;
        int top = (height - panelH) / 2;

        guiGraphics.fill(left, top, left + panelW, top + panelH, 0xCC606060);
        guiGraphics.drawCenteredString(font, title, width / 2, top + 6, 0xFFFFFF);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    public void onServerSync() {
        stateInitialized = false;
        rebuildWidgets();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private TurretBaseBlockEntity getBase() {
        Level level = Minecraft.getInstance().level;
        if (level == null) return null;
        if (level.getBlockEntity(basePos) instanceof TurretBaseBlockEntity base) return base;
        return null;
    }
}

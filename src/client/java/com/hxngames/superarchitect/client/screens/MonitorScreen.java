package com.hxngames.superarchitect.client.screens;

import com.hxngames.superarchitect.menus.MonitorMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class MonitorScreen extends AbstractContainerScreen<MonitorMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(com.hxngames.superarchitect.SuperArchitect.MOD_ID, "textures/gui/container/monitor.png");

    private int scrollOffset = 0;
    private EditBox searchBox;
    private Button sortButton;

    public MonitorScreen(MonitorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageHeight = 224;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        this.searchBox = new EditBox(this.font, i + 82, j - 14, 80, 10, Component.literal("Search"));
        this.searchBox.setResponder(query -> {
            if (this.minecraft != null && this.minecraft.getConnection() != null) {
                net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(new com.hxngames.superarchitect.network.MonitorSearchPacket(query));
            }
        });
        this.addRenderableWidget(this.searchBox);

        this.sortButton = net.minecraft.client.gui.components.Button.builder(
            Component.literal("Sort"),
            button -> {
                if (this.minecraft != null && this.minecraft.gameMode != null) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, -1);
                }
            }
        ).bounds(i + 8, j - 14, 40, 12).build();
        this.addRenderableWidget(this.sortButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (scrollY != 0) {
            int newOffset = scrollOffset - (int) Math.signum(scrollY);
            newOffset = Math.max(0, newOffset);
            if (newOffset != scrollOffset) {
                scrollOffset = newOffset;
                // Send button click packet to server to update scroll offset
                if (this.minecraft != null && this.minecraft.gameMode != null) {
                    this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, scrollOffset);
                }
            }
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        
        // Draw Network Stats
        Slot statsSlot = this.menu.slots.get(54);
        if (statsSlot.hasItem()) {
            net.minecraft.world.item.ItemStack statsStack = statsSlot.getItem();
            net.minecraft.world.item.component.ItemLore lore = statsStack.get(net.minecraft.core.component.DataComponents.LORE);
            if (lore != null && lore.lines().size() >= 3) {
                long used = Long.parseLong(lore.lines().get(0).getString());
                long max = Long.parseLong(lore.lines().get(1).getString());
                String sortTypeStr = lore.lines().get(2).getString();
                String freeStr = "Used: " + formatNumber(used) + " / " + formatNumber(max);
                
                int i = (this.width - this.imageWidth) / 2;
                int j = (this.height - this.imageHeight) / 2;
                
                // Update sort button text
                if (this.sortButton != null) {
                    String btnText = switch (sortTypeStr) {
                        case "QUANTITY_DESC" -> "Qty ▼";
                        case "QUANTITY_ASC" -> "Qty ▲";
                        case "NAME_AZ" -> "A-Z";
                        default -> "Sort";
                    };
                    this.sortButton.setMessage(Component.literal(btnText));
                }

                // Push free space text slightly to the right to fit the sort button
                guiGraphics.drawString(this.font, freeStr, i + 55, j + 5, 0x404040, false);
            }
        }
    }

    private static String formatNumber(long count) {
        if (count < 1000) return String.valueOf(count);
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format(java.util.Locale.US, "%.1f%c", count / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1)).replace(".0", "");
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        // The texture is expected to be a full 256x256 custom UI map (or at least 176x222).
        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);
        
        // Simple visual scrollbar placeholder
        int scrollBarY = j + 18 + (int)((scrollOffset / 10.0f) * (6 * 18 - 15));
        scrollBarY = Math.min(j + 18 + 6 * 18 - 15, scrollBarY);
        guiGraphics.fill(i + 169, scrollBarY, i + 173, scrollBarY + 15, 0xFFAAAAAA);
    }
}

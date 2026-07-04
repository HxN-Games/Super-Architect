package com.hxngames.superarchitect.menus;

import com.hxngames.superarchitect.blockentities.MonitorBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class MonitorMenu extends AbstractContainerMenu {
    private final Container displayContainer;
    private final MonitorBlockEntity blockEntity;
    private final Player player;

    // Client-side constructor
    public MonitorMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, null);
    }

    // Server-side constructor
    public MonitorMenu(int syncId, Inventory playerInventory, MonitorBlockEntity blockEntity) {
        super(SuperArchitectMenus.MONITOR_MENU, syncId);
        this.displayContainer = new SimpleContainer(55);
        this.blockEntity = blockEntity;
        this.player = playerInventory.player;

        // Terminal Grid - 6 rows of 9 slots
        for (int row = 0; row < 6; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(displayContainer, col + row * 9, 8 + col * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return false; // Display only
                    }
                });
            }
        }

        // 55th slot (index 54) for invisible data transfer (stats)
        this.addSlot(new Slot(displayContainer, 54, -1000, -1000) {
            @Override
            public boolean mayPlace(ItemStack stack) { return false; }
            @Override
            public boolean mayPickup(Player player) { return false; }
        });

        // Player inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }

        // Player hotbar
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 198));
        }

        if (this.player != null && !this.player.level().isClientSide) {
            this.sortType = PLAYER_SORT_PREFS.getOrDefault(this.player.getUUID(), SortType.QUANTITY_DESC);
        }

        refreshDisplay();
    }

    public enum SortType {
        QUANTITY_DESC,
        QUANTITY_ASC,
        NAME_AZ
    }
    
    private static final Map<java.util.UUID, SortType> PLAYER_SORT_PREFS = new java.util.HashMap<>();
    
    private int scrollOffset = 0;
    private String searchQuery = "";
    private SortType sortType = SortType.QUANTITY_DESC;

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (id == -1) {
            // Cycle sort type
            int nextOrdinal = (this.sortType.ordinal() + 1) % SortType.values().length;
            this.sortType = SortType.values()[nextOrdinal];
            this.scrollOffset = 0;
            if (!player.level().isClientSide) {
                PLAYER_SORT_PREFS.put(player.getUUID(), this.sortType);
            }
        } else {
            this.scrollOffset = id;
        }
        refreshDisplay();
        return true;
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query == null ? "" : query.toLowerCase(java.util.Locale.ROOT);
        this.scrollOffset = 0;
        refreshDisplay();
    }

    public void refreshDisplay() {
        if (blockEntity == null || player.level().isClientSide) return;

        Map<Item, Integer> networkItemsMap = blockEntity.getNetworkItems();
        int[] stats = blockEntity.getNetworkStats();
        
        this.displayContainer.clearContent();
        
        // Pass stats and sort type to the client via 55th slot
        ItemStack statsStack = new ItemStack(net.minecraft.world.item.Items.STONE, 1);
        statsStack.set(net.minecraft.core.component.DataComponents.LORE, 
            new net.minecraft.world.item.component.ItemLore(
                java.util.List.of(
                    net.minecraft.network.chat.Component.literal(String.valueOf(stats[0])),
                    net.minecraft.network.chat.Component.literal(String.valueOf(stats[1])),
                    net.minecraft.network.chat.Component.literal(this.sortType.name())
                )
            )
        );
        this.displayContainer.setItem(54, statsStack);
        
        // Convert to list for sorting
        java.util.List<Map.Entry<Item, Integer>> sortedItems = new java.util.ArrayList<>(networkItemsMap.entrySet());
        
        // Sort items
        sortedItems.sort((a, b) -> {
            switch (this.sortType) {
                case QUANTITY_ASC:
                    return Integer.compare(a.getValue(), b.getValue());
                case NAME_AZ:
                    return a.getKey().getDescription().getString().compareToIgnoreCase(b.getKey().getDescription().getString());
                case QUANTITY_DESC:
                default:
                    return Integer.compare(b.getValue(), a.getValue());
            }
        });
        
        int index = 0;
        int skip = this.scrollOffset * 9;
        
        for (Map.Entry<Item, Integer> entry : sortedItems) {
            Item item = entry.getKey();
            if (!this.searchQuery.isEmpty()) {
                String itemName = item.getDescription().getString().toLowerCase(java.util.Locale.ROOT);
                if (!itemName.contains(this.searchQuery)) {
                    continue;
                }
            }

            if (skip > 0) {
                skip--;
                continue;
            }
            if (index >= 54) break; 

            ItemStack displayStack = new ItemStack(item, 1);
            String formattedStored = formatNumber(entry.getValue());
            displayStack.set(net.minecraft.core.component.DataComponents.LORE, 
                new net.minecraft.world.item.component.ItemLore(
                    java.util.List.of(net.minecraft.network.chat.Component.literal("Stored: " + formattedStored).withStyle(net.minecraft.ChatFormatting.YELLOW))
                )
            );
            
            this.displayContainer.setItem(index++, displayStack);
        }
    }

    private static String formatNumber(long count) {
        if (count < 1000) return String.valueOf(count);
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format(java.util.Locale.US, "%.1f%c", count / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1)).replace(".0", "");
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickType, Player player) {
        if (blockEntity != null && !player.level().isClientSide) {
            // Check if clicking in the terminal grid
            if (slotId >= 0 && slotId < 54) {
                Slot slot = this.slots.get(slotId);
                ItemStack cursorStack = this.getCarried();

                if (!cursorStack.isEmpty()) {
                    // Try to insert cursor stack into network
                    ItemStack remainder = blockEntity.insertIntoNetwork(cursorStack);
                    this.setCarried(remainder);
                    refreshDisplay();
                } else if (slot.hasItem()) {
                    // Try to extract item from network
                    Item targetItem = slot.getItem().getItem();
                    int amount = button == 1 ? targetItem.getDefaultMaxStackSize() / 2 : targetItem.getDefaultMaxStackSize();
                    if (clickType == ClickType.QUICK_MOVE) amount = targetItem.getDefaultMaxStackSize();
                    
                    ItemStack extracted = blockEntity.extractFromNetwork(targetItem, amount);
                    if (!extracted.isEmpty()) {
                        if (clickType == ClickType.QUICK_MOVE) {
                            if (!this.moveItemStackTo(extracted, 54, this.slots.size(), true)) {
                                // If inventory is full, try pushing it back
                                blockEntity.insertIntoNetwork(extracted);
                            }
                        } else {
                            this.setCarried(extracted);
                        }
                    }
                    refreshDisplay();
                }
                return; // Cancel default interaction
            } else if (slotId >= 54 && clickType == ClickType.QUICK_MOVE) {
                // Shift-click from inventory to insert into network
                Slot slot = this.slots.get(slotId);
                if (slot != null && slot.hasItem()) {
                    ItemStack remainder = blockEntity.insertIntoNetwork(slot.getItem());
                    slot.set(remainder);
                    refreshDisplay();
                    return; // Cancel default
                }
            }
        }
        super.clicked(slotId, button, clickType, player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        // Shift-clicking logic handled dynamically in clicked()
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}

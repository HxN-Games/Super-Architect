package com.hxngames.superarchitect.items;

import com.hxngames.superarchitect.items.components.DiskComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DiskItem extends Item {
    private final int maxSize;

    public DiskItem(int size) {
        super(
                new Properties()
                        .component(SuperArchitectItems.DISK_COMPONENT, new DiskComponent(java.util.Collections.emptyList()))
        );
        this.maxSize = size;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag);

        int count = getUsed(itemStack);
        int size = maxSize;

        if (count > size) {
            list.add(Component.translatable("itemTooltip.super_architect.disk_full", count, size).withStyle(ChatFormatting.RED));
        } else {
            list.add(Component.translatable("itemTooltip.super_architect.disk", count, size).withStyle(ChatFormatting.GRAY));
        }
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getUsed(ItemStack stack) {
        DiskComponent component = stack.get(SuperArchitectItems.DISK_COMPONENT);
        if (component == null || component.items() == null) {
            return 0;
        }
        
        int total = 0;
        for (DiskComponent.ItemEntry entry : component.items()) {
            total += entry.count();
        }
        return total;
    }

    /**
     * Inserts an item stack into the disk.
     *
     * @param diskStack     The disk item stack.
     * @param stackToInsert The item stack to insert.
     * @return The remaining item stack that could not fit (or the whole stack if disk is full).
     */
    public ItemStack insert(ItemStack diskStack, ItemStack stackToInsert) {
        if (stackToInsert.isEmpty()) {
            return stackToInsert;
        }

        DiskComponent component = diskStack.getOrDefault(SuperArchitectItems.DISK_COMPONENT, new DiskComponent(java.util.Collections.emptyList()));
        List<DiskComponent.ItemEntry> currentItems = new java.util.ArrayList<>(component.items());
        
        int currentUsed = getUsed(diskStack);
        int availableSpace = maxSize - currentUsed;

        if (availableSpace <= 0) {
            return stackToInsert; // Disk is full
        }

        int amountToInsert = Math.min(availableSpace, stackToInsert.getCount());
        
        boolean found = false;
        for (int i = 0; i < currentItems.size(); i++) {
            DiskComponent.ItemEntry existing = currentItems.get(i);
            if (ItemStack.isSameItemSameComponents(existing.stack(), stackToInsert)) {
                // Merge counts
                currentItems.set(i, new DiskComponent.ItemEntry(existing.stack(), existing.count() + amountToInsert));
                found = true;
                break;
            }
        }

        if (!found) {
            ItemStack prototype = stackToInsert.copyWithCount(1);
            currentItems.add(new DiskComponent.ItemEntry(prototype, amountToInsert));
        }

        // Apply updated component back to the disk
        diskStack.set(SuperArchitectItems.DISK_COMPONENT, new DiskComponent(currentItems));

        // Return the remainder
        ItemStack remainder = stackToInsert.copy();
        remainder.shrink(amountToInsert);
        return remainder;
    }

    /**
     * Extracts a specific item type from the disk.
     *
     * @param diskStack     The disk item stack.
     * @param itemToExtract The type of item to extract.
     * @param maxAmount     The maximum amount to extract.
     * @return The extracted item stack.
     */
    public ItemStack extract(ItemStack diskStack, Item itemToExtract, int maxAmount) {
        DiskComponent component = diskStack.get(SuperArchitectItems.DISK_COMPONENT);
        if (component == null || component.items() == null || component.items().isEmpty()) {
            return ItemStack.EMPTY;
        }

        List<DiskComponent.ItemEntry> currentItems = new java.util.ArrayList<>(component.items());
        ItemStack extracted = ItemStack.EMPTY;

        for (int i = 0; i < currentItems.size(); i++) {
            DiskComponent.ItemEntry existing = currentItems.get(i);
            if (existing.stack().is(itemToExtract)) {
                int amountToTake = Math.min(existing.count(), maxAmount);
                extracted = existing.stack().copyWithCount(amountToTake);
                
                if (existing.count() <= amountToTake) {
                    currentItems.remove(i);
                } else {
                    currentItems.set(i, new DiskComponent.ItemEntry(existing.stack(), existing.count() - amountToTake));
                }
                break;
            }
        }

        if (!extracted.isEmpty()) {
            diskStack.set(SuperArchitectItems.DISK_COMPONENT, new DiskComponent(currentItems));
        }

        return extracted;
    }
}

package com.hxngames.superarchitect.blockentities;

import com.hxngames.superarchitect.blocks.DiskRackBlock;
import com.hxngames.superarchitect.items.DiskItem;
import com.hxngames.superarchitect.menus.MonitorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MonitorBlockEntity extends BlockEntity implements MenuProvider {

    public MonitorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SuperArchitectBlockEntities.MONITOR, blockPos, blockState);
    }

    /**
     * Finds all connected DiskRackBlockEntities using BFS.
     */
    public List<DiskRackBlockEntity> getConnectedRacks() {
        if (this.level == null) return Collections.emptyList();

        List<DiskRackBlockEntity> racks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        visited.add(this.worldPosition);
        queue.add(this.worldPosition);

        int maxSearch = 128;

        while (!queue.isEmpty() && visited.size() < maxSearch) {
            BlockPos current = queue.poll();
            
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = current.relative(dir);
                if (!visited.contains(neighborPos)) {
                    BlockEntity be = this.level.getBlockEntity(neighborPos);
                    if (be instanceof DiskRackBlockEntity rack) {
                        visited.add(neighborPos);
                        queue.add(neighborPos);
                        racks.add(rack);
                    } else if (be instanceof MonitorBlockEntity) {
                        // Monitors also connect the network
                        visited.add(neighborPos);
                        queue.add(neighborPos);
                    }
                }
            }
        }
        return racks;
    }

    /**
     * Aggregates all items stored in the network.
     */
    public Map<Item, Integer> getNetworkItems() {
        Map<Item, Integer> items = new LinkedHashMap<>();
        for (DiskRackBlockEntity rack : getConnectedRacks()) {
            for (int i = 0; i < 4; i++) {
                ItemStack diskStack = rack.getInventory().getItem(i);
                if (!diskStack.isEmpty() && diskStack.getItem() instanceof DiskItem disk) {
                    var component = diskStack.get(com.hxngames.superarchitect.items.SuperArchitectItems.DISK_COMPONENT);
                    if (component != null && component.items() != null) {
                        for (var entry : component.items()) {
                            Item item = entry.stack().getItem();
                            items.put(item, items.getOrDefault(item, 0) + entry.count());
                        }
                    }
                }
            }
        }
        return items;
    }

    /**
     * Gets [totalUsed, totalMax] space in the network.
     */
    public int[] getNetworkStats() {
        int used = 0;
        int max = 0;
        for (DiskRackBlockEntity rack : getConnectedRacks()) {
            for (int i = 0; i < 4; i++) {
                ItemStack diskStack = rack.getInventory().getItem(i);
                if (!diskStack.isEmpty() && diskStack.getItem() instanceof DiskItem disk) {
                    used += disk.getUsed(diskStack);
                    max += disk.getMaxSize();
                }
            }
        }
        return new int[]{used, max};
    }

    /**
     * Attempts to insert an ItemStack into the network.
     * @return The remainder that could not fit.
     */
    public ItemStack insertIntoNetwork(ItemStack stack) {
        ItemStack remainder = stack.copy();
        for (DiskRackBlockEntity rack : getConnectedRacks()) {
            for (int i = 0; i < 4; i++) {
                ItemStack diskStack = rack.getInventory().getItem(i);
                if (!diskStack.isEmpty() && diskStack.getItem() instanceof DiskItem disk) {
                    remainder = disk.insert(diskStack, remainder);
                    rack.getInventory().setItem(i, diskStack); // Triggers state update/save
                    if (remainder.isEmpty()) {
                        return remainder;
                    }
                }
            }
        }
        return remainder;
    }

    /**
     * Attempts to extract an item from the network.
     * @return The extracted stack.
     */
    public ItemStack extractFromNetwork(Item item, int amount) {
        int leftToExtract = amount;
        ItemStack extracted = ItemStack.EMPTY;

        for (DiskRackBlockEntity rack : getConnectedRacks()) {
            for (int i = 0; i < 4; i++) {
                ItemStack diskStack = rack.getInventory().getItem(i);
                if (!diskStack.isEmpty() && diskStack.getItem() instanceof DiskItem disk) {
                    ItemStack result = disk.extract(diskStack, item, leftToExtract);
                    if (!result.isEmpty()) {
                        rack.getInventory().setItem(i, diskStack); // Triggers state update/save
                        if (extracted.isEmpty()) {
                            extracted = result;
                        } else {
                            extracted.grow(result.getCount());
                        }
                        leftToExtract -= result.getCount();
                        if (leftToExtract <= 0) {
                            return extracted;
                        }
                    }
                }
            }
        }
        return extracted;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.super_architect.monitor");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new MonitorMenu(syncId, playerInventory, this);
    }
}

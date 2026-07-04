package com.hxngames.superarchitect.blockentities;

import com.hxngames.superarchitect.blocks.DiskRackBlock;
import com.hxngames.superarchitect.items.DiskItem;
import com.hxngames.superarchitect.menus.DiskRackMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiskRackBlockEntity extends BlockEntity implements MenuProvider {
    private final SimpleContainer inventory = new SimpleContainer(4) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack stack) {
            return slot < 4 && stack.getItem() instanceof DiskItem;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    };

    public DiskRackBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(SuperArchitectBlockEntities.DISK_RACK, blockPos, blockState);
        this.inventory.addListener(container -> updateBlockState());
    }

    private void updateBlockState() {
        if (this.level == null || this.level.isClientSide) return;
        BlockState currentState = this.getBlockState();
        BlockState newState = currentState;

        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.inventory.getItem(i);
            int stage = getStageForDisk(stack);

            newState = switch (i) {
                case 0 -> newState.setValue(DiskRackBlock.BAY1, stage);
                case 1 -> newState.setValue(DiskRackBlock.BAY2, stage);
                case 2 -> newState.setValue(DiskRackBlock.BAY3, stage);
                case 3 -> newState.setValue(DiskRackBlock.BAY4, stage);
                default -> newState;
            };
        }

        if (newState != currentState) {
            this.level.setBlock(this.worldPosition, newState, 3);
        }
    }

    private int getStageForDisk(ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof DiskItem diskItem)) {
            return 0; // State 0: missing
        }
        int used = diskItem.getUsed(stack);
        int max = diskItem.getMaxSize();
        if (used == 0) {
            return 1; // Not empty per se, but < 50%
        }
        double percentage = (double) used / max;
        if (percentage >= 1.0) {
            return 3; // State 3: full
        } else if (percentage >= 0.5) {
            return 2; // State 2: > 50% and < 100%
        } else {
            return 1; // State 1: < 50%
        }
    }

    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
        for (int i = 0; i < 4; i++) {
            items.set(i, this.inventory.getItem(i));
        }
        ContainerHelper.saveAllItems(compoundTag, items, provider);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, items, provider);
        for (int i = 0; i < 4; i++) {
            this.inventory.setItem(i, items.get(i));
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.super_architect.disk_rack");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new DiskRackMenu(syncId, playerInventory, this.inventory);
    }
}

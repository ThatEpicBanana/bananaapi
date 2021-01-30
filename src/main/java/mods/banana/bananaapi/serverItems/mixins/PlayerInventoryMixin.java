package mods.banana.bananaapi.serverItems.mixins;

import mods.banana.bananaapi.serverItems.ServerItemHandler;
import mods.banana.bananaapi.serverItems.ServerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @ModifyVariable(method = "deserialize", at = @At("STORE"), ordinal = 0)
    private ItemStack onDeserialize(ItemStack stack) {
        for(ServerItem item : ServerItemHandler.getItems()) {
            if(item.matches(stack) && item.onTakenFromInventory(player, stack)) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }
}

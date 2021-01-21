package mods.banana.bananaapi.serverItems.mixins;

import mods.banana.bananaapi.serverItems.ServerItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {
    @ModifyVariable(method = "deserialize", at = @At("STORE"), ordinal = 0)
    private ItemStack onDeserialize(ItemStack stack) {
        for(ServerItem item : ServerItem.items) {
            if(item.preventSteal() && item.sameIdentifierAs(stack)) {
                return ItemStack.EMPTY;
            }
        }
        return stack;
    }
}

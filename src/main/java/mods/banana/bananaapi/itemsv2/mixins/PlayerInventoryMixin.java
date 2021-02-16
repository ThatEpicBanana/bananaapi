package mods.banana.bananaapi.itemsv2.mixins;

import mods.banana.bananaapi.itemsv2.events.CloseScreenCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
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
        return CloseScreenCallback.EVENT.invoker().removeItemFrom(
                (ServerPlayerEntity) player,
                player.currentScreenHandler,
                stack,
                -1
        ) ? ItemStack.EMPTY : stack;
    }
}

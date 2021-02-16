package mods.banana.bananaapi.itemsv2.mixins;

import mods.banana.bananaapi.itemsv2.events.CloseScreenCallback;
import mods.banana.bananaapi.serverItems.ServerItem;
import mods.banana.bananaapi.serverItems.ServerItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Shadow public abstract void setStackInSlot(int slot, ItemStack stack);

    @Inject(method = "close", at = @At("HEAD"))
    private void onClose(PlayerEntity player, CallbackInfo ci) {
        if(!player.world.isClient) {
            for(int i = 0; i < player.inventory.size(); i++) {
                if(CloseScreenCallback.EVENT.invoker().removeItemFrom(
                        (ServerPlayerEntity) player,
                        (ScreenHandler) (Object) this,
                        player.inventory.getStack(i),
                        i
                )) player.inventory.removeStack(i);
            }
        }
    }
}

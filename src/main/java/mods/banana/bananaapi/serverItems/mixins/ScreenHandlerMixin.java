package mods.banana.bananaapi.serverItems.mixins;

import mods.banana.bananaapi.serverItems.ServerItemHandler;
import mods.banana.bananaapi.serverItems.ServerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {
    @Inject(method = "close", at = {@At("HEAD")})
    private void onClose(PlayerEntity player, CallbackInfo ci) {
        for(int i = 0; i < player.inventory.size(); i++) {
            ItemStack stack = player.inventory.getStack(i);
            if(!stack.isEmpty()) {
                for(ServerItem item : ServerItemHandler.getItems()) {
                    if(item.matches(stack) && item.onTakenFromInventory(player, stack)) {
                        player.inventory.removeStack(i);
                    }
                }
            }
        }
    }
}

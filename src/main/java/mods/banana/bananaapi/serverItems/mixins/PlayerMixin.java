package mods.banana.bananaapi.serverItems.mixins;

import com.mojang.authlib.GameProfile;
import mods.banana.bananaapi.serverItems.ServerItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerMixin extends PlayerEntity implements ScreenHandlerListener {
    public PlayerMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "onDisconnect", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
//        for(int i = 0; i < inventory.size(); i++) {
//            ItemStack stack = inventory.getStack(i);
//            if(!stack.isEmpty()) {
//                for(ServerItem item : ServerItem.items) {
//                    if(item.preventSteal() && item.sameIdentifierAs(stack)) {
//                        inventory.removeStack(i);
//                    }
//                }
//            }
//        }
    }
}

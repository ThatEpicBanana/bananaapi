package mods.banana.bananaapi.itemsv2.mixins;

import mods.banana.bananaapi.itemsv2.events.DropItemCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(method = "spawnEntity", at = {@At("HEAD")}, cancellable = true)
    private void onSpawn(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if(entity instanceof ItemEntity) {
            if(DropItemCallback.EVENT.invoker().destroyDrop((ItemEntity) entity)) {
                cir.setReturnValue(false);
            }
        }
    }
}

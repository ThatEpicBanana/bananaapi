package mods.banana.bananaapi;

import mods.banana.bananaapi.serverItems.ServerItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public class BananaApi implements ModInitializer {
    public static MinecraftServer server;

    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register(server1 -> server = server1);

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if(world.isClient) return TypedActionResult.pass(ItemStack.EMPTY);
            ItemStack stack = player.getStackInHand(hand);
            for(ServerItem item : ServerItem.items) {
                if(item.sameIdentifierAs(stack)) {
                    if(item.onUse(stack, (ServerPlayerEntity) player, hand == Hand.MAIN_HAND ? player.inventory.selectedSlot : 40)) return TypedActionResult.success(player.getStackInHand(hand));
                }
            }
            return TypedActionResult.pass(ItemStack.EMPTY);
        });

    }
}

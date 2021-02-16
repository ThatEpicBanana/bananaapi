package mods.banana.bananaapi.itemsv2.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Callback for each item when closing a screen
 * Called before screen closes and when player disconnects (where i = -1)
 * Upon return:
 *  - true removes item
 *  - false does not remove item and continues checking other listeners
 */
public interface CloseScreenCallback {
    Event<CloseScreenCallback> EVENT = EventFactory.createArrayBacked(CloseScreenCallback.class,
            listeners -> (player, screenHandler, stack, i) -> {
                for (CloseScreenCallback event : listeners) {
                    if (event.removeItemFrom(player, screenHandler, stack, i)) {
                        return true;
                    }
                }

                return false;
            }
    );

    boolean removeItemFrom(ServerPlayerEntity player, ScreenHandler screenHandler, ItemStack stack, int i);
}

package mods.banana.bananaapi.itemsv2.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.ItemEntity;

/**
 * Callback when an item is dropped
 * Called whenever any item spawns
 *  - true removes item
 *  - false does not remove item and continues checking other listeners
 */
public interface DropItemCallback {
    Event<DropItemCallback> EVENT = EventFactory.createArrayBacked(DropItemCallback.class,
            listeners -> (itemEntity) -> {
                for (DropItemCallback event : listeners) {
                    if (event.destroyDrop(itemEntity)) {
                        return true;
                    }
                }

                return false;
            }
    );

    boolean destroyDrop(ItemEntity itemEntity);
}

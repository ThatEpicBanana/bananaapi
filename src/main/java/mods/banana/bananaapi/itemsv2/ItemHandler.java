package mods.banana.bananaapi.itemsv2;

import mods.banana.bananaapi.itemsv2.events.CloseScreenCallback;
import mods.banana.bananaapi.itemsv2.events.DropItemCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;

import java.util.ArrayList;

public class ItemHandler {
    public static ArrayList<CustomItem> items = new ArrayList<>();

    public static void register(CustomItem item) {
        items.add(item);
        if(item.getTags().contains(CustomItem.Tag.CLOSE)) CloseScreenCallback.EVENT.register(item);
        if(item.getTags().contains(CustomItem.Tag.USE)) UseItemCallback.EVENT.register(item);
        if(item.getTags().contains(CustomItem.Tag.DROP)) DropItemCallback.EVENT.register(item);
    }
}

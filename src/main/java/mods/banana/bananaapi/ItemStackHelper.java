package mods.banana.bananaapi;

import net.minecraft.item.ItemStack;

public class ItemStackHelper {
    public static ItemStack setCount(ItemStack itemStack, int count) {
        itemStack.setCount(count);
        return itemStack;
    }
}

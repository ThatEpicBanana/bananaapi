package mods.banana.bananaapi.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.Text;

import java.util.List;

public class ItemStackHelper {
    public static ItemStack setCount(ItemStack itemStack, int count) {
        itemStack.setCount(count);
        return itemStack;
    }

    public static ItemStack setTag(ItemStack stack, CompoundTag tag) {
        stack.setTag(tag);
        return stack;
    }

    public static ItemStack setName(ItemStack stack, Text text) {
        CompoundTag tag = stack.getTag();
        if(tag == null) tag = new CompoundTag();

        CompoundTag display = new CompoundTag();
        display.putString("Name", Text.Serializer.toJson(text));

        tag.put("display", display);

        stack.setTag(tag);

        return stack;
    }

    public static ItemStack setLore(ItemStack stack, List<Text> lines) {
        CompoundTag tag = stack.getTag();
        if(tag == null) tag = new CompoundTag();

        CompoundTag display = new CompoundTag();

        ListTag list = new ListTag();

        for(Text text : lines) {
            list.add(StringTag.of(Text.Serializer.toJson(text)));
        }

        display.put("Lore", list);

        tag.put("display", display);

        stack.setTag(tag);

        return stack;
    }
}

package mods.banana.bananaapi.helpers;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

public class ItemStackHelper {
    public static ItemStack setItem(ItemStack stack, Item item) {
        stack = ItemStackHelper.setTag(
                new ItemStack(item),
                stack.getTag()
        );
        return stack;
    }

    public static ItemStack setCount(ItemStack itemStack, int count) {
        itemStack.setCount(count);
        return itemStack;
    }

    public static ItemStack addCount(ItemStack itemStack, int count) {
        return setCount(itemStack, itemStack.getCount() + count);
    }

    public static ItemStack setTag(ItemStack stack, CompoundTag tag) {
        stack.setTag(tag);
        return stack;
    }

    public static ItemStack setName(ItemStack stack, Text text) {
        CompoundTag tag = stack.getTag();
        if(tag == null) tag = new CompoundTag();

        CompoundTag display = tag.getCompound("display");
        display.putString("Name", Text.Serializer.toJson(text));

        tag.put("display", display);

        stack.setTag(tag);

        return stack;
    }

    public static ItemStack addLore(ItemStack stack, List<Text> lines, int index) {
        List<Text> newLore = new ArrayList<>();

        if(stack.hasTag() && stack.getTag().contains("display") && stack.getTag().getCompound("display").contains("Lore")) {
            // get lore tag
            ListTag lore = stack.getTag().getCompound("display").getList("Lore", NbtType.STRING);
            // for each tag in the list
            for(int i = 0; i < lore.size(); i++) {
                // if index is the set add index, add all lines
                if(i == index) newLore.addAll(lines);
                // add text to lore
                newLore.add(Text.Serializer.fromJson(lore.getString(i)));
            }
        } else newLore.addAll(lines);

        return setLore(stack, newLore);
    }

    public static ItemStack setLore(ItemStack stack, List<Text> lines) {
        CompoundTag tag = stack.getTag();
        if(tag == null) tag = new CompoundTag();

        CompoundTag display = tag.getCompound("display");

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

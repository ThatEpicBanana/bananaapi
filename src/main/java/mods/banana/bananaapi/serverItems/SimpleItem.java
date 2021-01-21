package mods.banana.bananaapi.serverItems;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class SimpleItem extends ServerItem {
    public boolean preventDrop = false;
    public boolean preventSteal = false;

    public SimpleItem(ItemConvertible parent, Identifier identifier) {
        this.parent = parent;
        this.identifier = identifier;
        this.tag = new CompoundTag();
    }

    /**
     * Sets the item's customModelData to the input number
     * @param customModelData new data
     * @return the updated item
     */
    public SimpleItem withCustomModelData(int customModelData) {
        tag.putInt("CustomModelData", customModelData);
        return this;
    }

    /**
     * Sets the item's name to the input text
     * @param name new name
     * @return the updated item
     */
    public SimpleItem withName(Text name) {
        CompoundTag displayTag = new CompoundTag();
        displayTag.putString("Name", Text.Serializer.toJson(name));

        tag.put("display", displayTag);

        return this;
    }

    public SimpleItem setDropPrevention(boolean preventDrop) {
        this.preventDrop = preventDrop;
        return this;
    }

    public SimpleItem setStealPrevention(boolean preventSteal) {
        this.preventSteal = preventSteal;
        return this;
    }

    @Override
    public ItemStack getItemStack(int count) {
        CompoundTag tag = new CompoundTag();

        CompoundTag moduleTag = new CompoundTag();
        moduleTag.putString("type", identifier.getPath());

        tag.put(identifier.getNamespace(), moduleTag);

        ItemStack stack = new ItemStack(parent, count);
        stack.setTag(tag);

        return stack;
    }

    @Override
    public ItemStack convert(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag == null) tag = new CompoundTag();

        CompoundTag moduleTag = new CompoundTag();
        moduleTag.putString("type", identifier.getPath());

        tag.put(identifier.getNamespace(), moduleTag);

        stack.setTag(tag);

        return stack;
    }

    @Override
    public void setCustomTag(ItemStack stack, CompoundTag tag) {
        CompoundTag newTag = stack.getTag();
        if(newTag == null) newTag = new CompoundTag();

        newTag.put(identifier.getNamespace(), tag);

        stack.setTag(newTag);
    }

    @Override
    public boolean onItemEntitySpawn(ItemStack itemStack) { return preventDrop; }

    @Override
    public boolean onUse(ItemStack itemStack, ServerPlayerEntity player, int slot) {
        return true;
    }

    @Override
    public boolean preventSteal() {
        return preventSteal;
    }
}

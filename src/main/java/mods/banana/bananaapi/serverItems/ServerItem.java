package mods.banana.bananaapi.serverItems;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ServerItem {
    protected ItemConvertible parent;
    protected Identifier identifier;
    protected CompoundTag tag;
    
    public boolean preventDrop = false;
    public boolean preventSteal = false;

    public ServerItem(ItemConvertible parent, Identifier identifier) {
        this(parent, identifier, 1, true, null);
    }

    public ServerItem(ItemConvertible parent, Identifier identifier, int customModelData, boolean preventSteal, Text name) {
        this.parent = parent;
        this.identifier = identifier;
        this.tag = new CompoundTag();

        this.preventSteal = preventSteal;
        this.preventDrop = preventSteal;

        if(customModelData != 0) setCustomModelData(customModelData);
        if(name != null) setName(name);
    }

    /**
     * Sets the item's customModelData to the input number
     * @param customModelData new data
     * @return the updated item
     */
    public ServerItem setCustomModelData(int customModelData) {
        tag.putInt("CustomModelData", customModelData);
        return this;
    }

    /**
     * Sets the item's name to the input text
     * @param name new name
     * @return the updated item
     */
    public ServerItem setName(Text name) {
        CompoundTag displayTag = new CompoundTag();
        displayTag.putString("Name", Text.Serializer.toJson(name));

        tag.put("display", displayTag);

        return this;
    }

    public ServerItem setDropPrevention(boolean preventDrop) {
        this.preventDrop = preventDrop;
        return this;
    }

    public ServerItem setStealPrevention(boolean preventSteal) {
        this.preventSteal = preventSteal;
        return this;
    }

    public ItemStack getItemStack(int count) {
        CompoundTag tag = this.tag;

        CompoundTag moduleTag = new CompoundTag();
        moduleTag.putString("type", identifier.getPath());

        tag.put(identifier.getNamespace(), moduleTag);

        ItemStack stack = new ItemStack(parent, count);
        stack.setTag(tag);

        return stack;
    }

    public ItemStack convert(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if(tag == null) tag = new CompoundTag();

        CompoundTag moduleTag = new CompoundTag();
        moduleTag.putString("type", identifier.getPath());

        tag.put(identifier.getNamespace(), moduleTag);

        stack.setTag(tag);

        return stack;
    }

    public void setCustomTag(ItemStack stack, CompoundTag tag) {
        CompoundTag newTag = stack.getTag();
        if(newTag == null) newTag = new CompoundTag();

        newTag.put(identifier.getNamespace(), tag);

        stack.setTag(newTag);
    }

    public boolean matches(ItemStack stack) {
        if(stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if(tag.contains(identifier.getNamespace())) {
                return tag.getCompound(identifier.getNamespace()).getString("type").equals(identifier.getPath());
            }
        }
        return false;
    }

    public boolean onItemEntitySpawn(ItemStack itemStack) { return preventDrop; }
    public boolean onTakenFromInventory(PlayerEntity player, ItemStack stack) {
        return preventSteal;
    }

    public boolean onUse(ItemStack itemStack, ServerPlayerEntity player, int slot) {
        return true;
    }

    public CompoundTag getCustomTag(ItemStack stack) { return (CompoundTag) stack.getTag().get(identifier.getNamespace()); }

}

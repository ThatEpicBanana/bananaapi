package mods.banana.bananaapi.serverItems;

import mods.banana.bananaapi.helpers.ItemStackHelper;
import mods.banana.bananaapi.helpers.TagHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Deprecated
public class ServerItem {
    protected ItemConvertible parent;
    protected Identifier identifier;
    protected CompoundTag tag;
    
    public boolean preventDrop = false;
    public boolean preventSteal = false;

    public ServerItem(ItemConvertible parent, Identifier identifier) {
        this(parent, identifier, null);
    }

    public ServerItem(ItemConvertible parent, Identifier identifier, Text text) {
        this(parent, identifier, 1, true, text);
    }

    public ServerItem(ItemConvertible parent, Identifier identifier, int customModelData, boolean preventSteal, Text name) {
        this.parent = parent;
        this.identifier = identifier;
        this.tag = new CompoundTag();
        tag.put(identifier.getNamespace(), new CompoundTag());
        initTag();

        this.preventSteal = preventSteal;
        this.preventDrop = preventSteal;

        if(customModelData != 0) setCustomModelData(customModelData);
        if(name != null) setName(name);
    }

    private void initTag() {
        setCustomValue("type", StringTag.of(identifier.getPath()));
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

    public ItemStack getItemStack() { return getItemStack(1); }

    public ItemStack getItemStack(int count) {
        ItemStack stack = new ItemStack(parent, count);
        stack.setTag(this.tag.copy());

        return stack;
    }

    public ItemStack convert(ItemStack stack) {
        convertItem(stack);
        convertTag(stack);
        return stack;
    }

    public ItemStack convertItem(ItemStack stack) {
        return ItemStackHelper.setItem(stack, parent.asItem());
    }

    public ItemStack convertTag(ItemStack stack) {
        if(!stack.hasTag()) stack.setTag(new CompoundTag());

        stack.setTag(TagHelper.combine(stack.getTag(), this.tag));

        return stack;
    }

    public ItemStack setCustomTag(ItemStack stack, CompoundTag tag) {
        CompoundTag newTag = stack.getTag();
        if(newTag == null) newTag = new CompoundTag();

        newTag.put(identifier.getNamespace(), tag);

        stack.setTag(newTag);

        return stack;
    }

    public void setCustomTag(CompoundTag tag) {
        CompoundTag newTag = getTag();
        if(newTag == null) newTag = new CompoundTag();

        newTag.put(identifier.getNamespace(), tag);

        setTag(newTag);
    }

    public void setCustomValue(String key, Tag tag) {
        CompoundTag newTag = getCustomTag();
        newTag.put(key, tag);
        setCustomTag(newTag);
    }

    public ItemStack setCustomValue(ItemStack stack, String key, Tag tag) {
        if(!stack.hasTag()) stack.setTag(new CompoundTag());

        CompoundTag newTag = TagHelper.combine(getCustomTag(stack), getCustomTag());

        newTag.put(key, tag);

        return setCustomTag(stack, newTag);
    }

    public Tag getCustomValue(String key) {
        return getCustomTag().get(key);
    }

    public Tag getCustomValue(ItemStack stack, String key) {
        return getCustomTag(stack).get(key);
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

    public boolean itemMatches(ItemStack stack) {
        return getParent().equals(stack.getItem());
    }

    public boolean onItemEntitySpawn(ItemStack itemStack) { return preventDrop; }
    public boolean onTakenFromInventory(PlayerEntity player, ItemStack stack) {
        return preventSteal;
    }

    public boolean onUse(ItemStack itemStack, ServerPlayerEntity player, int slot) {
        return true;
    }

    public CompoundTag getCustomTag(ItemStack stack) { return (CompoundTag) stack.getTag().get(identifier.getNamespace()); }
    public CompoundTag getCustomTag() { return getTag().getCompound(identifier.getNamespace()); }

    public CompoundTag getTag() { return tag; }
    public void setTag(CompoundTag tag) { this.tag = tag; }

    public Identifier getIdentifier() { return identifier; }
    public ItemConvertible getParent() { return parent; }
}

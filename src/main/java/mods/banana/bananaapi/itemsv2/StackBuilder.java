package mods.banana.bananaapi.itemsv2;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StackBuilder {
    private Item item;
    private CompoundTag tag;
    private int count = 1;

    private Identifier id = null;

    private ItemStack toOverwrite;

    public StackBuilder() {
        this.tag = new CompoundTag();
    }

    public StackBuilder(ItemStack toOverwrite) {
        this.tag = toOverwrite.hasTag() ? toOverwrite.getTag() : new CompoundTag();
        this.item = toOverwrite.getItem();
        this.count = toOverwrite.getCount();

        this.toOverwrite = toOverwrite;
    }

    public ItemStack build() {
        return build(toOverwrite != null);
    }

    public ItemStack build(boolean overwrite) {
        ItemStack out = new ItemStack(item == null ? Items.AIR : item);
        out.setCount(count);
        out.setTag(tag);

        if(overwrite) {
            toOverwrite.setCount(count);
            toOverwrite.setTag(tag);
        }

        return out;
    }

    public StackBuilder item(Item item) {
        this.item = item;
        return this;
    }

    public StackBuilder tag(CompoundTag tag) {
        this.tag = tag;
        return this;
    }

    public StackBuilder count(int count) {
        this.count = count;
        return this;
    }

    public StackBuilder id(Identifier id) {
        this.id = id;

        CompoundTag namespaceTag = tag.getCompound(id.getNamespace());
        namespaceTag.putString("type", id.getPath());
        tag.put(id.getNamespace(), namespaceTag);
        return this;
    }

    public StackBuilder customValue(String key, Tag value) {
        if(id != null && !id.getNamespace().equals("")) {
            tag.getCompound(id.getNamespace()).put(key, value);
        } else return generalValue(key, value);
        return this;
    }

    public StackBuilder generalValue(String key, Tag value) {
        tag.put(key, value);
        return this;
    }


    public StackBuilder customString(String key, String string) {
        return customValue(key, StringTag.of(string));
    }

    public StackBuilder customUUID(String key, String uuid) {
        return customUUID(key, UUID.fromString(uuid));
    }

    public StackBuilder customUUID(String key, UUID uuid) {
        return customValue(key, NbtHelper.fromUuid(uuid));
    }

    public StackBuilder customPos(String key, BlockPos pos) {
        return customValue(key, NbtHelper.fromBlockPos(pos));
    }

    public StackBuilder customIdentifier(String key, Identifier identifier) {
        return customValue(key, StringTag.of(identifier.toString()));
    }

    public StackBuilder customBoolean(String key, boolean bool) {
        return customValue(key, ByteTag.of(bool));
    }


    private CompoundTag getDisplay() {
        if(!tag.contains("display", NbtType.COMPOUND))
            tag.put("display", new CompoundTag());
        return tag.getCompound("display");
    }


    private String getName() {
        return getDisplay().getString("Name");
    }

    public StackBuilder name(Text text) {
        getDisplay().putString("Name", Text.Serializer.toJson(text));
        return this;
    }

    public StackBuilder name(String text) {
        return name(new LiteralText(text));
    }


    private ListTag getLoreNbt() {
        return getDisplay().getList("Lore", NbtType.STRING);
    }

    private List<Text> getLore() {
        return getLoreNbt().stream()
                .map(tag1 -> Text.Serializer.fromJson(tag1.asString()))
                .collect(Collectors.toList());
    }

    public StackBuilder setLore(String string) {
        return setLore(new LiteralText(string));
    }

    public StackBuilder setLore(Text text) {
        return setLore(List.of(text));
    }

    public StackBuilder setLore(ListTag tag) {
        getDisplay().put("Lore", tag);
        return this;
    }

    public StackBuilder setLore(List<Text> texts) {
        ListTag tag = new ListTag();

        for(Text text : texts) {
            tag.add(StringTag.of(Text.Serializer.toJson(text)));
        }

        return setLore(tag);
    }

    public StackBuilder addLore(String string) {
        return addLore(new LiteralText(string));
    }

    public StackBuilder addLore(Text text) {
        ArrayList<Text> newLore = new ArrayList<>(getLore());
        newLore.add(text);
        return setLore(newLore);
    }

    public StackBuilder addLore(String text, int i) {
        return addLore(new LiteralText(text), i);
    }

    public StackBuilder addLore(Text text, int i) {
        ArrayList<Text> newLore = new ArrayList<>(getLore());
        newLore.add(i, text);
        return setLore(newLore);
    }

    public StackBuilder addLore(List<Text> texts, int i) {
        ArrayList<Text> newLore = new ArrayList<>(getLore());
        newLore.addAll(i, texts);
        return setLore(newLore);
    }


    public StackBuilder customModelData(int i) {
        tag.putInt("CustomModelData", i);
        return this;
    }
}

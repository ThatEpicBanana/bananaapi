package mods.banana.bananaapi.itemsv2;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import mods.banana.bananaapi.helpers.TextHelper;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StackBuilder implements Cloneable {
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

    public StackBuilder replaceLore(int i, Text text) {
        ListTag tag = getLoreNbt();
        tag.setTag(i, StringTag.of(Text.Serializer.toJson(text)));
        return setLore(tag);
    }

    public StackBuilder replaceLore(int i, List<Text> texts) {
        ListTag tag = getLoreNbt();

        // set all previous lines
        for(int j = 0; j < Math.min(tag.size(), texts.size()); j++) {
            tag.setTag(i + j, TextHelper.toTag(texts.get(j)));
        }

        // add all new lines
        for(int j = Math.min(tag.size(), texts.size()); j < texts.size(); j++) {
            tag.add(TextHelper.toTag(texts.get(j)));
        }

        // set lore
        return setLore(tag);
    }

    public StackBuilder addLore(String string) {
        return addLore(new LiteralText(string).setStyle(TextHelper.TRUE_RESET));
    }

    public StackBuilder addLore(Text text) {
        ArrayList<Text> newLore = new ArrayList<>(getLore());
        newLore.add(text);
        return setLore(newLore);
    }

    public StackBuilder addLore(String text, int i) {
        return addLore(new LiteralText(text).setStyle(TextHelper.TRUE_RESET), i);
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


    public StackBuilder enchant(Enchantment enchantment, int level) {
        ItemStack stack = build();
        stack.addEnchantment(enchantment, level);
        this.tag = stack.getTag();
        return this;
    }

    public StackBuilder clone() {
        try {
            return (StackBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Identifier getId() { return id; }
    private CompoundTag getTag() { return tag; }
    private int getCount() { return count; }
    private Item getItem() { return item; }

    public static class Serializer implements JsonSerializer<StackBuilder>, JsonDeserializer<StackBuilder> {
        private static final Gson GSON = new GsonBuilder()
                .registerTypeAdapter(StackBuilder.class, new StackBuilder.Serializer())
                .create();

        @Override
        public StackBuilder deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();

            StackBuilder builder = new StackBuilder();

            if(object.has("item")) {
                builder.item(Registry.ITEM.get(new Identifier(object.get("item").getAsString())));
            }

            if(object.has("tag")) {
                try {
                    builder.tag(StringNbtReader.parse(object.get("tag").getAsString()));
                } catch (CommandSyntaxException e) {
                    throw new JsonParseException(e);
                }
            }

            if(object.has("count")) {
                builder.count(object.get("count").getAsInt());
            }

            if(object.has("id")) {
                builder.id(new Identifier(object.get("id").getAsString()));
            }

            if(object.has("name")) {
                builder.name(Text.Serializer.fromJson(object.get("name")));
            }

            if(object.has("customModelData")) {
                builder.customModelData(object.get("customModelData").getAsInt());
            }

            if(object.has("lore")) {
                if(object.get("lore").isJsonPrimitive()) {
                    builder.addLore(object.get("lore").getAsString());

                } else if(object.get("lore").isJsonArray()) {
                    JsonArray loreJson = object.get("lore").getAsJsonArray();
                    ArrayList<Text> lore = new ArrayList<>();

                    for(JsonElement element : loreJson) {
                        lore.add(Text.Serializer.fromJson(element));
                    }

                    builder.setLore(lore);
                }
            }

            return builder;
        }

        @Override
        public JsonElement serialize(StackBuilder src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();

            if(src.getId() != null) object.addProperty("id", src.getId().toString());
            if(src.getTag() != null) object.addProperty("tag", src.getTag().toString());
            if(src.getCount() != 1) object.addProperty("count", src.getCount());
            if(src.getItem() != null) object.addProperty("item", Registry.ITEM.getId(src.getItem()).toString());

            return object;
        }

        public static StackBuilder fromJson(JsonElement element) {
            return GSON.fromJson(element, StackBuilder.class);
        }

        public static JsonElement toJson(StackBuilder object) {
            return GSON.toJsonTree(object);
        }
    }
}

package mods.banana.bananaapi.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public class TextHelper {
    public static Gson STYLE_SERIALIZER = new GsonBuilder().registerTypeAdapter(Style.class, new Style.Serializer()).create();

    public static Style TRUE_RESET = STYLE_SERIALIZER.fromJson("{\"bold\":false,\"italic\":false,\"underlined\":false,\"strikethrough\":false,\"obfuscated\":false,\"color\":\"white\"}", Style.class);

    /**
     * gets the lore of text
     * @param text The lore to be added
     * @return The display tag for an item
     */
    public static CompoundTag getLore(Text text) {
        CompoundTag displayTag = new CompoundTag();
        ListTag loreTag = new ListTag();

        loreTag.add(StringTag.of(Text.Serializer.toJson(text)));

        displayTag.put("Lore", loreTag);

        return displayTag;
    }

    public static StringTag toTag(Text text) {
        return StringTag.of(Text.Serializer.toJson(text));
    }

    public static MutableText literalText(String string) {
        return new LiteralText(string).setStyle(TRUE_RESET);
    }

    public static MutableText t(String string) {
        return literalText(string);
    }
}

package mods.banana.bananaapi.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class TextHelper {
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
}

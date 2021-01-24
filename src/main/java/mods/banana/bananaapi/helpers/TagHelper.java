package mods.banana.bananaapi.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.lwjgl.system.CallbackI;

public class TagHelper {
    public static CompoundTag combine(CompoundTag tag1, CompoundTag tag2) {
        CompoundTag output = tag1.copy();
        for(String key : tag2.getKeys()) {
            Tag tag = tag2.get(key);
            if(tag instanceof CompoundTag) {

                // if output already contains key
                if(output.contains(key)) {
                    // put the combined tags
                    output.put(key, combine((CompoundTag) tag, output.getCompound(key)));

                } else output.put(key, tag); // else just add the compound

            } else if(tag instanceof ListTag) {

                // if output already contains the tag
                if(output.contains(key)) {
                    // get the previous list
                    ListTag outputList = output.getList(key, tag.getType());

                    // iterate through each index of the previous list
                    for(Tag i : (ListTag)tag) {
                        // and add the new item to the output list
                        outputList.addTag(outputList.size(), i);
                    }
                } else output.put(key, tag); // else just add the list

            } else output.put(key, tag);
        }
        return output;
    }
}
package mods.banana.bananaapi.helpers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import org.lwjgl.system.CallbackI;

public class TagHelper {
    public static CompoundTag combine(CompoundTag tag1, CompoundTag tag2) {
        CompoundTag output;

        if(tag1 == null) output = new CompoundTag();
        else output = tag1.copy();

        for(String key : tag2.getKeys()) {
//            System.out.println(key);
            Tag tag = tag2.get(key);
            assert tag != null;
            tag = tag.copy();

            if(tag.getType() == 10) { // compound tag

                // if output already contains key
                if(output.contains(key)) {
                    // put the combined tags
                    output.put(key, combine(output.getCompound(key), (CompoundTag) tag));

                } else output.put(key, tag); // else just add the compound

            } else if(tag.getType() == 9) { // list tag

                // if output already contains the tag
                if(output.contains(key)) {
                    // get the previous list
                    ListTag outputList = output.getList(key, ((ListTag)tag).getElementType());

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

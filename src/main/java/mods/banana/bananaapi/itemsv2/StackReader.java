package mods.banana.bananaapi.itemsv2;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StackReader {
    private final ArrayList<Identifier> identifiers = new ArrayList<>();
    private final ItemStack stack;

    public StackReader(Identifier identifier, ItemStack stack) {
        this.identifiers.add(identifier);
        this.stack = stack;
    }

    public StackReader(ItemStack stack) {
        this.stack = stack;
        guessId();
    }

    public StackReader guessId() {
        CompoundTag stackTag = stack.getOrCreateTag();

        // check all keys
        for(String key : stackTag.getKeys()) {
            Tag value = stackTag.get(key);

            // check if key is a compound
            if(value.getType() == NbtType.COMPOUND) {
                CompoundTag tag = (CompoundTag) value;

                // check if tag is a builder stack
                if(tag.contains("type", NbtType.STRING)) {
                    identifiers.add(new Identifier(key, tag.getString("type")));
                }
            }
        }

        return this;
    }

    public List<CompoundTag> getCustomCompounds() {
        CompoundTag tag = stack.getOrCreateTag();

        ArrayList<CompoundTag> out = new ArrayList<>();

        for(Identifier identifier : identifiers) {
            out.add(tag.getCompound(identifier.getNamespace()));
        }

        return out;
    }

    public Tag getCustomValue(String key, Integer nbtType) {
        for(CompoundTag tag : getCustomCompounds()) {
            if(nbtType != null ? tag.contains(key, nbtType) : tag.contains(key)) {
                return tag.get(key);
            }
        }

        return null;
    }

    public Tag getCustomValue(String key) {
        return getCustomValue(key, null);
    }

    public boolean hasCustomValue(String key, Integer nbtType) {
        for(CompoundTag tag : getCustomCompounds()) {
            if(nbtType != null ? tag.contains(key, nbtType) : tag.contains(key)) return true;
        }
        return false;
    }

    public boolean hasCustomValue(String key) {
        return hasCustomValue(key, null);
    }

    public Tag getGeneralValue(String key, Integer nbtType) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.contains(key, nbtType) ? tag.get(key) : null;
    }

    public Tag getGeneralValue(String key) {
        return stack.getOrCreateTag().get(key);
    }


    public String getCustomString(String key) {
        return hasCustomValue(key, NbtType.STRING)
                ? getCustomValue(key, NbtType.STRING).asString()
                : null;
    }

    public UUID getCustomUUID(String key) {
        return hasCustomValue(key, NbtType.STRING)
                ? UUID.fromString(getCustomValue(key, NbtType.STRING).asString())
                : null;
    }

    public BlockPos getCustomPos(String key) {
        return hasCustomValue(key, NbtType.COMPOUND)
                ? NbtHelper.toBlockPos((CompoundTag) getCustomValue(key, NbtType.COMPOUND))
                : null;
    }

    public Identifier getCustomIdentifier(String key) {
        return hasCustomValue(key, NbtType.STRING)
                ? new Identifier(getCustomValue(key, NbtType.STRING).asString())
                : null;
    }

    public boolean getCustomBoolean(String key) {
        return hasCustomValue(key, NbtType.BYTE) && getCustomValue(key, NbtType.BYTE) == ByteTag.ONE;
    }


    public Text getName() {
        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("display", NbtType.COMPOUND) && tag.getCompound("display").contains("Name", NbtType.STRING)) {
            return Text.Serializer.fromJson(tag.getCompound("display").getString("Name"));
        }
        return null;
    }

    public List<Text> getLore() {
        ArrayList<Text> out = new ArrayList<>();

        CompoundTag tag = stack.getOrCreateTag();
        if(tag.contains("display", NbtType.COMPOUND) && tag.getCompound("display").contains("Lore", NbtType.LIST)) {
            for(Tag value : tag.getCompound("display").getList("Lore", NbtType.STRING)) {
                out.add(Text.Serializer.fromJson(value.asString()));
            }
        }

        return out;
    }

    public HoverEvent toHoverEvent() {
        return new HoverEvent(
                HoverEvent.Action.SHOW_ITEM,
                new HoverEvent.ItemStackContent(stack)
        );
    }
}

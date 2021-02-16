package mods.banana.bananaapi.itemsv2;

import mods.banana.bananaapi.itemsv2.events.CloseScreenCallback;
import mods.banana.bananaapi.itemsv2.events.DropItemCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class CustomItem implements UseItemCallback, CloseScreenCallback, DropItemCallback {
    private final Identifier id;
    private final Item item;

    private final boolean preventSteal;

    private final int customModelData;
    private final Text name;

    private final List<Tag> tags;

    public enum Tag {
        USE,
        CLOSE,
        DROP
    }

    public CustomItem(Identifier id, Item item, int customModelData, Text name, boolean preventSteal, List<Tag> tags) {
        this.id = id;
        this.item = item;
        this.customModelData = customModelData;
        this.name = name;
        this.preventSteal = preventSteal;
        this.tags = tags;
    }

    public CustomItem(Identifier id, Item item, Text name, List<Tag> tags) {
        this(id, item, 1, name, true, tags);
        if(!this.tags.contains(Tag.DROP)) this.tags.add(Tag.DROP);
        if(!this.tags.contains(Tag.CLOSE)) this.tags.add(Tag.CLOSE);
    }


    public boolean idMatches(ItemStack stack) {
        return id == null ||
                (stack.hasTag() &&
                stack.getTag().contains(getId().getNamespace(), NbtType.COMPOUND) &&
                stack.getTag().getCompound(getId().getNamespace()).contains("type", NbtType.STRING) &&
                stack.getTag().getCompound(getId().getNamespace()).getString("type").equals(getId().getPath()));
    }

    public boolean itemMatches(ItemStack stack) {
        return getItem() == null ||
                getItem().equals(stack.getItem());
    }

    public boolean matches(ItemStack stack) {
        return itemMatches(stack) && idMatches(stack);
    }


    public ItemStack convert(ItemStack stack) {
        return toBuilder(stack, true).build();
    }

    public ItemStack convertTag(ItemStack stack) {
        return toBuilder(stack, false).build();
    }


    public StackBuilder toBuilder() {
        return toBuilder(null, true);
    }

    public StackBuilder toBuilder(ItemStack stack, boolean setItem) {
        StackBuilder builder = stack == null ? new StackBuilder() : new StackBuilder(stack);

        if(getItem() != null && setItem) builder.item(getItem());
        if(getId() != null) builder.id(getId());
        if(getCustomModelData() != 0) builder.customModelData(getCustomModelData());
        if(getName() != null) builder.name(getName());

        return builder;
    }

    public StackReader toReader(ItemStack stack) {
        return new StackReader(getId(), stack);
    }

    public ItemStack getItemStack() {
        return toBuilder().build();
    }

    public Identifier getId() { return id; }
    public Item getItem() { return item; }
    public int getCustomModelData() { return customModelData; }
    public Text getName() { return name; }
    public boolean isPreventSteal() { return preventSteal; }
    public List<Tag> getTags() { return tags; }

    /**
     * Called before screen closes and when player disconnects (where i = -1)
     * NOTE: to use this, you must specify the tag CustomItem.Tag.CLOSE
     * Upon return:
     *  - true removes item
     *  - false does not remove item and continues checking other listeners
     */
    @Override
    public boolean removeItemFrom(ServerPlayerEntity player, ScreenHandler screenHandler, ItemStack stack, int i) {
        return isPreventSteal() && matches(stack);
    }

    /**
     * Callback for right-clicking ("using") an item. Is hooked in before the spectator check, so make sure to check for the player's game mode as well!
     * NOTE: to use this, you must specify the tag CustomItem.Tag.USE
     * Upon return:
     *  - SUCCESS cancels further processing and, on the client, sends a packet to the server.
     *  - PASS falls back to further processing.
     *  - FAIL cancels further processing and does not send a packet to the server.
     */
    public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
        return TypedActionResult.pass(ItemStack.EMPTY);
    }

    /**
     * Callback when an item is dropped
     * Called whenever any item spawns
     * NOTE: to use this, you must specify the tag CustomItem.Tag.DROP
     * Upon return:
     *  - true removes item
     *  - false does not remove item and continues checking other listeners
     */
    @Override
    public boolean destroyDrop(ItemEntity itemEntity) {
        return isPreventSteal() && matches(itemEntity.getStack());
    }

    public static class Builder {
        Identifier id = null;
        Item item = null;

        int customModelData = 1;
        Text name = null;

        boolean preventSteal = true;
        List<Tag> tags = new ArrayList<>();

        public Builder() {}

        public Builder id(Identifier id) {
            this.id = id;
            return this;
        }

        public Builder id(String namespace, String path) {
            return id(new Identifier(namespace, path));
        }

        public Builder item(Item item) {
            this.item = item;
            return this;
        }

        public Builder customModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        public Builder name(String name) {
            return name(new LiteralText(name));
        }

        public Builder name(Text name) {
            this.name = name;
            return this;
        }

        public Builder preventSteal(boolean preventSteal) {
            this.preventSteal = preventSteal;
            return this;
        }

        public CustomItem build() {
            if(preventSteal) {
                tags.add(Tag.CLOSE);
                tags.add(Tag.DROP);
            }
            return new CustomItem(id, item, customModelData, name, preventSteal, tags);
        }
    }
}

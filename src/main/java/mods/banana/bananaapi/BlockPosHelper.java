package mods.banana.bananaapi;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

public class BlockPosHelper {
    /**
     * gets a block position from a tag defined by an x, y, and z
     * ex: {x:0, y:0, z:0}
     * @param tag Input tag
     * @return new block position
     */
    public static BlockPos fromTag(CompoundTag tag) {
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }

    /**
     * gets a tag from a block position
     * ex: {x:0, y:0, z:0}
     * @param pos block position to be converted
     * @return new compound tag
     */
    public static CompoundTag toTag(BlockPos pos) {
        CompoundTag out = new CompoundTag();
        out.putInt("x", pos.getX());
        out.putInt("y", pos.getY());
        out.putInt("z", pos.getZ());
        return out;
    }

    /**
     * gets a string from a block position
     */
    public static String toString(BlockPos pos) {
        return pos.getX() + ", " + pos.getY() + ", " + pos.getZ();
    }
}

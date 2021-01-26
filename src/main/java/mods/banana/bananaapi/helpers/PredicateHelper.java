package mods.banana.bananaapi.helpers;

import mods.banana.bananaapi.BananaApi;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.server.world.ServerWorld;

public class PredicateHelper {
    public static boolean test(LootCondition condition, ItemStack stack) {
        return condition.test(new LootContext.Builder(BananaApi.server.getWorld(ServerWorld.OVERWORLD)) // have to pass in world to get server
                .parameter(LootContextParameters.TOOL, stack) // use tool as it's the most simple
                .build( // pass in builder
                        new LootContextType.Builder()
                                .require(LootContextParameters.TOOL) // has to specify if parameters are used
                                .build()
                )
        );
    }
}

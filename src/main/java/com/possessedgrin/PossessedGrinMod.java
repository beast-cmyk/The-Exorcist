package com.possessedgrin;

import com.possessedgrin.haunt.OuijaBoardItem;
import com.possessedgrin.power.PossessedPowers;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

/** Common (client + server) start-up: the Ouija board and the possession powers. */
public final class PossessedGrinMod implements ModInitializer {
    public static final Item OUIJA_BOARD = new OuijaBoardItem(new Item.Settings().maxCount(1).group(ItemGroup.MISC));

    @Override
    public void onInitialize() {
        Registry.register(Registry.ITEM, PossessedGrin.id("ouija_board"), OUIJA_BOARD);
        PossessedPowers.init();
    }
}

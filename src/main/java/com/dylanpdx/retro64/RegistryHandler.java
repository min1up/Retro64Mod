package com.dylanpdx.retro64;

import com.dylanpdx.retro64.blocks.CastleStairsBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class RegistryHandler {

    public static final Block EXAMPLE_BLOCK = new CastleStairsBlock(FabricBlockSettings.of(Material.STONE).strength(4.0f));
    
    public static void init() {
        Registry.register(Registry.BLOCK, new Identifier(Retro64.MOD_ID, "castlestairs"), EXAMPLE_BLOCK);
        Registry.register(Registry.ITEM, new Identifier(Retro64.MOD_ID, "castlestairs"), new BlockItem(EXAMPLE_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
    }
    
    //CASTLE_STAIRS = ITEM.register("castlestairs", CastleStairsBlock::new);

}


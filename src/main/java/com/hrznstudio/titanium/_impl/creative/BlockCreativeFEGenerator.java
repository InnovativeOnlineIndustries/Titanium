package com.hrznstudio.titanium._impl.creative;

import com.hrznstudio.titanium._impl.creative.tile.TileCreativeFEGenerator;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockTileBase;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class BlockCreativeFEGenerator extends BlockTileBase<TileCreativeFEGenerator> {

    public static BlockCreativeFEGenerator INSTANCE = new BlockCreativeFEGenerator();

    public BlockCreativeFEGenerator() {
        super("creative_fe_generator", Block.Properties.from(Blocks.BEDROCK), TileCreativeFEGenerator.class);
    }

    @Override
    public IFactory<TileCreativeFEGenerator> getTileEntityFactory() {
        return TileCreativeFEGenerator::new;
    }
}

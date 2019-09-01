package com.hrznstudio.titanium._impl.test;

import com.hrznstudio.titanium._impl.test.tile.TileAssetTest;
import com.hrznstudio.titanium.api.IFactory;
import com.hrznstudio.titanium.block.BlockRotation;
import net.minecraft.block.material.Material;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class BlockAssetTest extends BlockRotation<TileAssetTest>{
    public static BlockAssetTest TEST;

    public BlockAssetTest() {
        super("block_asset_test", Properties.create(Material.ROCK), TileAssetTest.class);
    }

    @Override
    public IFactory<TileAssetTest> getTileEntityFactory() {
        return TileAssetTest::new;
    }

    @Nonnull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public List<Pool> getStaticDrops() {
        return new ArrayList<>();
    }
}

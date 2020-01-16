package com.hrznstudio.titanium.api.multiblock;

import com.google.common.collect.Maps;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class ShapedTemplate implements ITemplate {
    private Map<Direction, Map<BlockPos, StructureBlockInformation>> Structure_Map = Maps.newHashMap();

    public ShapedTemplate(Direction direction, Map<BlockPos, StructureBlockInformation> structureBlockInfoMap) {
        addTemplate(direction, structureBlockInfoMap);
        for (Direction dir : Direction.values()) {
            if (dir.equals(direction) || dir.equals(Direction.UP) || dir.equals(Direction.DOWN)) return;
            
        }
    }

    public Map<Direction, Map<BlockPos, StructureBlockInformation>> getStructure_Map() {
        return Structure_Map;
    }

    @Override
    public ITemplate addTemplate(Direction direction, Map<BlockPos, StructureBlockInformation> structureBlockInfoMap) {
        Structure_Map.put(direction, structureBlockInfoMap);
        return this;
    }
}

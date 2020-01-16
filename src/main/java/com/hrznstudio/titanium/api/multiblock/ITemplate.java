package com.hrznstudio.titanium.api.multiblock;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public interface ITemplate {
    ITemplate addTemplate(Direction direction, Map<BlockPos, StructureBlockInformation> structureBlockInfoMap);

}

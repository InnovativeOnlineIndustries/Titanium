package com.hrznstudio.titanium.block.tile.fluid;

import com.hrznstudio.titanium.block.tile.TileBase;
import com.hrznstudio.titanium.container.capability.IFacingHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.function.Predicate;

public class PosFluidTank extends FluidTank implements IFluidTankProperties, IFacingHandler {

    private final int posX;
    private final int posY;
    private Predicate<FluidStack> fillPredicate;
    private Predicate<FluidStack> drainPredicate;
    private int color;
    private String name;
    private HashMap<EnumFacing, FaceMode> facingModes;

    public PosFluidTank(int amount, int posX, int posY, String name) {
        super(amount);
        this.posX = posX;
        this.posY = posY;
        this.color = 0;
        this.name = name;
        this.fillPredicate = fluidStack1 -> true;
        this.drainPredicate = fluidStack1 -> true;
        this.facingModes = new HashMap<>();
        for (EnumFacing facing : EnumFacing.values()) {
            this.facingModes.put(facing, FaceMode.ENABLED);
        }
    }

    public PosFluidTank setFillFilter(Predicate<FluidStack> filter) {
        this.fillPredicate = filter;
        return this;
    }

    public PosFluidTank setDrainFilter(Predicate<FluidStack> filter) {
        this.drainPredicate = filter;
        return this;
    }

    public PosFluidTank setTile(TileEntity tile) {
        this.tile = tile;
        return this;
    }

    @Override
    public HashMap<EnumFacing, FaceMode> getFacingModes() {
        return facingModes;
    }

    @Nullable
    @Override
    public FluidStack getContents() {
        return getFluid();
    }

    @Override
    public boolean canFillFluidType(FluidStack fluidStack) {
        return fluidStack != null && fillPredicate.test(fluidStack);
    }

    @Override
    public boolean canDrainFluidType(FluidStack fluidStack) {
        return fluidStack != null && drainPredicate.test(fluidStack);
    }

    @Override
    protected void onContentsChanged() {
        super.onContentsChanged();
        if (tile instanceof TileBase) {
            ((TileBase) tile).markForUpdate();
        } else {
            tile.markDirty();
        }
    }

    @Override
    public int getColor() {
        return new Color(color).getRGB();
    }

    public PosFluidTank setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(posX, posY, 18 - 1, 46 - 1);
    }

    @Override
    public FluidTank readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("FacingModes")) {
            NBTTagCompound compound = nbt.getCompound("FacingModes");
            for (String face : compound.keySet()) {
                facingModes.put(EnumFacing.byName(face), FaceMode.valueOf(compound.getString(face)));
            }
        }
        return super.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound comp) {
        NBTTagCompound nbt = super.writeToNBT(comp);
        NBTTagCompound compound = new NBTTagCompound();
        for (EnumFacing facing : facingModes.keySet()) {
            compound.setString(facing.getName(), facingModes.get(facing).name());
        }
        nbt.setTag("FacingModes", compound);
        return nbt;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}

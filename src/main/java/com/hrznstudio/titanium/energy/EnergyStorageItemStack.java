/*
 * This file is part of Titanium
 * Copyright (C) 2026, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.energy;

import com.hrznstudio.titanium.attachment.StoredEnergyAttachment;
import com.hrznstudio.titanium.item.EnergyItem;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class EnergyStorageItemStack implements EnergyHandler {
    private final ItemAccess access;
    private final EnergyItem item;

    public EnergyStorageItemStack(ItemAccess access, EnergyItem item) {
        this.access = access;
        this.item = item;
    }

    public void putInternal(int energy) {
        update(Math.min(getAmountAsInt() + energy, getCapacityAsInt()), null);
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonNegative(amount);
        int inserted = Math.min(getCapacityAsInt() - getAmountAsInt(), Math.min(item.getInput(), amount));
        return inserted > 0 && update(getAmountAsInt() + inserted, transaction) ? inserted : 0;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        TransferPreconditions.checkNonNegative(amount);
        int extracted = Math.min(getAmountAsInt(), Math.min(item.getOutput(), amount));
        return extracted > 0 && update(getAmountAsInt() - extracted, transaction) ? extracted : 0;
    }

    @Override
    public long getAmountAsLong() {
        return get().stored();
    }

    @Override
    public long getCapacityAsLong() {
        return item.getCapacity();
    }

    private boolean update(int stored, TransactionContext transaction) {
        ItemResource current = access.getResource();
        if (!current.is(item)) return false;
        StoredEnergyAttachment attachment = new StoredEnergyAttachment(stored, item.getCapacity(), item.getInput(), item.getOutput());
        ItemResource updated = current.with(StoredEnergyAttachment.TYPE, attachment);
        return !updated.isEmpty() && access.exchange(updated, access.getAmount(), transaction) == access.getAmount();
    }

    public StoredEnergyAttachment get() {
        return access.getResource().getOrDefault(StoredEnergyAttachment.TYPE, new StoredEnergyAttachment(item));
    }
}

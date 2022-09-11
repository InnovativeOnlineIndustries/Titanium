/*
 * This file is part of Titanium
 * Copyright (C) 2022, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block_network.graph;


import com.hrznstudio.titanium.api.block_network.NetworkElement;
import com.hrznstudio.titanium.block_network.Network;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Set;

public class NetworkGraph {
    private final Network network;

    private Set<NetworkElement> elements = new HashSet<>();


    public NetworkGraph(Network network) {
        this.network = network;
    }

    public NetworkGraphScannerResult scan(Level originLevel, BlockPos originPos) {
        NetworkGraphScanner scanner = new NetworkGraphScanner(elements, network.getType());

        NetworkGraphScannerResult result = scanner.scanAt(originLevel, originPos);

        this.elements = result.getFoundElements();

        result.getNewElements().forEach(p -> p.joinNetwork(network));
        result.getRemovedElements().forEach(NetworkElement::leaveNetwork);


        return result;
    }

    public Set<NetworkElement> getElements() {
        return elements;
    }


}

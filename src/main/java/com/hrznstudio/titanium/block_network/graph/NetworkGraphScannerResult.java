/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block_network.graph;


import com.hrznstudio.titanium.api.block_network.NetworkElement;

import java.util.List;
import java.util.Set;

public class NetworkGraphScannerResult {
    private final Set<NetworkElement> foundElements;
    private final Set<NetworkElement> newElements;
    private final Set<NetworkElement> removedElements;

    private final List<NetworkGraphScannerRequest> requests;

    public NetworkGraphScannerResult(Set<NetworkElement> foundElements, Set<NetworkElement> newElemenets, Set<NetworkElement> removedElements, List<NetworkGraphScannerRequest> requests) {
        this.foundElements = foundElements;
        this.newElements = newElemenets;
        this.removedElements = removedElements;
        this.requests = requests;
    }

    public Set<NetworkElement> getFoundElements() {
        return foundElements;
    }

    public Set<NetworkElement> getNewElements() {
        return newElements;
    }

    public Set<NetworkElement> getRemovedElements() {
        return removedElements;
    }

    public List<NetworkGraphScannerRequest> getRequests() {
        return requests;
    }
}

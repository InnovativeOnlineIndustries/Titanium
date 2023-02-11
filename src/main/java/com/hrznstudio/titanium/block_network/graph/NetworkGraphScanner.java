/*
 * This file is part of Titanium
 * Copyright (C) 2023, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.block_network.graph;

import com.hrznstudio.titanium.api.block_network.NetworkElement;
import com.hrznstudio.titanium.block_network.NetworkManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.*;

public class NetworkGraphScanner {
    private final Set<NetworkElement> foundElements = new HashSet<>();
    private final Set<NetworkElement> newElements = new HashSet<>();
    private final Set<NetworkElement> removedElements = new HashSet<>();

    private final Set<NetworkElement> currentElements;
    private final ResourceLocation requiredNetworkType;

    private final List<NetworkGraphScannerRequest> allRequests = new ArrayList<>();
    private final Queue<NetworkGraphScannerRequest> requests = new ArrayDeque<>();

    public NetworkGraphScanner(Set<NetworkElement> currentElements, ResourceLocation requiredNetworkType) {
        this.currentElements = currentElements;
        this.removedElements.addAll(currentElements);
        this.requiredNetworkType = requiredNetworkType;
    }

    public NetworkGraphScannerResult scanAt(Level level, BlockPos pos) {
        addRequest(new NetworkGraphScannerRequest(level, pos, null, null));

        NetworkGraphScannerRequest request;
        while ((request = requests.poll()) != null) {
            singleScanAt(request);
        }

        return new NetworkGraphScannerResult(
            foundElements,
            newElements,
            removedElements,
            allRequests
        );
    }

    private void singleScanAt(NetworkGraphScannerRequest request) {
        NetworkElement pipe = NetworkManager.get(request.getLevel()).getElement(request.getPos());

        if (pipe != null) {
            if (!requiredNetworkType.equals(pipe.getNetworkType())) {
                return;
            }

            if (foundElements.add(pipe)) {
                if (!currentElements.contains(pipe)) {
                    newElements.add(pipe);
                }

                removedElements.remove(pipe);

                request.setSuccessful(true);

                for (Direction dir : Direction.values()) {
                    addRequest(new NetworkGraphScannerRequest(
                        request.getLevel(),
                        request.getPos().relative(dir),
                        dir,
                        request
                    ));
                }
            }
        }
    }

    private void addRequest(NetworkGraphScannerRequest request) {
        requests.add(request);
        allRequests.add(request);
    }
}

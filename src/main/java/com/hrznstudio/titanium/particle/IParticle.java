/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>, All rights reserved.
 *
 * This means no, you cannot use this code. This is licensed for sole use by Horizon and it's partners, you MUST be granted specific written permission from Horizon to use this code.
 */

package com.hrznstudio.titanium.particle;

public interface IParticle {

    /**
     * Checks if the given particle is alive currently
     *
     * @return True if the particle is alive
     */
    boolean alive();

    /**
     * Checks if the particle has the additive flag
     *
     * @return True if the particle is additive
     */
    boolean isAdditive();

    /**
     * Checks if the particle should render when behind/inside a block
     *
     * @return True if the particle should render behind/inside blocks
     */
    boolean renderThroughBlocks();
}

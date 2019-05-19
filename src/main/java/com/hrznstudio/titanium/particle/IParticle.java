/*
 * This file is part of Titanium
 * Copyright (C) 2019, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
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

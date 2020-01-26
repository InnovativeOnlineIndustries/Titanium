/*
 * This file is part of Titanium
 * Copyright (C) 2020, Horizon Studio <contact@hrznstudio.com>.
 *
 * This code is licensed under GNU Lesser General Public License v3.0, the full license text can be found in LICENSE.txt
 */

package com.hrznstudio.titanium.api.multiblock.matcher;

import com.hrznstudio.titanium.Titanium;
import net.minecraftforge.eventbus.api.Event;

public class ResultHandler {

    final int x;
    final Event.Result result;
    public static final ResultHandler DEFAULT = new ResultHandler(0, Event.Result.DEFAULT);

    public ResultHandler(int x, Event.Result result) {
        this.x = x;
        this.result = result;
    }

    public boolean isAllowed() {
        return result == Event.Result.ALLOW;
    }

    public boolean isDefault() {
        return result == Event.Result.DEFAULT;
    }

    public boolean isDenied() {
        return result == Event.Result.DENY;
    }

    public static ResultHandler combine(ResultHandler a, ResultHandler b) {
        if (Math.abs(a.x) > Math.abs(b.x)) {
            return a;
        } else if(Math.abs(a.x) < Math.abs(b.x)) {
            return b;
        } else {
            Titanium.LOGGER.info("Can't combine same valued results");
            return a;
        }
    }

    public static ResultHandler allowed(int x) {
        ResultHandler handler = null;
        if(x > 0) {
            handler = new ResultHandler(x, Event.Result.ALLOW);
        }
        return handler;
    }

    public static ResultHandler denied(int x) {
        ResultHandler handler = null;
        if(x > 0) {
            handler = new ResultHandler(x, Event.Result.DENY);
        }
        return handler;
    }
}

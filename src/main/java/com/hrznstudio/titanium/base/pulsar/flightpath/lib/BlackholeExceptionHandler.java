package com.hrznstudio.titanium.base.pulsar.flightpath.lib;

import com.hrznstudio.titanium.base.pulsar.flightpath.IExceptionHandler;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Default exception handler when Flightpath has been asked to ignore errors.
 *
 * @author Arkan <arkan@drakon.io>
 */
@ParametersAreNonnullByDefault
public class BlackholeExceptionHandler implements IExceptionHandler {

    @Override
    public void handle(Exception ex) {
        // NO-OP
    }

    @Override
    public void flush() {
        // NO-OP
    }

}

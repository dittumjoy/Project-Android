package com.app.thoughtsharing;

import com.squareup.otto.Bus;

/**
 * Created by dewneot-pc on 12/15/2015.
 */
public final class BusProvider {
    private static final Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }

    private BusProvider() {
        // No instances.
    }
}
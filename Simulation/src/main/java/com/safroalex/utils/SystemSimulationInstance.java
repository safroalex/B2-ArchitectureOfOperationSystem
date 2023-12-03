package com.safroalex.utils;

import com.safroalex.logic.Statistics;
import com.safroalex.logic.SystemSimulation;

public class SystemSimulationInstance {
    private static SystemSimulation instance;

    public static void initialize(Statistics mainStatistic, int totalSources, int totalDevices, double minInterval, double maxInterval, int bufferCapacity) {
        if (instance == null) {
            instance = new SystemSimulation(mainStatistic, totalSources, totalDevices, minInterval, maxInterval, bufferCapacity);
        }
    }

    public static SystemSimulation getInstance() {
        return instance;
    }
}


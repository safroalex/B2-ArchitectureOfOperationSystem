package com.safroalex.utils;

public class SimulationParameters {
    private static int totalSteps;
    private static int totalSources;     // Количество источников
    private static int totalDevices;     // Количество приборов (больше, чем источников)
    private static double minInterval; // Минимальный интервал между заявками
    private static double maxInterval;   // Максимальный интервал между заявками
    private static int bufferCapacity;   // Вместимость буффера


    public static void setTotalSteps(int steps, int sources, int devices, double mininterval, double maxinterval, int capacity) {
        totalSteps = steps;
        totalSources = sources;
        totalDevices = devices;
        minInterval = mininterval;
        maxInterval = maxinterval;
        bufferCapacity = capacity;
    }

    public static int getTotalSteps() {
        return totalSteps;
    }

    // Геттер для totalSources
    public static int getTotalSources() {
        return totalSources;
    }

    // Геттер для totalDevices
    public static int getTotalDevices() {
        return totalDevices;
    }

    // Геттер для minInterval
    public static double getMinInterval() {
        return minInterval;
    }

    // Геттер для maxInterval
    public static double getMaxInterval() {
        return maxInterval;
    }

    // Геттер для bufferCapacity
    public static int getBufferCapacity() {
        return bufferCapacity;
    }
}

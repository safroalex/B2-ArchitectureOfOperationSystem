package com.safroalex.logic;

public class TestSimulation {
    public static void main(String[] args) {
        // Параметры
        int totalSources = 3;     // Количество источников
        int totalDevices = 5;     // Количество приборов (больше, чем источников)
        double minInterval = 5; // Минимальный интервал между заявками
        double maxInterval = 6; // Максимальный интервал между заявками
        int totalSteps = 10;      // Количество шагов моделирования

        SystemSimulation simulation = new SystemSimulation(totalSources, totalDevices, minInterval, maxInterval);

        for (int i = 0; i < totalSteps; i++) {
            System.out.println("Шаг " + (i + 1));
            simulation.step();
        }
    }
}

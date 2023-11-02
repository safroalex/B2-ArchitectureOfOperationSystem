package com.safroalex.logic;

public class TestSimulation {
    public static void main(String[] args) {
        // Параметры
        int totalSources = 5;     // Количество источников
        int totalDevices = 2;     // Количество приборов (больше, чем источников)
        double minInterval = 1.5; // Минимальный интервал между заявками
        double maxInterval = 2; // Максимальный интервал между заявками
        int totalSteps = 5;      // Количество шагов моделирования
        int bufferCapacity = 5; // Вместимость буффера

        SystemSimulation simulation
                = new SystemSimulation
                (totalSources, totalDevices, minInterval, maxInterval, bufferCapacity);

        for (int i = 0; i < totalSteps; i++) {
            System.out.println("Шаг " + (i + 1));
            simulation.step();
        }

        System.out.println(RejectionHandler.getRejectedRequests());

    }
}

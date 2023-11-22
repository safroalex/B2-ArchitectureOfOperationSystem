package com.safroalex.logic;

public class TestSimulation {
    public static void main(String[] args) {
        // Параметры
        int totalSources = 5;     // Количество источников
        int totalDevices = 2;     // Количество приборов (больше, чем источников)
        double minInterval = 1.5; // Минимальный интервал между заявками
        double maxInterval = 2;   // Максимальный интервал между заявками
        int totalSteps = 5;       // Количество шагов моделирования
        int bufferCapacity = 5;   // Вместимость буффера

        Statistics mainStatistic = new Statistics();

        SystemSimulation simulation
                = new SystemSimulation
                (mainStatistic ,totalSources, totalDevices, minInterval, maxInterval, bufferCapacity);

        for (int i = 0; i < totalSteps; i++) {
            System.out.println("=====================================");
            System.out.println("shag " + (i + 1));
            simulation.step();
            System.out.println(simulation.getStatus());
        }

        System.out.println("=====================================");
        System.out.println("otklonennye-zayavki:");
        System.out.println(RejectionHandler.getRejectedRequests());

         System.out.println(simulation.getStatistics());
    }
}

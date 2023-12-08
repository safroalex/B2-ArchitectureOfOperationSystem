package com.safroalex.logic;

public class TestSimulation {
    public static void main(String[] args) {
        // Параметры
        int totalSources = 5;     // Количество источников
        int totalDevices = 3;     // Количество приборов
        double minInterval = 1.5; // Минимальный интервал между заявками
        double maxInterval = 2;   // Максимальный интервал между заявками
        int totalSteps = 100;       // Количество шагов моделирования
        int bufferCapacity = 5;   // Вместимость буффера

        Statistics statistic = new Statistics();

        SystemSimulation simulation
                = new SystemSimulation
                (statistic ,totalSources, totalDevices,
                        minInterval, maxInterval, bufferCapacity);

        for (int i = 0; i < totalSteps; i++) {
            System.out.println("=====================================");
            System.out.println("shag " + (i + 1));
            simulation.step();
            System.out.println(simulation.getAndSetStatus());
        }

        System.out.println("=====================================");
        System.out.println("REJECTED:");
        System.out.println(RejectionHandler.getRejectedRequests());

        System.out.println(simulation.getStatistics());
        System.out.println(statistic.getTotalServiceTimeBySource(3));
    }
}

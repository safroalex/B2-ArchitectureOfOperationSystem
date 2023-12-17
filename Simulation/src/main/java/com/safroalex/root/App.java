package com.safroalex.root;

import com.safroalex.logic.Statistics;
import com.safroalex.ui.windows.AutoWindow;
import com.safroalex.ui.windows.StatisticsWindow;
import com.safroalex.utils.SimulationParameters;
import com.safroalex.utils.SystemSimulationInstance;
import javafx.application.Application;


public class App {
    private static final int WINDOW_TYPE = 1; // 0 для StatisticsWindow или 1 для MainWindow

    public static void main(String[] args) {
        int totalSources = 4;     // Количество источников
        int totalDevices = 5;     // Количество приборов
        double minInterval = 2; // Минимальный интервал между заявками
        double maxInterval = 2.5;   // Максимальный интервал между заявками
        int totalSteps = 1000;       // Количество шагов моделирования
        int bufferCapacity = 500;   // Вместимость буффера

        Statistics mainStatistic = new Statistics();
        SystemSimulationInstance.initialize(mainStatistic,
                totalSources, totalDevices, minInterval,
                maxInterval, bufferCapacity);
        SimulationParameters.setTotalSteps(totalSteps, totalSources,
                totalDevices, minInterval, maxInterval, bufferCapacity);

        if (WINDOW_TYPE == 0) {
            Application.launch(StatisticsWindow.class, args);
        } else if (WINDOW_TYPE == 1) {
            Application.launch(AutoWindow.class, args);
        }
    }
}


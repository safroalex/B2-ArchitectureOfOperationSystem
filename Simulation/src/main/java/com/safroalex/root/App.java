package com.safroalex.root;

import com.safroalex.logic.Statistics;
import com.safroalex.ui.MainWindow;
import com.safroalex.ui.*;
import com.safroalex.utils.SimulationParameters;
import com.safroalex.utils.SystemSimulationInstance;
import javafx.application.Application;


public class App {
    private static final int WINDOW_TYPE = 0; // 0 для StatisticsWindow или 1 для MainWindow

    public static void main(String[] args) {
        int totalSources = 4;     // Количество источников
        int totalDevices = 3;     // Количество приборов
        double minInterval = 1.5; // Минимальный интервал между заявками
        double maxInterval = 2;   // Максимальный интервал между заявками
        int bufferCapacity = 5;   // Вместимость буффера
        int totalSteps = 6;       // Количество шагов моделирования

        Statistics mainStatistic = new Statistics();
        SystemSimulationInstance.initialize(mainStatistic,
                totalSources, totalDevices, minInterval,
                maxInterval, bufferCapacity);
        SimulationParameters.setTotalSteps(totalSteps, totalSources,
                totalDevices, minInterval, maxInterval, bufferCapacity);

        if (WINDOW_TYPE == 0) {
            Application.launch(StatisticsWindow.class, args);
        } else if (WINDOW_TYPE == 1) {
            Application.launch(MainWindow.class, args);
        }
    }
}


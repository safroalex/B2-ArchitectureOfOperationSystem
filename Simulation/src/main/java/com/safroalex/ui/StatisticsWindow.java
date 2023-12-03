package com.safroalex.ui;

import com.safroalex.logic.RejectionHandler;
import com.safroalex.logic.SystemSimulation;
import com.safroalex.utils.SimulationParameters;
import com.safroalex.utils.SystemSimulationInstance;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.scene.control.TextArea;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class StatisticsWindow extends Application {
    @Override
    public void start(Stage primaryStage) {
        SystemSimulation simulation = SystemSimulationInstance.getInstance();
        int totalSteps = SimulationParameters.getTotalSteps();
        int totalSources = SimulationParameters.getTotalSources();
        int totalDevices = SimulationParameters.getTotalDevices();
        double minInterval = SimulationParameters.getMinInterval();
        double maxInterval = SimulationParameters.getMaxInterval();
        int bufferCapacity = SimulationParameters.getBufferCapacity();


        VBox root = new VBox();
        root.setSpacing(10);

        // Добавляем информацию для каждой категории
        root.getChildren().add(createInfoBox("Общее количество источников: ", totalSources, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/sensor.png"));
        root.getChildren().add(createInfoBox("Общее количество приборов: ", totalDevices, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/device.png"));
        root.getChildren().add(createInfoBox("Общее количество шагов: ", totalSteps, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/time.png"));
        root.getChildren().add(createInfoBox("Минимальный интервал: ", minInterval, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/interval.png"));
        root.getChildren().add(createInfoBox("Максимальный интервал: ", maxInterval, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/interval.png"));
        root.getChildren().add(createInfoBox("Вместимость буфера: ", bufferCapacity, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/buffer.png"));


        // Создаем TextArea для вывода результатов
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(600, 400);

        // Заполняем TextArea информацией
        for (int i = 0; i < totalSteps; i++) {
            textArea.appendText("=====================================\n");
            //textArea.appendText("Шаг " + (i + 1) + "\n");
            simulation.step();
            textArea.appendText(simulation.getStatus() + "\n");
        }

        textArea.appendText("=====================================\n");
        textArea.appendText("Отклоненные заявки:\n");
        textArea.appendText(RejectionHandler.getRejectedRequests() + "\n");

        textArea.appendText(simulation.getStatistics() + "\n");

        // Создаем корневой контейнер и добавляем в него TextArea
        root.getChildren().add(textArea);
        Scene scene = new Scene(root, 700, 500);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Пошаговая Симуляции");
        primaryStage.show();
    }


    private HBox createInfoBox(String labelText, Object value, String imagePath) {
        HBox box = new HBox();
        box.setSpacing(5);
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        Label label = new Label(labelText + value.toString());
        box.getChildren().addAll(imageView, label);
        return box;
    }
}




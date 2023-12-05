package com.safroalex.ui;

import com.safroalex.logic.RejectionHandler;
import com.safroalex.logic.Statistics;
import com.safroalex.logic.SystemSimulation;
import com.safroalex.utils.SimulationParameters;
import com.safroalex.utils.SystemSimulationInstance;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import javafx.scene.control.TextArea;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.geometry.Pos;


public class StatisticsWindow extends Application {
    SystemSimulation simulation = SystemSimulationInstance.getInstance();
    int totalSteps = SimulationParameters.getTotalSteps();
    int totalSources = SimulationParameters.getTotalSources();
    int totalDevices = SimulationParameters.getTotalDevices();
    double minInterval = SimulationParameters.getMinInterval();
    double maxInterval = SimulationParameters.getMaxInterval();
    int bufferCapacity = SimulationParameters.getBufferCapacity();
    Statistics statistics = simulation.getStatisticObject();
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setSpacing(10);

        // Добавляем информацию для каждой категории
        root.getChildren().add(createInfoBox("Общее количество источников: ", totalSources, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/sensor.png"));
        root.getChildren().add(createInfoBox("Общее количество приборов: ", totalDevices, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/device.png"));
        root.getChildren().add(createInfoBox("Общее количество шагов: ", totalSteps, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/time.png"));
        root.getChildren().add(createInfoBox("Минимальный интервал: ", minInterval, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/interval.png"));
        root.getChildren().add(createInfoBox("Максимальный интервал: ", maxInterval, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/interval.png"));
        root.getChildren().add(createInfoBox("Вместимость буфера: ", bufferCapacity, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/buffer.png"));

        // TextArea для вывода результатов
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


        HBox sourceRectangles = createSourceRectangles(totalSources);
        root.getChildren().add(sourceRectangles);

        VBox deviceTree = createDeviceTree(totalDevices, statistics, totalSources);
        root.getChildren().add(deviceTree);

        StackPane rejectedRequestsDisplay = createRejectedRequestsDisplay();
        root.getChildren().add(rejectedRequestsDisplay);


        textArea.appendText("=====================================\n");
        textArea.appendText("Отклоненные заявки:\n");
        textArea.appendText(RejectionHandler.getRejectedRequests() + "\n");

        textArea.appendText(simulation.getStatistics() + "\n");

        root.getChildren().add(textArea);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true); // по ширине содержимого

        Scene scene = new Scene(scrollPane, 700, 500);

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

    private HBox createSourceRectangles(int totalSources) {
        HBox rectanglesBox = new HBox(10);
        rectanglesBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < totalSources; i++) {
            int requestsCount = (totalSources * totalSteps) / totalSources;

            // Создаем прямоугольник с градиентной заливкой
            Rectangle rectangle = new Rectangle(60, 40);
            LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.LIGHTGRAY), new Stop(1, Color.DARKGRAY));
            rectangle.setFill(gradient);

            // Создаем текст
            Text text = new Text(String.valueOf(requestsCount));
            text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            text.setFill(Color.WHITE);

            // Добавляем эффект тени
            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(5.0);
            dropShadow.setOffsetX(3.0);
            dropShadow.setOffsetY(3.0);
            dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
            rectangle.setEffect(dropShadow);

            // Группируем прямоугольник и текст
            StackPane stack = new StackPane();
            stack.getChildren().addAll(rectangle, text);

            // Добавляем анимацию при наведении
            stack.setOnMouseEntered(e -> rectangle.setFill(Color.DARKGRAY));
            stack.setOnMouseExited(e -> rectangle.setFill(gradient));

            // Добавляем группу в контейнер
            rectanglesBox.getChildren().add(stack);
        }

        return rectanglesBox;
    }


    private VBox createDeviceTree(int totalDevices, Statistics statistics, int totalSources) {
        int processed = 0;
        int totalProcessed = 0;
        for (int i = 0; i < totalSources; i++) {
            processed = statistics.getTotalRequestsGeneratedBySource(i) - statistics.getRequestsDeniedBySource(i);
            totalProcessed += processed;
        }

        // Создаем корневой узел - текст с общим количеством обработанных заявок
        Text rootText = new Text(String.valueOf(totalProcessed));
        rootText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        rootText.setFill(Color.FORESTGREEN); // Установка цвета текста

        // Создаем контейнер для прямоугольников
        HBox deviceBox = new HBox(10);
        deviceBox.setAlignment(Pos.CENTER);

        // Генерируем прямоугольники для устройств
        for (int i = 0; i < totalDevices; i++) {
            // Создаем прямоугольник с градиентной заливкой
            Rectangle rectangle = new Rectangle(60, 40);
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.SKYBLUE), new Stop(1, Color.DEEPSKYBLUE));
            rectangle.setFill(gradient);

            // Создаем текст
            Text text = new Text("Dev");
            text.setFill(Color.WHITE);

            // Группируем прямоугольник и текст
            StackPane stack = new StackPane();
            stack.getChildren().addAll(rectangle, text);

            // Добавление эффекта тени к прямоугольнику
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.GRAY);
            rectangle.setEffect(shadow);

            // Добавляем группу в контейнер прямоугольников
            deviceBox.getChildren().add(stack);
        }

        // Создаем VBox для корневого узла и контейнера прямоугольников
        VBox treeContainer = new VBox(10);
        treeContainer.setAlignment(Pos.TOP_CENTER);

        // Добавляем корневой узел и контейнер прямоугольников в VBox
        treeContainer.getChildren().addAll(rootText, deviceBox);

        return treeContainer;
    }


    private StackPane createRejectedRequestsDisplay() {
        // Создаем прямоугольник с градиентной заливкой
        Rectangle rectangle = new Rectangle(100, 50);
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.LIGHTCORAL), new Stop(1, Color.DARKRED));
        rectangle.setFill(gradient);

        // Получаем количество отклоненных заявок
        int rejectedRequestsCount = RejectionHandler.getRejectedRequests().size();

        // Создаем текст для отображения количества отклоненных заявок
        Text text = new Text(String.valueOf(rejectedRequestsCount));
        text.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        text.setFill(Color.WHITE); // Установка белого цвета текста

        // Добавление эффекта тени к прямоугольнику
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.GRAY);
        rectangle.setEffect(shadow);

        // Группируем прямоугольник и текст в StackPane
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, text);

        return stackPane;
    }


}




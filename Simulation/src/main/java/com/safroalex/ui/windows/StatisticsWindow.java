package com.safroalex.ui.windows;

import com.safroalex.logic.*;
import com.safroalex.utils.SimulationParameters;
import com.safroalex.utils.SystemSimulationInstance;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
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

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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
        //root.getChildren().add(createInfoBox("Общее количество шагов: ", totalSteps, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/time.png"));
        root.getChildren().add(createInfoBox("Минимальный интервал: ", minInterval, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/interval.png"));
        root.getChildren().add(createInfoBox("Максимальный интервал: ", maxInterval, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/interval.png"));
        root.getChildren().add(createInfoBox("Вместимость буфера: ", bufferCapacity, "file:/Users/anon/GITREPOS/PERSONAL/B2-SimulationModeling/Simulation/src/main/resources/buffer.png"));

        // TextArea для вывода результатов
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(600, 400);

        // Переменная для хранения общего количества заявок
        AtomicInteger totalRequestsCount = new AtomicInteger(0);

        // Создаем контейнер для прямоугольников источников
        VBox sourceRectanglesContainer = new VBox();
        HBox sourceRectangles = createSourceRectangles(totalSources, totalRequestsCount.get());
        sourceRectanglesContainer.getChildren().add(sourceRectangles);
        root.getChildren().add(sourceRectanglesContainer);

        StackPane bufferStatusDisplay = createBufferStatusDisplay(0);
        VBox bufferStatusContainer = new VBox(bufferStatusDisplay);
        root.getChildren().add(bufferStatusContainer);

        VBox deviceTreeContainer = new VBox();
        VBox deviceTree = createDeviceTree(statistics, totalSources);
        deviceTreeContainer.getChildren().add(deviceTree);
        root.getChildren().add(deviceTreeContainer);

        StackPane rejectedRequestsDisplay = createRejectedRequestsDisplay();
        VBox rejectedRequestsContainer = new VBox(rejectedRequestsDisplay);
        root.getChildren().add(rejectedRequestsContainer);



        // Создаем кнопку для перехода к следующему шагу
        Button nextStepButton = new Button("Next");
        nextStepButton.setOnAction(e -> {
            // Вызываем метод следующего шага симуляции
            simulation.step();

            // Увеличиваем количество заявок
            totalRequestsCount.incrementAndGet();

            // Пересоздаем прямоугольники с новым количеством заявок
            HBox updatedSourceRectangles = createSourceRectangles(totalSources, totalRequestsCount.get());
            sourceRectanglesContainer.getChildren().set(0, updatedSourceRectangles); // Обновляем содержимое контейнера

            // Обновляем дерево устройств с новыми данными
            VBox updatedDeviceTree = createDeviceTree(statistics, totalSources);
            deviceTreeContainer.getChildren().set(0, updatedDeviceTree);

            // Обновление отображения буфера
            StackPane updatedBufferStatusDisplay = createBufferStatusDisplay(
                    statistics.getRequestsBufferSize());
            bufferStatusContainer.getChildren().set(0, updatedBufferStatusDisplay);

            // Обновляем отображение отклоненных заявок
            StackPane updatedRejectedRequestsDisplay = createRejectedRequestsDisplay();
            rejectedRequestsContainer.getChildren().set(0, updatedRejectedRequestsDisplay);

            // Обновляем TextArea с новым состоянием симуляции
            textArea.appendText("=====================================\n");
            textArea.appendText(simulation.getAndSetStatus() + "\n");

            textArea.appendText("Отклоненные заявки:\n");
            textArea.appendText(RejectionHandler.getRejectedRequests() + "\n");

            textArea.appendText(String.valueOf(statistics.getBufferIsEmpty()));
            textArea.appendText(String.valueOf(statistics.getBufferIsFull()));
            textArea.appendText(String.valueOf(statistics.getRequestsBufferSize()));
        });

        root.getChildren().addAll(nextStepButton, textArea);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(root);
        scrollPane.setFitToWidth(true); // по ширине содержимого

        Scene scene = new Scene(scrollPane, 700, 500);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Пошаговая Симуляция");
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

    private HBox createSourceRectangles(int totalSources, int requestsPerSource) {
        HBox rectanglesBox = new HBox(10);
        rectanglesBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < totalSources; i++) {
            int requestsCount = requestsPerSource;

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


    private VBox createDeviceTree(Statistics statistics, int totalSources) {
        List<Device> devices = statistics.getDevicesList();
        int processed = 0;
        int totalProcessed = 0;
        for (int i = 0; i < totalSources; i++) {
            processed = statistics.getTotalRequestsGeneratedBySource(i) - statistics.getRequestsDeniedBySource(i);
            totalProcessed += processed;
        }

        // Создаем корневой узел - текст с общим количеством обработанных заявок
        Text rootText = new Text(String.valueOf(totalProcessed));
        rootText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        rootText.setFill(Color.FORESTGREEN);

        // Создаем контейнер для устройств
        HBox deviceBox = new HBox(10);
        deviceBox.setAlignment(Pos.CENTER);

        // Генерируем элементы для каждого устройства
        for (Device device : devices) {
            Request currentRequest = device.getCurrentRequest();
            String requestId = (currentRequest != null) ? String.valueOf(currentRequest.getRequestId()) : "None";
            String sourceId = (currentRequest != null) ? String.valueOf(currentRequest.getSourceId()) : "None";

            // Создаем прямоугольник с градиентной заливкой
            Rectangle rectangle = new Rectangle(100, 60);
            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.SKYBLUE), new Stop(1, Color.DEEPSKYBLUE));
            rectangle.setFill(gradient);

            // Текст с информацией о текущем запросе
            Text text = new Text("Dev " + device.getDeviceId() + "\nReq: " + requestId + "\nSrc: " + sourceId);
            text.setFill(Color.WHITE);
            text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

            // Группируем прямоугольник и текст
            StackPane stack = new StackPane();
            stack.getChildren().addAll(rectangle, text);

            // Добавление эффекта тени
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.GRAY);
            rectangle.setEffect(shadow);

            // Добавляем группу в контейнер
            deviceBox.getChildren().add(stack);
        }

        // Группируем корневой узел и контейнер устройств в вертикальную структуру
        VBox tree = new VBox(10, rootText, deviceBox);
        tree.setAlignment(Pos.CENTER);

        return tree;
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

    private StackPane createBufferStatusDisplay(int bufferSize) {
        LinkedList<Request> requests = statistics.getRequestsOfBuffer();

        Color bufferColor = Color.YELLOW;

        Rectangle rectangle = new Rectangle(300, 50);
        rectangle.setFill(bufferColor);

        // Создаем текст для отображения размера буфера
        Text bufferSizeText = new Text("Prev Buffer Size: " + bufferSize);
        bufferSizeText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        bufferSizeText.setFill(Color.BLACK);

        // Создание контейнера для ID заявок и источников в буфере
        VBox requestsContainer = new VBox(2); // Вертикальный бокс с отступом 2
        for (Request request : requests) {
            // ID источника
            Text sourceIdText = new Text("Source: " + request.getSourceId());
            sourceIdText.setFont(Font.font("Verdana", FontWeight.BOLD, 10));
            sourceIdText.setFill(Color.BLUE);

            // ID заявки
            Text requestIdText = new Text("Request: " + request.getRequestId());
            requestIdText.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            requestIdText.setFill(Color.BLACK);

            // Добавление источника и заявки в контейнер
            VBox requestInfo = new VBox(sourceIdText, requestIdText);
            requestsContainer.getChildren().add(requestInfo);
        }

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, requestsContainer, bufferSizeText);

        return stackPane;
    }
}




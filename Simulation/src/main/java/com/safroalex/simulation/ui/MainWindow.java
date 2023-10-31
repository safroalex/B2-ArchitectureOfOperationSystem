package com.safroalex.simulation.ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainWindow {
    public void start(Stage primaryStage) {
        // Создаем корневой узел с надписью
        StackPane root = new StackPane();
        Label label = new Label("Привет, JavaFX!");
        root.getChildren().add(label);

        // Создаем сцену
        Scene scene = new Scene(root, 300, 200);

        // Устанавливаем сцену на подмостках
        primaryStage.setScene(scene);

        // Устанавливаем заголовок окна
        primaryStage.setTitle("Мое JavaFX Приложение");

        // Показываем окно
        primaryStage.show();
    }

}

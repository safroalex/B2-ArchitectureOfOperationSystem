package com.safroalex.simulation.root;

import javafx.application.Application;
import javafx.stage.Stage;
import com.safroalex.simulation.ui.MainWindow;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) {

        new MainWindow().start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

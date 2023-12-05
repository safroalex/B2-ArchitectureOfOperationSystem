package com.safroalex.ui;

import com.safroalex.logic.Statistics;
import com.safroalex.logic.SystemSimulation;
import com.safroalex.utils.SimulationParameters;
import com.safroalex.utils.SystemSimulationInstance;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class MainWindow extends Application {
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


        // Создаем таблицу
        TableView<SourceData> table = new TableView<>();

        // Определяем столбцы
        TableColumn<SourceData, Number> sourceNumberCol = new TableColumn<>("№ источника");
        sourceNumberCol.setCellValueFactory(new PropertyValueFactory<>("sourceNumber"));

        TableColumn<SourceData, Number> requestCountCol = new TableColumn<>("Количество заявок");
        requestCountCol.setCellValueFactory(new PropertyValueFactory<>("requestCount"));

        TableColumn<SourceData, Number> rejectedCountCol = new TableColumn<>("Отклоненные заявки");
        rejectedCountCol.setCellValueFactory(new PropertyValueFactory<>("rejectedCount"));

        TableColumn<SourceData, Double> avgStayTimeCol = new TableColumn<>("Ср. время преб.");
        avgStayTimeCol.setCellValueFactory(new PropertyValueFactory<>("avgStayTime"));

        TableColumn<SourceData, Double> avgBufferTimeCol = new TableColumn<>("Ср. время в БП");
        avgBufferTimeCol.setCellValueFactory(new PropertyValueFactory<>("avgBufferTime"));

        TableColumn<SourceData, Double> avgServiceTimeCol = new TableColumn<>("Ср. время обсл.");
        avgServiceTimeCol.setCellValueFactory(new PropertyValueFactory<>("avgServiceTime"));

        TableColumn<SourceData, Double> varianceServiceTimeCol = new TableColumn<>("Дисп. Tобсл.");
        varianceServiceTimeCol.setCellValueFactory(new PropertyValueFactory<>("varianceServiceTime"));

        TableColumn<SourceData, Double> varianceBufferTimeCol = new TableColumn<>("Дисп. TБП");
        varianceBufferTimeCol.setCellValueFactory(new PropertyValueFactory<>("varianceBufferTime"));

        // Добавляем столбцы в таблицу
        table.getColumns().addAll(sourceNumberCol, requestCountCol, rejectedCountCol, avgStayTimeCol, avgBufferTimeCol, avgServiceTimeCol, varianceServiceTimeCol, varianceBufferTimeCol);

        ObservableList<SourceData> data = FXCollections.observableArrayList();
        for (int i = 0; i < totalSources; i++) {
            data.add(new SourceData(i));
        }
        table.setItems(data);



        // Создаем вторую таблицу для характеристик приборов
        TableView<DeviceData> deviceTable = new TableView<>();

        TableColumn<DeviceData, Number> deviceNumberCol = new TableColumn<>("№ прибора");
        deviceNumberCol.setCellValueFactory(new PropertyValueFactory<>("deviceNumber"));

        TableColumn<DeviceData, Number> utilizationCol = new TableColumn<>("Коэффициент использования");
        utilizationCol.setCellValueFactory(new PropertyValueFactory<>("utilizationCoefficient"));

        deviceTable.getColumns().addAll(deviceNumberCol, utilizationCol);

        // Располагаем обе таблицы в VBox
        VBox vbox = new VBox(table, deviceTable);

        // Создаем сцену с vbox
        Scene scene = new Scene(vbox, 600, 600); // Можно увеличить высоту, чтобы вместить обе таблицы

        // Устанавливаем сцену и показываем окно
        primaryStage.setScene(scene);
        primaryStage.setTitle("Статистика Источников и Приборов ВС");
        primaryStage.show();
    }


    // Класс для данных, отображаемых в таблице
    public class SourceData {
        private final SimpleIntegerProperty sourceNumber;

        public SourceData(int sourceNumber) {
            this.sourceNumber = new SimpleIntegerProperty(sourceNumber);
        }





        public int getSourceNumber() {
            return sourceNumber.get();
        }

        public void setSourceNumber(int value) {
            sourceNumber.set(value);
        }
    }

    // Класс для данных приборов
    public static class DeviceData {
        private final SimpleIntegerProperty deviceNumber;
        private final SimpleDoubleProperty utilizationCoefficient;

        public DeviceData(int deviceNumber, double utilizationCoefficient) {
            this.deviceNumber = new SimpleIntegerProperty(deviceNumber);
            this.utilizationCoefficient = new SimpleDoubleProperty(utilizationCoefficient);
        }

        // Геттеры и сеттеры
        public int getDeviceNumber() {
            return deviceNumber.get();
        }

        public void setDeviceNumber(int value) {
            deviceNumber.set(value);
        }

        public double getUtilizationCoefficient() {
            return utilizationCoefficient.get();
        }

        public void setUtilizationCoefficient(double value) {
            utilizationCoefficient.set(value);
        }
    }
}

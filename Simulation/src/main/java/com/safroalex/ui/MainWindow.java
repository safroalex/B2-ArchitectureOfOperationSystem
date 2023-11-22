package com.safroalex.ui;

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

        // Создаем и добавляем данные в таблицу
        ObservableList<SourceData> data = FXCollections.observableArrayList(
                new SourceData(1, 5, 3, 1.0, 0.5, 2.0, 0.1, 0.2)
                //  здесь остальные данные
        );
        table.setItems(data);




        // Создаем вторую таблицу для характеристик приборов
        TableView<DeviceData> deviceTable = new TableView<>();

        TableColumn<DeviceData, Number> deviceNumberCol = new TableColumn<>("№ прибора");
        deviceNumberCol.setCellValueFactory(new PropertyValueFactory<>("deviceNumber"));

        TableColumn<DeviceData, Number> utilizationCol = new TableColumn<>("Коэффициент использования");
        utilizationCol.setCellValueFactory(new PropertyValueFactory<>("utilizationCoefficient"));

        deviceTable.getColumns().addAll(deviceNumberCol, utilizationCol);

        // Добавляем данные во вторую таблицу
        ObservableList<DeviceData> deviceData = FXCollections.observableArrayList(
                new DeviceData(1, 0.75)
                //  здесь остальные данные
        );
        deviceTable.setItems(deviceData);



        // Располагаем обе таблицы в VBox
        VBox vbox = new VBox(table, deviceTable);

        // Создаем сцену с vbox
        Scene scene = new Scene(vbox, 600, 600); // Можно увеличить высоту, чтобы вместить обе таблицы

        // Устанавливаем сцену и показываем окно
        primaryStage.setScene(scene);
        primaryStage.setTitle("Статистика Источников и Приборов ВС");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Класс для данных, отображаемых в таблице
    public static class SourceData {
        private final SimpleIntegerProperty sourceNumber;
        private final SimpleIntegerProperty requestCount;
        private final SimpleIntegerProperty rejectedCount;
        private final SimpleDoubleProperty avgStayTime;
        private final SimpleDoubleProperty avgBufferTime;
        private final SimpleDoubleProperty avgServiceTime;
        private final SimpleDoubleProperty varianceServiceTime;
        private final SimpleDoubleProperty varianceBufferTime;

        public SourceData(int sourceNumber, int requestCount, int rejectedCount,
                          double avgStayTime, double avgBufferTime, double avgServiceTime,
                          double varianceServiceTime, double varianceBufferTime) {
            this.sourceNumber = new SimpleIntegerProperty(sourceNumber);
            this.requestCount = new SimpleIntegerProperty(requestCount);
            this.rejectedCount = new SimpleIntegerProperty(rejectedCount);
            this.avgStayTime = new SimpleDoubleProperty(avgStayTime);
            this.avgBufferTime = new SimpleDoubleProperty(avgBufferTime);
            this.avgServiceTime = new SimpleDoubleProperty(avgServiceTime);
            this.varianceServiceTime = new SimpleDoubleProperty(varianceServiceTime);
            this.varianceBufferTime = new SimpleDoubleProperty(varianceBufferTime);

        }

        public int getSourceNumber() {
            return sourceNumber.get();
        }

        public void setSourceNumber(int value) {
            sourceNumber.set(value);
        }

        public int getRequestCount() {
            return requestCount.get();
        }

        public void setRequestCount(int value) {
            requestCount.set(value);
        }

        public int getRejectedCount() {
            return rejectedCount.get();
        }

        public void setRejectedCount(int value) {
            rejectedCount.set(value);
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

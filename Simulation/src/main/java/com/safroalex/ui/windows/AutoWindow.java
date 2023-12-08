package com.safroalex.ui.windows;

import com.safroalex.logic.Statistics;
import com.safroalex.logic.SystemSimulation;
import com.safroalex.ui.autoWindow.DeviceData;
import com.safroalex.ui.autoWindow.SourceData;
import com.safroalex.utils.SimulationParameters;
import com.safroalex.utils.SystemSimulationInstance;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class AutoWindow extends Application {
    final SystemSimulation simulation = SystemSimulationInstance.getInstance();
    final Statistics statistics = simulation.getStatisticObject();
    final int totalSteps = SimulationParameters.getTotalSteps();
    final int totalSources = SimulationParameters.getTotalSources();
    final int totalDevices = SimulationParameters.getTotalDevices();
    final double minInterval = SimulationParameters.getMinInterval();
    final double maxInterval = SimulationParameters.getMaxInterval();
    final int bufferCapacity = SimulationParameters.getBufferCapacity();

    @Override
    public void start(Stage primaryStage) {
        for (int i = 0; i < totalSteps; i++) {
            simulation.step();
        }

        System.out.println(statistics.toString());
        System.out.println(simulation.getAndSetStatus());
        System.out.println(simulation.getStatistics());
        System.out.println(statistics.getTotalServiceTimeBySource(3));


        TableView<SourceData> table = new TableView<>();

        TableColumn<SourceData, Number> sourceNumberCol = new TableColumn<>("№ источника");
        sourceNumberCol.setCellValueFactory(new PropertyValueFactory<>("sourceNumber"));

        TableColumn<SourceData, Number> requestCountCol = new TableColumn<>("Количество заявок");
        requestCountCol.setCellValueFactory(new PropertyValueFactory<>("requestCount"));

        TableColumn<SourceData, Number> denialProbabilityCol = new TableColumn<>("Вероятность отказа");
        denialProbabilityCol.setCellValueFactory(new PropertyValueFactory<>("denialProbability"));

        TableColumn<SourceData, Double> avgServiceTimeCol = new TableColumn<>("Ср. время обсл.");
        avgServiceTimeCol.setCellValueFactory(new PropertyValueFactory<>("totalServiceTimeBySource"));

        // Добавляем столбцы в таблицу
        table.getColumns().addAll(sourceNumberCol, requestCountCol, denialProbabilityCol, avgServiceTimeCol);

        // Создание и установка данных для таблицы
        ObservableList<SourceData> sourceData = SourceData.createSourceDataList(totalSources, statistics);
        table.setItems(sourceData);



        // Создаем вторую таблицу для характеристик приборов
        TableView<DeviceData> deviceTable = new TableView<>();

        TableColumn<DeviceData, Number> deviceNumberCol = new TableColumn<>("№ прибора");
        deviceNumberCol.setCellValueFactory(new PropertyValueFactory<>("deviceNumber"));

        TableColumn<DeviceData, Number> utilizationCol = new TableColumn<>("Коэффициент использования");
        utilizationCol.setCellValueFactory(new PropertyValueFactory<>("utilizationCoefficient"));

        deviceTable.getColumns().addAll(deviceNumberCol, utilizationCol);
        
        ObservableList<DeviceData> deviceData = DeviceData.createDeviceDataList(totalDevices, statistics);
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
}



//

//
//        TableColumn<SourceData, Double> avgStayTimeCol = new TableColumn<>("Ср. время преб.");
//        avgStayTimeCol.setCellValueFactory(new PropertyValueFactory<>("avgStayTime"));
//
//        TableColumn<SourceData, Double> avgBufferTimeCol = new TableColumn<>("Ср. время в БП");
//        avgBufferTimeCol.setCellValueFactory(new PropertyValueFactory<>("avgBufferTime"));
//

//
//        TableColumn<SourceData, Double> varianceServiceTimeCol = new TableColumn<>("Дисп. Tобсл.");
//        varianceServiceTimeCol.setCellValueFactory(new PropertyValueFactory<>("varianceServiceTime"));
//
//        TableColumn<SourceData, Double> varianceBufferTimeCol = new TableColumn<>("Дисп. TБП");
//        varianceBufferTimeCol.setCellValueFactory(new PropertyValueFactory<>("varianceBufferTime"));




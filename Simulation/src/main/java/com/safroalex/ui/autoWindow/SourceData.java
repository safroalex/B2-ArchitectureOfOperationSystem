package com.safroalex.ui.autoWindow; // Убедитесь, что используете правильный пакет

import com.safroalex.logic.Statistics;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SourceData {
    private final SimpleIntegerProperty sourceNumber;
    private final SimpleDoubleProperty denialProbability;
    private final SimpleIntegerProperty requestCount;
    private final SimpleDoubleProperty totalServiceTimeBySource;
    public SourceData(int sourceNumber, double denialProbability, int requestCount, double totalServiceTimeBySource) {
        this.sourceNumber = new SimpleIntegerProperty(sourceNumber);
        this.denialProbability = new SimpleDoubleProperty(denialProbability);
        this.requestCount = new SimpleIntegerProperty(requestCount);
        this.totalServiceTimeBySource = new SimpleDoubleProperty(totalServiceTimeBySource);
    }

    public static ObservableList<SourceData> createSourceDataList(int totalSources, Statistics statistics) {
        ObservableList<SourceData> data = FXCollections.observableArrayList();
        for (int i = 0; i < totalSources; i++) {
            double denialProbability = statistics.getRatioOfDenials(i);
            int requestCount = statistics.getTotalRequestsGeneratedBySource(i);
            double totalServiceTimeBySource = statistics.getTotalServiceTimeBySource(i);
            data.add(new SourceData(i, denialProbability, requestCount, totalServiceTimeBySource));
        }
        return data;
    }

    public int getSourceNumber() {
        return sourceNumber.get();
    }

    public double getDenialProbability() {
        return denialProbability.get();
    }

    public int getRequestCount() {
        return requestCount.get();
    }

    public double getTotalServiceTimeBySource() {
        return totalServiceTimeBySource.get();
    }
}

package com.safroalex.ui.autoWindow;

import com.safroalex.logic.Statistics;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DeviceData {
    private final SimpleIntegerProperty deviceNumber;
    private final SimpleDoubleProperty utilizationCoefficient;

    public DeviceData(int deviceNumber, double utilizationCoefficient) {
        this.deviceNumber = new SimpleIntegerProperty(deviceNumber);
        this.utilizationCoefficient = new SimpleDoubleProperty(utilizationCoefficient);
    }

    public static ObservableList<DeviceData> createDeviceDataList(int totalDevices, Statistics statistics, int totalSteps) {
        ObservableList<DeviceData> data = FXCollections.observableArrayList();
        for (int i = 0; i < totalDevices; i++) {
            data.add(new DeviceData(i,statistics.getDeviceUtilizationCoefficient(i, totalSteps)));
        }
        return data;
    }

    public int getDeviceNumber() {
        return deviceNumber.get();
    }

    public double getUtilizationCoefficient() {
        return utilizationCoefficient.get();
    }
}

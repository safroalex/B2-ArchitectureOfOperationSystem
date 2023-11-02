package com.safroalex.logic;

import java.util.ArrayList;
import java.util.List;

public class SystemSimulation {

    private List<Source> sources;
    private List<Device> devices;
    private double currentModelTime;

    // Конструктор
    public SystemSimulation(int totalSources, int totalDevices, double minInterval, double maxInterval) {
        this.sources = new ArrayList<>();
        this.devices = new ArrayList<>();
        this.currentModelTime = 0;

        for (int i = 0; i < totalSources; i++) {
            sources.add(new Source(i, minInterval, maxInterval));
        }

        for (int i = 0; i < totalDevices; i++) {
            devices.add(new Device(i));
        }
    }

    // Получение текущего количества источников
    public int getSourceCount() {
        return sources.size();
    }

    // Поиск свободного прибора по кольцевой структуре.
    private Device findFreeDevice() {
        for (Device device : devices) {
            if (!device.isBusy()) {
                return device;
            }
        }
        return null;
    }

    // Шаг моделирования
    public void step() {
        currentModelTime += 1;
        System.out.println("Текущее время модели: " + currentModelTime);

        for (Device device : devices) {
            if (device.isBusy() && currentModelTime >= device.getTimeWhenWillBeFree()) {
                device.finishProcessing(currentModelTime, sources);
            }
        }

        for (Source source : sources) {
            Request request = source.generateRequest();
            if (request != null) {
                System.out.println("Заявка сгенерирована: " + request);
                processRequest(request);
            }
        }
    }

    // Обработка заявки
    private void processRequest(Request request) {
        Device freeDevice = findFreeDevice();
        if (freeDevice != null) {
            freeDevice.takeRequest(request, currentModelTime);
            System.out.println("Заявка обработана прибором: " + freeDevice); // Нужно реализовать метод `toString()` для Device
        } else {
            // TODO: Реализовать логику для заявок, которые не могут быть обработаны из-за отсутствия свободных приборов
        }
    }
}

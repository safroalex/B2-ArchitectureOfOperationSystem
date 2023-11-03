package com.safroalex.logic;

import java.util.ArrayList;
import java.util.List;

public class SystemSimulation {

    private List<Source> sources;
    private List<Device> devices;
    private Buffer buffer;
    private double currentModelTime;
    private Statistics statistics;

    public SystemSimulation(int totalSources, int totalDevices, double minInterval, double maxInterval, int bufferCapacity) {
        this.sources = new ArrayList<>();
        this.devices = new ArrayList<>();
        this.buffer = new Buffer(bufferCapacity);
        this.currentModelTime = 0;
        this.statistics = new Statistics();

        for (int i = 0; i < totalSources; i++) {
            sources.add(new Source(i, minInterval, maxInterval));
        }

        for (int i = 0; i < totalDevices; i++) {
            devices.add(new Device(i));
        }
    }

    private Device findFreeDevice() {
        for (Device device : devices) {
            if (!device.isBusy()) {
                return device;
            }
        }
        return null;
    }

    public void step() {
        currentModelTime += 1;
        //System.out.println("Текущее время модели: " + currentModelTime);

        for (Device device : devices) {
            if (device.isBusy() && currentModelTime >= device.getTimeWhenWillBeFree()) {
                device.finishProcessing(currentModelTime, sources);

                // Обработка заявок из буфера, если прибор свободен и в буфере есть заявки.
                Request bufferedRequest = buffer.getRequest();
                if (bufferedRequest != null) {
                    device.takeRequest(bufferedRequest, currentModelTime);
                    System.out.println("Заявка из буфера обработана прибором: " + device);
                }
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

    private void processRequest(Request request) {
        Device freeDevice = findFreeDevice();
        if (freeDevice != null) {
            // считаем статистику для времени обслуживания
            double serviceTime = freeDevice.getServiceTimeForRequest(request);
            statistics.addServiceTime(request.getSourceId(), serviceTime);
            statistics.addServiceTimeSquared(request.getSourceId(), Math.pow(serviceTime, 2));

            freeDevice.takeRequest(request, currentModelTime);
            System.out.println("Заявка обработывается прибором: " + freeDevice);
        } else {
            // Если все приборы заняты, попытка добавить заявку в буфер.
            boolean addedToBuffer = buffer.addToBuffer_D1OZ2(request, currentModelTime);
            if (addedToBuffer) {
                // если заявка все еще в буфере, считаем статистику для времени ожидания
                if (buffer.requests.contains(request)) { // проверяем, что заявка все еще в буфере
                    double waitTime = buffer.getWaitTimeForRequest(request, currentModelTime);
                    statistics.addWaitTime(request.getSourceId(), waitTime);
                    statistics.addWaitTimeSquared(request.getSourceId(), Math.pow(waitTime, 2));
                }

                System.out.println("Заявка добавлена в буфер: " + request);
            } else {
                System.out.println("Заявка отклонена: " + request);
            }
        }
    }

    public String getStatus() {
        StringBuilder status = new StringBuilder();

        status.append("Текущее время модели: ").append(currentModelTime).append("\n");

        status.append("Источники:\n");
        for (Source source : sources) {
            status.append(source.toString()).append("\n"); // Предполагается, что у класса Source есть переопределенный метод toString()
        }

        status.append("Приборы:\n");
        for (Device device : devices) {
            status.append(device.toString()).append("\n"); // Предполагается, что у класса Device есть переопределенный метод toString()
        }

        status.append("Буфер:\n");
        status.append(buffer.toString()); // Предполагается, что у класса Buffer есть переопределенный метод toString()

        return status.toString();
    }

    public String getStatistics() {
        StringBuilder statistics = new StringBuilder();

        statistics.append("\nСтатистика:\n");
        statistics.append(statistics.toString()); // Предполагается, что у класса Statistics есть переопределенный метод toString()

        return statistics.toString();
    }


}

package com.safroalex.logic;

import java.util.ArrayList;
import java.util.List;

public class SystemSimulation {

    private List<Source> sources;
    private List<Device> devices;
    private Buffer buffer;
    private double currentModelTime;
    private Statistics statistics;

    public SystemSimulation(Statistics statistics, int totalSources, int totalDevices, double minInterval, double maxInterval, int bufferCapacity) {
        this.sources = new ArrayList<>();
        this.devices = new ArrayList<>();
        this.buffer = new Buffer(statistics ,bufferCapacity);
        this.currentModelTime = 0;
        this.statistics = statistics;

        for (int i = 0; i < totalSources; i++) {
            sources.add(new Source(statistics ,i, minInterval, maxInterval));
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
                    System.out.println("zayavka-iz-bufera-obrabotana-priborom: " + device);
                }
            }
        }

        for (Source source : sources) {
            Request request = source.generateRequest();
            if (request != null) {
                System.out.println("zayavka-sgenerirovana: " + request);
                processRequest(request);
            }
        }
    }

    private void processRequest(Request request) {
        Device freeDevice = findFreeDevice();
        if (freeDevice != null) {
            // считаем статистику для времени обслуживания
            double serviceTime = freeDevice.getServiceTimeForRequest(request);
            //statistics.addServiceTime(request.getSourceId(), serviceTime);
            //statistics.addServiceTimeSquared(request.getSourceId(), Math.pow(serviceTime, 2));

            freeDevice.takeRequest(request, currentModelTime);
            System.out.println("zayavka-obrabotyvaetsya-priborom: " + freeDevice);
        } else {
            // Если все приборы заняты, попытка добавить заявку в буфер.
            boolean addedToBuffer = buffer.addToBuffer_D1OZ2(request, currentModelTime);
            if (addedToBuffer) {
                // если заявка все еще в буфере, считаем статистику для времени ожидания
                if (buffer.requests.contains(request)) { // проверяем, что заявка все еще в буфере
                    double waitTime = buffer.getWaitTimeForRequest(request, currentModelTime);
                   // statistics.addWaitTime(request.getSourceId(), waitTime);
                    //statistics.addWaitTimeSquared(request.getSourceId(), Math.pow(waitTime, 2));
                }

                System.out.println("zayavka-dobavlena-v-bufer: " + request);
            } else {
                System.out.println("zayavka-otklonena: " + request);
            }
        }
    }

    public String getStatus() {
        StringBuilder status = new StringBuilder();

        status.append("tekushchee-vremya-modeli: ").append(currentModelTime).append("\n");

        status.append("istochniki:\n");
        for (Source source : sources) {
            statistics.addServiceTime(source.getSourceId() ,source.getAverageServiceTime());
            status.append(source.toString()).append("\n");
        }

        status.append("pribory:\n");
        for (Device device : devices) {
            status.append(device.toString()).append("\n");
        }

        status.append("bufer:\n");
        status.append(buffer.toString());

        return status.toString();
    }

    public String getStatistics() {
        StringBuilder statistics = new StringBuilder();

        statistics.append(this.statistics.toString());

        return statistics.toString();
    }


}

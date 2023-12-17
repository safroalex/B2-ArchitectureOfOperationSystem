package com.safroalex.logic;

import java.util.ArrayList;
import java.util.List;

public class SystemSimulation {

    private final List<Source> sources;
    private final List<Device> devices;
    private final Buffer buffer;
    private double currentModelTime;
    private final Statistics statistics;
    private int lastUsedDeviceIndex = -1;

    public SystemSimulation(Statistics statistics, int totalSources, int totalDevices,
                            double minInterval, double maxInterval, int bufferCapacity) {
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

    public Statistics getStatisticObject() {
        return this.statistics;
    }

    private Device findFreeDevice() {
        int devicesCount = devices.size();
        int startIndex = (lastUsedDeviceIndex + 1) % devicesCount;
        for (int i = 0; i < devicesCount; i++) {
            int currentIndex = (startIndex + i) % devicesCount;
            Device device = devices.get(currentIndex);
            if (!device.isBusy()) {
                lastUsedDeviceIndex = currentIndex; // Обновление индекса последнего использованного прибора
                return device;
            }
        }
        return null;
    }

    public void step() {
        currentModelTime += 1;

        for (Device device : devices) {
            if (device.isBusy() && currentModelTime >= device.getTimeWhenWillBeFree()) {
                device.finishProcessing(currentModelTime, sources);

                // Обработка заявок из буфера, если прибор свободен и в буфере есть заявки.
                Request bufferedRequest = buffer.getRequest(currentModelTime);
                if (bufferedRequest != null) {
                    device.takeRequest(bufferedRequest, currentModelTime);
                    System.out.println("zayavka-iz-bufera-obrabotana-priborom: " + device);
                }
            }
        }

        for (Source source : sources) {
            Request request = source.generateRequest(currentModelTime);
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

            freeDevice.takeRequest(request, currentModelTime);
            request.setWaitTime(currentModelTime - request.getCreationTimeForTable());
            statistics.addWaitTime(request.getSourceId(), request.getWaitTime());
            System.out.println("zayavka-obrabotyvaetsya-priborom: " + freeDevice);
        } else {
            // Если все приборы заняты, попытка добавить заявку в буфер.
            boolean addedToBuffer = buffer.addToBuffer_D1OZ2(request, currentModelTime);
            if (addedToBuffer) {
                // если заявка все еще в буфере, считаем статистику для времени ожидания
                if (buffer.requests.contains(request)) { // проверяем, что заявка все еще в буфере
                    double waitTime = buffer.getWaitTimeForRequest(request, currentModelTime);
                }

                System.out.println("zayavka-dobavlena-v-bufer: " + request);
            } else {
                System.out.println("zayavka-otklonena: " + request);
            }
        }
    }

    public String getAndSetStatus() {
        statistics.setRequestsBufferSize(buffer.getRequestsCount());
        statistics.setBufferIsEmpty(buffer.isEmpty());
        statistics.setBufferIsFull(buffer.isFull());
        statistics.setListOfRequestOfBuffer(buffer.getRequests());
        statistics.setListOfDevices(devices);
        for (Device device : devices) {
        statistics.setDeviceUsageTime(device.getDeviceId(), device.getTotalTimeWorked());
        }

        StringBuilder status = new StringBuilder();

        status.append("TIME: ").append(currentModelTime).append("\n");

        status.append("SOURCES:\n");
        for (Source source : sources) {
            statistics.addServiceTime(source.getSourceId() ,source.getAverageServiceTime());

            status.append(source.toString()).append("\n");
        }

        status.append("DEVICES:\n");
        for (Device device : devices) {
            status.append(device.toString()).append("\n");
        }

        status.append("BUFFER:\n");
        status.append(buffer.toString());


        return status.toString();
    }

    public String getStatistics() {
        StringBuilder statistic = new StringBuilder();

        statistic.append(this.statistics.toString());

        return statistic.toString();
    }


}

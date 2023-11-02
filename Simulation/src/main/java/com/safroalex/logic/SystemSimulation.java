package com.safroalex.logic;

import java.util.ArrayList;
import java.util.List;

public class SystemSimulation {

    private List<Source> sources;
    private List<Device> devices;
    private Buffer buffer;
    private double currentModelTime;

    public SystemSimulation(int totalSources, int totalDevices, double minInterval, double maxInterval, int bufferCapacity) {
        this.sources = new ArrayList<>();
        this.devices = new ArrayList<>();
        this.buffer = new Buffer(bufferCapacity);
        this.currentModelTime = 0;

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
        System.out.println("Текущее время модели: " + currentModelTime);

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
            freeDevice.takeRequest(request, currentModelTime);
            System.out.println("Заявка обработана прибором: " + freeDevice);
        } else {
            // Если все приборы заняты, попытка добавить заявку в буфер.
            boolean addedToBuffer = buffer.addToBuffer_D1OZ2(request);
            if (addedToBuffer) {
                System.out.println("Заявка добавлена в буфер: " + request);
            } else {
                System.out.println("Заявка отклонена: " + request);
            }
        }
    }
}

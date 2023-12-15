package com.safroalex.logic;

import java.util.List;

public class Device {

    private boolean isBusy; // Показывает, занят ли прибор обработкой
    private Request currentRequest; // Текущая обрабатываемая заявка
    private double timeWhenWillBeFree; // Время, когда прибор освободится
    private final int deviceId;
    private double totalTimeWorked; // Общее время работы прибора
    private double currentServiceTime; // Время обслуживания текущей заявки

    public Device(int deviceId) {
        this.deviceId = deviceId;
        this.isBusy = false;
        this.totalTimeWorked = 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("Device ID: ").append(deviceId).append(", ");

        if (isBusy) {
            builder.append("status-zanyat, ");
            builder.append("osvoboditsya-v: ").append(timeWhenWillBeFree).append(", ");
            builder.append("tekushchaya-zayavka: ").append(currentRequest);
        } else {
            builder.append("status-svoboden");
        }

        builder.append(", vremya-raboty: ").append(totalTimeWorked);

        return builder.toString();
    }


    // Проверка, занят ли прибор
    public boolean isBusy() {
        return isBusy;
    }

    // Принять заявку на обработку
    public void takeRequest(Request request, double currentTime) {
        if (isBusy) {
            throw new IllegalStateException("pribor-uzhe-zanyat-obrabotkoj.");
        }
        this.currentServiceTime = getExponentialServiceTime(); // Устанавливаем время обслуживания заявки
        this.currentRequest = request;
        this.isBusy = true;
        this.timeWhenWillBeFree = currentTime + currentServiceTime; // Используем currentServiceTime вместо getExponentialServiceTime()
        System.out.println("pribor ID: " + deviceId + " nachal-obrabatyvat-zayavku: " + request.toString());
    }

    // Завершить обработку текущей заявки
    public Request finishProcessing(double currentTime, List<Source> sources) {
        if (!isBusy) {
            throw new IllegalStateException("pribor-ne-obrabatyvaet-zayavku.");
        }
        if (currentTime < timeWhenWillBeFree) {
            throw new IllegalArgumentException("obrabotka-zayavki-eshche-ne-zavershena.");
        }
        this.totalTimeWorked += currentTime;
        this.isBusy = false;

        // Уведомляем источник о завершении обработки
        Source source = findSourceById(currentRequest.getSourceId(), sources);
        if (source != null) {
            source.informRequestCompletion(currentRequest, timeWhenWillBeFree);
        }
        System.out.println("pribor ID: " + deviceId + " zavershil-obrabotku-zayavki: " + currentRequest.toString());

        return currentRequest;
    }

    private Source findSourceById(int id, List<Source> sources) {
        for (Source source : sources) {
            if (source.getSourceId() == id) {
                return source;
            }
        }
        return null;
    }

    // Экспоненциальное распределение для времени обработки
    private double getExponentialServiceTime() {
        // Предположим среднее время обслуживания равно 1.0
        double lambda = 1.0;
        return (-Math.log(1.0 - Math.random()) / lambda);
    }

    public double getTimeWhenWillBeFree() {
        return timeWhenWillBeFree;
    }

    public double getServiceTimeForRequest(Request request) {
        if (currentRequest != null && currentRequest.equals(request)) {
            return currentServiceTime;
        }
        return 0; // или вернуть другое значение или исключение, если заявка не обрабатывается в данный момент
    }

    public int getDeviceId() {
        return deviceId;
    }

    public double getTotalTimeWorked() {
        return totalTimeWorked;
    }

    public Request getCurrentRequest() {
        return currentRequest;
    }
}

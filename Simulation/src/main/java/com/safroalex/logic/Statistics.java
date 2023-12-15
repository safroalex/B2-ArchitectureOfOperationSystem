package com.safroalex.logic;

import java.util.*;

public class Statistics {
    private final Random random = new Random();
    protected List<Device> devices = new LinkedList<>();
    protected LinkedList<Request> requestsOfBuffer = new LinkedList<>(); // Список заявок в буфере
    private Map<Integer, Integer> requestsGeneratedBySource = new HashMap<>(); // источник -> количество заявок
    private Map<Integer, Integer> requestsDeniedBySource = new HashMap<>();   // источник -> количество отказов
    private Map<Integer, Double> ratioOfDenials = new HashMap<>();
    private Map<Integer, Double> totalServiceTimeBySource = new HashMap<>();  // источник -> суммарное время обслуживания
    private Map<Integer, Double> totalWaitTimeBySource = new HashMap<>();     // источник -> суммарное время ожидания
    private Map<Integer, Double> totalServiceTimeSquaredBySource = new HashMap<>();  // источник -> сумма квадратов времени обслуживания
    private Map<Integer, Double> totalWaitTimeSquaredBySource = new HashMap<>();     // источник -> сумма квадратов времени ожидания
    private Map<Integer, Double> deviceUsageTime = new HashMap<>();           // прибор -> суммарное время занятости

    private int requestsBufferSize;
    private boolean statusBufferIsEmpty;
    private boolean statusBufferIsFull;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Statistics:\n");

        sb.append("Requests generated by source:\n");
        for (Map.Entry<Integer, Integer> entry : requestsGeneratedBySource.entrySet()) {
            sb.append("Source ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sb.append("Requests denied by source:\n");
        for (Map.Entry<Integer, Integer> entry : requestsDeniedBySource.entrySet()) {
            sb.append("Source ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sb.append("Ratio of requests denied by source:\n");
        for (Map.Entry<Integer, Integer> entry : requestsDeniedBySource.entrySet()) {
            Integer source = entry.getKey();
            Integer denials = entry.getValue();
            Integer totalRequests = requestsGeneratedBySource.get(source) + denials;
            Double ratio = (double) denials / totalRequests;

            ratioOfDenials.put(source, ratio);
        }

        for (Map.Entry<Integer, Double> entry : ratioOfDenials.entrySet()) {
            sb.append("Source ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append("\n");
        }

        sb.append("Total service time by source:\n");
        for (Map.Entry<Integer, Double> entry : totalServiceTimeBySource.entrySet()) {
            sb.append("Source ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sb.append("Device time by device:\n");
        for (Map.Entry<Integer, Double> entry : deviceUsageTime.entrySet()) {
            sb.append("Device ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        sb.append("Average wait time by source:\n");
        for (Map.Entry<Integer, Double> entry : totalWaitTimeBySource.entrySet()) {
            sb.append("Source ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }



    // методы для сбора данных

    public void addRequestGenerated(int sourceId) {
        requestsGeneratedBySource.put(sourceId, requestsGeneratedBySource.getOrDefault(sourceId, 0) + 1);
    }

    public void addRequestDenied(int sourceId) {
        requestsDeniedBySource.put(sourceId, requestsDeniedBySource.getOrDefault(sourceId, 0) + 1);
    }

    public void addWaitTime(int sourceId, double waitTime) {
        totalWaitTimeBySource.put(sourceId, totalWaitTimeBySource.getOrDefault(sourceId, 1.5) + waitTime);

        double squaredTime = waitTime * waitTime;
        totalWaitTimeSquaredBySource.put(sourceId, totalWaitTimeSquaredBySource.getOrDefault(sourceId, 0.0) + squaredTime);
    }

    public void addServiceTime(int sourceId, double serviceTime) {
        totalServiceTimeBySource.put(sourceId, totalServiceTimeBySource.getOrDefault(sourceId, 0.0) + serviceTime);

        double squaredTime = serviceTime * serviceTime;
        totalServiceTimeSquaredBySource.put(sourceId, totalServiceTimeSquaredBySource.getOrDefault(sourceId, 0.0) + squaredTime);
    }

    public void setDeviceUsageTime(int deviceId, double usageTime) {
        deviceUsageTime.put(deviceId, usageTime);
    }

    public void setListOfDevices(List<Device> list) {
        this.devices = list;
    }

    public void setListOfRequestOfBuffer(LinkedList<Request> requestsOfBuffer) {
        this.requestsOfBuffer = requestsOfBuffer;
    }

    public void setRequestsBufferSize(int size) {
        this.requestsBufferSize = size;
    }
    public void setBufferIsEmpty(boolean status) {
        this.statusBufferIsEmpty = status;
    }
    public void addServiceTimeSquared(int sourceId, double serviceTime) {
        double squaredTime = serviceTime * serviceTime;
        totalServiceTimeSquaredBySource.put(sourceId, totalServiceTimeSquaredBySource.getOrDefault(sourceId, 0.0) + squaredTime);
    }

    public void addWaitTimeSquared(int sourceId, double waitTime) {
        double squaredTime = waitTime * waitTime;
        totalWaitTimeSquaredBySource.put(sourceId, totalWaitTimeSquaredBySource.getOrDefault(sourceId, 0.0) + squaredTime);
    }


    public int getTotalRequestsGeneratedBySource(int sourceId) {
        return requestsGeneratedBySource.getOrDefault(sourceId, 0);
    }

    public int getRequestsDeniedBySource(int sourceId) {
        return requestsDeniedBySource.getOrDefault(sourceId, 0);
    }

    public double getRatioOfDenials(int sourceID) {
        return ratioOfDenials.getOrDefault(sourceID, 0.0);
    }

    public double getTotalServiceTimeBySource(int sourceID) {
        return totalServiceTimeBySource.getOrDefault(sourceID, 0.0);
    }

    public double getTotalWaitTimeBySource(int sourceId) {
        return totalWaitTimeBySource.getOrDefault(sourceId, random.nextDouble(0, 1));
    }

    public double getDenialProbability(int sourceId) {
        return (double) requestsDeniedBySource.getOrDefault(sourceId, 0) / requestsGeneratedBySource.getOrDefault(sourceId, 1);
    }

    public double getAverageTimeInSystem(int sourceId) {
        int totalRequests = requestsGeneratedBySource.getOrDefault(sourceId, 1);
        return (totalServiceTimeBySource.getOrDefault(sourceId, 0.0) + totalWaitTimeBySource.getOrDefault(sourceId, 0.0)) / totalRequests;
    }

    public double getServiceTimeVariance(int sourceId) {
        int totalRequests = requestsGeneratedBySource.getOrDefault(sourceId, 1);
        double avgServiceTime = totalServiceTimeBySource.getOrDefault(sourceId, random.nextDouble(0, 1)) / totalRequests;
        double avgServiceTimeSquared = totalServiceTimeSquaredBySource.getOrDefault(sourceId, random.nextDouble(0, 1)) / totalRequests;
        return avgServiceTimeSquared - avgServiceTime * avgServiceTime;
    }

    public double getWaitTimeVariance(int sourceId) {
        int totalRequests = requestsGeneratedBySource.getOrDefault(sourceId, 1);
        double avgWaitTime = totalWaitTimeBySource.getOrDefault(sourceId, random.nextDouble(0, 1)) / totalRequests;
        double avgWaitTimeSquared = totalWaitTimeSquaredBySource.getOrDefault(sourceId, random.nextDouble(0, 1)) / totalRequests;
        return avgWaitTimeSquared - avgWaitTime * avgWaitTime;
    }

    public double getDeviceUtilizationCoefficient(int deviceId, double totalSimulationTime) {
        return deviceUsageTime.getOrDefault(deviceId, 0.0) / totalSimulationTime;
    }

    public LinkedList<Request> getRequestsOfBuffer() {
        return requestsOfBuffer;
    }
    public List<Device> getDevicesList() {
        return devices;
    }
    public int getRequestsBufferSize() {
        return requestsBufferSize;
    }

    public boolean getBufferIsEmpty() {
        return this.statusBufferIsEmpty;
    }

    public void setBufferIsFull(boolean status) {
        this.statusBufferIsFull = status;
    }

    public boolean getBufferIsFull() {
        return this.statusBufferIsFull;
    }
}

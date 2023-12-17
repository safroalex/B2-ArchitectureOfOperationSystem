package com.safroalex.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Source {
    private final Random random = new Random();
    private int totalGeneratedRequests = 0;
    private final double minInterval;
    private final double maxInterval;
    private final int sourceId;
    private final List<Request> requests = new ArrayList<>();
    private final Statistics statistics;
    public Source(Statistics statistics ,int sourceId, double minInterval, double maxInterval) {
        this.statistics = statistics;
        this.sourceId = sourceId;
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
    }

    @Override
    public String toString() {
        return "Source{" +
                "sourceId=" + sourceId +
                ", totalGeneratedRequests=" + totalGeneratedRequests +
                ", averageServiceTime=" + getAverageServiceTime() +
                '}';
    }

    // Генерирует следующую заявку
    public Request generateRequest(double currentModelTime) {
        totalGeneratedRequests++;
        double creationTime = getNextCreationTime();
        double creationTimeForTable = currentModelTime + creationTime % 10;
        Request newRequest = new Request(sourceId, totalGeneratedRequests, creationTime, creationTimeForTable);
        requests.add(newRequest);  // Добавляем заявку в лист
        statistics.addRequestGenerated(newRequest.getSourceId());
        return newRequest;
    }

    private double getNextCreationTime() {
        return minInterval + (maxInterval - minInterval) * random.nextDouble();
    }

    public int getTotalGeneratedRequests() {
        return totalGeneratedRequests;
    }

    public int getSourceId() {
        return sourceId;
    }

    public double getAverageServiceTime() {
        double sumServiceTime = 0;
        int processedRequestsCount = 0;

        for (Request request : requests) {
            if (request.getCompletionTime() > 0) { // Проверяем, была ли заявка обработана
                sumServiceTime += request.getCompletionTime() - request.getCreationTime();
                processedRequestsCount++; // Учитываем только обработанные заявки
            }
        }

        return processedRequestsCount > 0 ? sumServiceTime / processedRequestsCount : 0;
    }


    // Метод для информирования источника о завершении обработки заявки
    public void informRequestCompletion(Request request, double completionTime) {
        request.setCompletionTime(completionTime);
    }
}


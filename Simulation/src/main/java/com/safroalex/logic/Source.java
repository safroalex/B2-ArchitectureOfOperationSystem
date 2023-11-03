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
    public Source(int sourceId, double minInterval, double maxInterval) {
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
    public Request generateRequest() {
        totalGeneratedRequests++;
        double creationTime = getNextCreationTime();
        Request newRequest = new Request(sourceId, totalGeneratedRequests, creationTime);
        requests.add(newRequest);  // Добавляем заявку в лист
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
        double sum = 0;
        for (Request request : requests) {
            sum += request.getCompletionTime() - request.getCreationTime();
        }
        return sum / totalGeneratedRequests;
    }

    // Метод для информирования источника о завершении обработки заявки
    public void informRequestCompletion(Request request, double completionTime) {
        request.setCompletionTime(completionTime);
    }
}


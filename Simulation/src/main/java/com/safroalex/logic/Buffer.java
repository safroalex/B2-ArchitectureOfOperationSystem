package com.safroalex.logic;

import java.util.LinkedList;
import java.util.List;

public class Buffer {
    private final int capacity; // Максимальная вместимость буфера
    protected final LinkedList<Request> requests; // Список заявок в буфере
    private Statistics statistics;

    public Buffer(Statistics statistics, int capacity) {
        this.statistics = statistics;
        this.capacity = capacity;
        this.requests = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "Buffer{" +
                "capacity=" + capacity +
                ", currentSize=" + requests.size() +
                ", requests=" + requests +
                '}';
    }

    // Добавление заявки в буфер по правилу D1OZ2 (постановка в конец)
    public boolean addToBuffer_D1OZ2(Request request, double currentTime) {
        if (requests.size() < capacity) {
            request.setEntryTime(currentTime); // Устанавливаем время входа в буфер
            requests.addLast(request);
            return true;
        } else {
            handleRejection_D1OO4(request, currentTime);
            return false;
        }
    }

    // Обработка отказа по правилу D1OO4 (отбрасывание самой последней заявки)
    private void handleRejection_D1OO4(Request newRequest, double currentTime) {
        Request lastRequest = requests.removeLast();
        statistics.addRequestDenied(lastRequest.getSourceId());
        RejectionHandler.addRejectedRequest(lastRequest);
        newRequest.setEntryTime(currentTime); // Устанавливаем время входа в буфер для новой заявки
        requests.addLast(newRequest);
    }

    public LinkedList<Request> getRequests() {
        return requests;
    }

    // Получение заявки из буфера. Вернет null, если буфер пуст.
    public Request getRequest(double currentModelTime) {
        if (requests.isEmpty()) {
            return null;
        }

        Request highestPriorityRequest = null;
        int highestPriority = Integer.MIN_VALUE;

        // Поиск заявки с наивысшим приоритетом
        for (Request request : requests) {
            int priority = request.getSourceId(); // предполагаем, что приоритет определяется идентификатором источника
            if (priority > highestPriority) {
                highestPriority = priority;
                highestPriorityRequest = request;
            }
        }

        // Если найдены заявки с одинаковым наивысшим приоритетом, выбираем последнюю поступившую
        if (highestPriorityRequest != null) {
            requests.remove(highestPriorityRequest);

            // Рассчитываем время ожидания заявки в буфере и добавляем его в статистику
            double entryTime = highestPriorityRequest.getEntryTime();
            double waitTime = currentModelTime - entryTime;
            statistics.addWaitTime(highestPriorityRequest.getSourceId(), waitTime);
        }

        return highestPriorityRequest;
    }

    public double getWaitTimeForRequest(Request request, double currentTime) {
        if (requests.contains(request)) {
            return currentTime - request.getEntryTime();
        }
        return 0; // или вернуть другое значение или исключение, если заявка не находится в буфере
    }

    public int getRequestsCount() {
        return requests.size();
    }
    // Проверка, пуст ли буфер
    public boolean isEmpty() {
        return requests.isEmpty();
    }

    // Проверка, полон ли буфер
    public boolean isFull() {
        return requests.size() == capacity;
    }

}

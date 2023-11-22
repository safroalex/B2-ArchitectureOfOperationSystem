package com.safroalex.logic;

import java.util.LinkedList;

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

    // Получение заявки из буфера. Вернет null, если буфер пуст.
    public Request getRequest() {
        if (requests.isEmpty()) {
            return null;
        } else {
            return requests.removeFirst();
        }
    }

    public double getWaitTimeForRequest(Request request, double currentTime) {
        if (requests.contains(request)) {
            return currentTime - request.getEntryTime();
        }
        return 0; // или вернуть другое значение или исключение, если заявка не находится в буфере
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

package com.safroalex.logic;

import java.util.LinkedList;

public class Buffer {
    private final int capacity; // Максимальная вместимость буфера
    private final LinkedList<Request> requests; // Список заявок в буфере

    public Buffer(int capacity) {
        this.capacity = capacity;
        this.requests = new LinkedList<>();
    }

    // Добавление заявки в буфер по правилу D1OZ2 (постановка в конец)
    public boolean addToBuffer_D1OZ2(Request request) {
        if (requests.size() < capacity) {
            requests.addLast(request);
            return true;
        } else {
            handleRejection_D1OO4(request);
            return false;
        }
    }

    // Обработка отказа по правилу D1OO4 (отбрасывание самой последней заявки)
    private void handleRejection_D1OO4(Request newRequest) {
        Request lastRequest = requests.removeLast();
        RejectionHandler.addRejectedRequest(lastRequest);
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

    // Проверка, пуст ли буфер
    public boolean isEmpty() {
        return requests.isEmpty();
    }

    // Проверка, полон ли буфер
    public boolean isFull() {
        return requests.size() == capacity;
    }
}

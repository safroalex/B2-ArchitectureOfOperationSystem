package com.safroalex.logic;

import java.util.LinkedList;
import java.util.List;

public class RejectionHandler {
    private static final List<Request> rejectedRequests = new LinkedList<>();

    // Добавление заявки в список отказов
    public static void addRejectedRequest(Request request) {
        rejectedRequests.add(request);
    }

    // Получение списка отклоненных заявок
    public static List<Request> getRejectedRequests() {
        return rejectedRequests;
    }
}

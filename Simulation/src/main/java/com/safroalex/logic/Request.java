package com.safroalex.logic;

public class Request {
    private final int sourceId;
    private final int requestId;
    private final double creationTime;
    private double completionTime;
    private double entryTime; // Время поступления заявки в буфер
    private double waitTime;
    private double creationTimeForTable;

    public Request(int sourceId, int requestId, double creationTime, double creationTimeForTable) {
        this.sourceId = sourceId;
        this.requestId = requestId;
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Request{" + "sourceId=" + sourceId + ", requestId=" + requestId + ", creationTime=" + creationTime + '}';
    }

    public void setEntryTime(double time) {
        this.entryTime = time;
    }

    public void setCompletionTime(double completionTime) {
        this.completionTime = completionTime;
    }

    public void setWaitTime(double waitTime) {
        this.waitTime = waitTime;
    }
    public double getEntryTime() {
        return entryTime;
    }
    public double getWaitTime() {
        return waitTime;
    }

    public double getCompletionTime() {
        return completionTime;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getRequestId() {
        return requestId;
    }

    public double getCreationTime() {
        return creationTime;
    }
    public double getCreationTimeForTable() {
        return creationTime;
    }
}

package com.safroalex.logic;

public class Request {
    private final int sourceId;
    private final int requestId;
    private final double creationTime;
    private double completionTime;

    public Request(int sourceId, int requestId, double creationTime) {
        this.sourceId = sourceId;
        this.requestId = requestId;
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "Request{" + "sourceId=" + sourceId + ", requestId=" + requestId + ", creationTime=" + creationTime + '}';
    }

    public void setCompletionTime(double completionTime) {  // И этот метод
        this.completionTime = completionTime;
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
}

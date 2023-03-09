package com.dkrucze.core.Data;

public class AlgorithmParameters {
    /** Name of edge detection algorithm used */
    private String name;
    /** Time of processing in ms divided */
    private double time;
    /** Amount of edged detected, 0-127.5 Higher value means more edges detected */
    private double edges;
    /** Overall performance of the algorithm on given image */
    private double performanceFactor;

    @Override
    public String toString() {
        return "AlgorithmParameters{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", edges=" + edges +
                ", performanceFactor=" + performanceFactor +
                '}';
    }

    public AlgorithmParameters(String name, double time, double edges, double performanceFactor) {
        this.name = name;
        this.time = time;
        this.edges = edges;
        this.performanceFactor = performanceFactor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getEdges() {
        return edges;
    }

    public void setEdges(double edges) {
        this.edges = edges;
    }

    public double getPerformanceFactor() {
        return performanceFactor;
    }

    public void setPerformanceFactor(double performanceFactor) {
        this.performanceFactor = performanceFactor;
    }
}

package com.dkrucze.core.Data;

public class AlgorithmParameters {
    /** Name of edge detection algorithm used */
    private String name;
    /** Time of processing in ms divided by image size */
    private double time;
    /** Amount of edged detected */
    //TODO calculate based on size,contrast,noise,blur and detected edges
    private double edges;
    /** Overall performance of the algorithm */
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

    public AlgorithmParameters(String name, double time, double edges) {
        this.name = name;
        this.time = time;
        this.edges = edges;
        //TODO calculate based on time and edges
        this.performanceFactor = 0;
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

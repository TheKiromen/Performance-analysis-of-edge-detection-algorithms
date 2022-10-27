package com.dkrucze.core.Data;

public class AlgorithmParameters {
    /** Name of edge detection algorithm used */
    private String name;
    /** Time of processing in ms divided by image size */
    private float time;
    /** Amount of edged detected */
    //TODO calculate based on size,contrast,noise,blur and detected edges
    private float edges;
    /** Overall performance of the algorithm */
    private float performanceFactor;

    @Override
    public String toString() {
        return "AlgorithmParameters{" +
                "name='" + name + '\'' +
                ", time=" + time +
                ", edges=" + edges +
                ", performanceFactor=" + performanceFactor +
                '}';
    }

    public AlgorithmParameters(String name, float time, float edges) {
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

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getEdges() {
        return edges;
    }

    public void setEdges(float edges) {
        this.edges = edges;
    }

    public float getPerformanceFactor() {
        return performanceFactor;
    }

    public void setPerformanceFactor(float performanceFactor) {
        this.performanceFactor = performanceFactor;
    }
}

package com.dkrucze.core.Data;

import java.util.ArrayList;

public class ImageParameters {
    /** File's name along with its extension */
    private String name;
    /** Number of pixels in the image */
    private int size;
    /** Amount of noise in image, value starts from 1. Higher value means more noise. */
    private double noise;
    /** Sharpness of the image, higher number means sharper image. Range is 0-127.5 */
    private double focus;
    /** Contrast of the image, range from 0 to 1 , higher value means better contrast*/
    private double contrast;
    /** Performance of different edge detection algorithms on this image */
    private ArrayList<AlgorithmParameters> algorithms;

    @Override
    public String toString() {
        return "ImageParameters{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", noise=" + noise +
                ", focus=" + focus +
                ", contrast=" + contrast +
                ", algorithms=" + algorithms +
                '}';
    }

    public ImageParameters(String name, int size, double noise, double focus, double contrast) {
        this.name = name;
        this.size = size;
        this.noise = noise;
        this.focus = focus;

        this.contrast = contrast;
        algorithms=new ArrayList<>();
    }

    public ImageParameters() {
        algorithms=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getNoise() {
        return noise;
    }

    public void setNoise(double noise) {
        this.noise = noise;
    }

    public double getFocus() {
        return focus;
    }

    public void setFocus(double focus) {
        this.focus = focus;
    }

    public double getContrast() {
        return contrast;
    }

    public void setContrast(double contrast) {
        this.contrast = contrast;
    }

    public ArrayList<AlgorithmParameters> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(ArrayList<AlgorithmParameters> ap){
        algorithms=ap;
    }
}

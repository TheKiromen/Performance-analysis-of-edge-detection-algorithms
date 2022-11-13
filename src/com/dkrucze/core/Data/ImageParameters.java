package com.dkrucze.core.Data;

import java.util.ArrayList;

public class ImageParameters {
    /** File's name along with its extension */
    private String name;
    /** Number of pixels in the image */
    private int size;
    /** Amount of noise in image */
    private double noise;
    /** Sharpness of the image, higher number means sharper image */
    private double blur;
    /** Contrast of the image, range from 0 to 1 */
    private double contrast;
    /** Performance of different edge detection algorithms on this image */
    private ArrayList<AlgorithmParameters> algorithms;

    @Override
    public String toString() {
        return "ImageParameters{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", noise=" + noise +
                ", blur=" + blur +
                ", contrast=" + contrast +
                ", algorithms=" + algorithms +
                '}';
    }

    public ImageParameters(String name, int size, double noise, double blur, double contrast) {
        this.name = name;
        this.size = size;
        this.noise = noise;
        this.blur = blur;

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

    public double getBlur() {
        return blur;
    }

    public void setBlur(double blur) {
        this.blur = blur;
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

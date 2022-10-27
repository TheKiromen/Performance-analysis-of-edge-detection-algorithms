package com.dkrucze.core.Data;

import java.util.ArrayList;

public class ImageParameters {
    /** File's name along with its extension */
    private String name;
    /** Number of pixels in the image */
    private int size;
    /** Amount of noise in image */
    private float noise;
    /** Sharpness of the image, higher number means sharper image */
    private float blur;
    /** Contrast of the image, range from 0 to 1 */
    private float contrast;
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

    public ImageParameters(String name, int size, float noise, float blur, float contrast) {
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

    public float getNoise() {
        return noise;
    }

    public void setNoise(float noise) {
        this.noise = noise;
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        this.blur = blur;
    }

    public float getContrast() {
        return contrast;
    }

    public void setContrast(float contrast) {
        this.contrast = contrast;
    }

    public ArrayList<AlgorithmParameters> getAlgorithms() {
        return algorithms;
    }

    public void setAlgorithms(ArrayList<AlgorithmParameters> ap){
        algorithms=ap;
    }
}

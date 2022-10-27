package com.dkrucze.core.Util;

import com.dkrucze.core.Data.AlgorithmParameters;
import org.opencv.core.Mat;

import java.util.ArrayList;

public class Analyzer {

    private Mat sourceImage;
    private ArrayList<AlgorithmParameters> parameters;

    public Analyzer(Mat image) {
        sourceImage=image;
        parameters=new ArrayList<>();
    }

    public ArrayList<AlgorithmParameters> analyze() {
        /* TODO
            calculate each algorithms time and edges
            save it to the variable
            add the variable to output
         */
        float time=0,edges=0;
        //Sobel
        parameters.add(new AlgorithmParameters("Sobel",time,edges));
        //Prewitt?
        parameters.add(new AlgorithmParameters("Prewitt",time,edges));
        //Laplacian
        parameters.add(new AlgorithmParameters("Laplacian",time,edges));
        //LaplacianOfGaussian??
        parameters.add(new AlgorithmParameters("Laplacian of Gauss",time,edges));
        //Canny??,
        parameters.add(new AlgorithmParameters("Canny",time,edges));

        //Return the output
        return parameters;
    }
}

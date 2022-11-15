package com.dkrucze.core.Util;

import com.dkrucze.core.Data.AlgorithmParameters;
import com.dkrucze.core.Data.ImageParameters;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class Analyzer {

    private ImageParameters imgData;
    private Mat sourceImage;

    public Analyzer(Mat image, ImageParameters imgData) {
        this.imgData=imgData;
        this.sourceImage=image;
    }

    public void analyze() {
        /**
            time - how many milliseconds it takes to perform edge detection
            edges - standard deviation of image after edge detection
            performance factor = (edges*contrast)/(size*time*noise*focus)
         */
        double time,edges,performanceFactor;
        MatOfDouble stDev = new MatOfDouble();
        Mat imgEdges = new Mat();

        //-----------------------------------------------Sobel-----------------------------------------------
        //Start time measurement
        time=System.currentTimeMillis();
        //Perform sobel edge detection
        Imgproc.Sobel(sourceImage,imgEdges,0,1,1);
        //Finish time measurement
        time=System.currentTimeMillis()-time;
        //Calculate the amount of detected edges
        Core.meanStdDev(sourceImage,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Calculate the overall performance of the algorithm
        //TODO verify it, increase noise weight????
        performanceFactor=(edges*imgData.getContrast())/(imgData.getSize()*time*imgData.getNoise()*imgData.getFocus());
        //Save the results
        imgData.getAlgorithms().add(new AlgorithmParameters("Sobel",time,edges,performanceFactor));


        //-----------------------------------------------Prewitt-----------------------------------------------
        //TODO
        imgData.getAlgorithms().add(new AlgorithmParameters("Prewitt",time,edges,performanceFactor));


        //-----------------------------------------------Laplacian-----------------------------------------------
        //TODO
        imgData.getAlgorithms().add(new AlgorithmParameters("Laplacian",time,edges,performanceFactor));


        //-----------------------------------------------LaplacianOfGaussian-----------------------------------------------
        //TODO
        imgData.getAlgorithms().add(new AlgorithmParameters("Laplacian of Gauss",time,edges,performanceFactor));


        //-----------------------------------------------Canny-----------------------------------------------
        //TODO
        imgData.getAlgorithms().add(new AlgorithmParameters("Canny",time,edges,performanceFactor));

        //Save the image for testing purposes
        //Imgcodecs.imwrite("outputImgs/"+imgData.getName()+"Sobel.jpg", imgEdges);
    }
}

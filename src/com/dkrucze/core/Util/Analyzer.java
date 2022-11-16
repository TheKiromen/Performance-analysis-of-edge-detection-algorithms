package com.dkrucze.core.Util;

import com.dkrucze.core.Data.AlgorithmParameters;
import com.dkrucze.core.Data.ImageParameters;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


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
            performance factor = (edges*contrast)/(time*noise*focus)
         */
        double time,edges,performanceFactor;
        MatOfDouble stDev = new MatOfDouble();
        Mat imgEdges = new Mat();

        //Smooth the image to remove high frequency noise
        Imgproc.blur(sourceImage,sourceImage,new Size(3,3));

        //-----------------------------------------------Sobel-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Perform sobel edge detection
        Imgproc.Sobel(sourceImage,imgEdges,-1,1,1);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Calculate the overall performance of the algorithm
        performanceFactor=(edges*imgData.getContrast())/(time*imgData.getNoise()*imgData.getFocus());
        //Save the results
        imgData.getAlgorithms().add(new AlgorithmParameters("Sobel",time,edges,performanceFactor));


        //-----------------------------------------------Prewitt-----------------------------------------------
        //TODO
        //imgData.getAlgorithms().add(new AlgorithmParameters("Prewitt",time,edges,performanceFactor));


        //-----------------------------------------------Laplacian-----------------------------------------------
        //TODO
        //imgData.getAlgorithms().add(new AlgorithmParameters("Laplacian",time,edges,performanceFactor));


        //-----------------------------------------------LaplacianOfGaussian-----------------------------------------------
        //TODO
        //imgData.getAlgorithms().add(new AlgorithmParameters("Laplacian of Gauss",time,edges,performanceFactor));


        //-----------------------------------------------Canny-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Perform sobel edge detection
        //TODO find better threshold values
        Imgproc.Canny(sourceImage,imgEdges,100,150);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Calculate the overall performance of the algorithm
        performanceFactor=(edges*imgData.getContrast())/(time*imgData.getNoise()*imgData.getFocus());
        //Save the results
        imgData.getAlgorithms().add(new AlgorithmParameters("Canny",time,edges,performanceFactor));

        //Save the image for testing purposes
        //Imgcodecs.imwrite("outputImgs/"+imgData.getName()+"Sobel.jpg", imgEdges);
    }
}

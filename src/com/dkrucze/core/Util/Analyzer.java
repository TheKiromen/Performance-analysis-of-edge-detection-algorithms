package com.dkrucze.core.Util;

import com.dkrucze.core.Data.AlgorithmParameters;
import com.dkrucze.core.Data.ImageParameters;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.opencv.core.CvType.CV_32F;


public class Analyzer {

    //Kernels
    Mat prewitt_kernel_x = new Mat(3,3,CV_32F);
    Mat prewitt_kernel_y = new Mat(3,3,CV_32F);
    Mat prewitt_kernel_diag1 = new Mat(3,3,CV_32F);
    Mat prewitt_kernel_diag2 = new Mat(3,3,CV_32F);
    Mat roberts_kernel_x = new Mat(2,2, CvType.CV_32F);
    Mat roberts_kernel_y = new Mat(2,2,CvType.CV_32F);

    //Input Data
    final ImageParameters imgData;
    final Mat sourceImage;

    public Analyzer(Mat image, ImageParameters imgData) {
        this.imgData=imgData;
        this.sourceImage=image;

        //Initialize Roberts cross
        roberts_kernel_x.put(0,0,1,0,0,-1);
        roberts_kernel_y.put(0,0,0,1,-1,0);

        //Initialize Prewitt kernels
        prewitt_kernel_x.put(0,0,-1,0,1,-1,0,1,-1,0,1);
        prewitt_kernel_y.put(0,0,1,1,1,0,0,0,-1,-1,-1);
        prewitt_kernel_diag1.put(0,0,0,1,1,-1,0,1,-1,-1,0);
        prewitt_kernel_diag2.put(0,0,1,1,0,1,0,-1,0,-1,-1);
    }

    public void analyze() {
        /*
            time - how many milliseconds it takes to perform edge detection
            edges - standard deviation of image after edge detection
         */
        double time,edges;
        //------------------------------------Algorithm outputs
        //Edges
        MatOfDouble stDev = new MatOfDouble();
        Mat imgEdges = new Mat();
        //Horizontal and Vertical gradients
        Mat x = new Mat();
        Mat y = new Mat();
        //Angled gradients
        Mat angle1 = new Mat();
        Mat angle2 = new Mat();
        //Prewitt weighted sums
        Mat prewitt_straight = new Mat();
        Mat prewitt_angled = new Mat();

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
        //Save the results
        imgData.getAlgorithms().add(new AlgorithmParameters("Sobel",time,edges));


        //-----------------------------------------------Prewitt_2K-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Convolve the image
        Imgproc.filter2D(sourceImage,x,-1,prewitt_kernel_x);
        Imgproc.filter2D(sourceImage,y,-1,prewitt_kernel_y);
        //Calculate the gradient
        Core.addWeighted(x,0.5,y,0.5,0,imgEdges);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Prewitt_2K",time,edges));


        //-----------------------------------------------Prewitt_4K-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Convolve the image
        Imgproc.filter2D(sourceImage,x,-1,prewitt_kernel_x);
        Imgproc.filter2D(sourceImage,y,-1,prewitt_kernel_y);
        Imgproc.filter2D(sourceImage,angle1,-1,prewitt_kernel_diag1);
        Imgproc.filter2D(sourceImage,angle2,-1,prewitt_kernel_diag2);
        //Calculate the gradient
        Core.addWeighted(x,0.5,y,0.5,0,prewitt_straight);
        Core.addWeighted(angle1,0.5,angle2,0.5,0,prewitt_angled);
        Core.addWeighted(prewitt_straight,0.5,prewitt_angled,0.5,0,imgEdges);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(prewitt_straight,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Prewitt_4K",time,edges));


        //-----------------------------------------------Roberts-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Convolve the image
        Imgproc.filter2D(sourceImage,x,-1,prewitt_kernel_x);
        Imgproc.filter2D(sourceImage,y,-1,prewitt_kernel_y);
        //Calculate the gradient
        Core.addWeighted(x,0.5,y,0.5,0,imgEdges);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Roberts",time,edges));


        //-----------------------------------------------Laplacian Of Gaussian-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Perform sobel edge detection
        Imgproc.Laplacian(sourceImage,imgEdges,-1);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        imgData.getAlgorithms().add(new AlgorithmParameters("Laplacian Of Gaussian",time,edges));


        //-----------------------------------------------Canny-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Calculate the thresholds
        MatOfDouble mean = new MatOfDouble();
        Core.meanStdDev(sourceImage,mean,new MatOfDouble());
        double lowerThreshold,higherThreshold;
        lowerThreshold=mean.get(0,0)[0]*0.7;
        higherThreshold=mean.get(0,0)[0]*1.3;
        //Perform sobel edge detection with dynamic thresholds
        Imgproc.Canny(sourceImage,imgEdges,lowerThreshold ,higherThreshold);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Save the results
        imgData.getAlgorithms().add(new AlgorithmParameters("Canny",time,edges));

        //Save the image for testing purposes
        // Imgcodecs.imwrite("outputImgs/"+imgData.getName()+"Canny.jpg", imgEdges);


        //-----------------------------------------------Hough-lines-----------------------------------------------
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Hough-lines",0,0));

        //-----------------------------------------------Hough-circles-----------------------------------------------
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Hough-circles",0,0));

        //-----------------------------------------------Hough-combined-----------------------------------------------
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Hough-combined",0,0));

        //-----------------------------------------------High pass filter-----------------------------------------------
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("High pass filter",0,0));
    }
}

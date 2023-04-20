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
        // Imgcodecs.imwrite("images/outputImgs/"+imgData.getName()+"Canny.jpg", imgEdges);


        //-----------------------------------------------Hough-lines-----------------------------------------------
        double cannyTime = time;
        //Start time measurement
        time=System.nanoTime();
        //Variable to hold lines
        Mat lines = new Mat();
        //Detect the lines
        Imgproc.HoughLinesP(imgEdges, lines, 1, Math.PI/180, 50, 50, 10);
        // Draw the lines
        imgEdges = new Mat(sourceImage.height(), sourceImage.width(), CV_32F);
        for (int i = 0; i < lines.rows(); i++) {
            double[] l = lines.get(i, 0);
            Imgproc.line(imgEdges, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(255,0,0), 1, Imgproc.LINE_AA, 0);
        }
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        time+=cannyTime;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        edges=stDev.get(0,0)[0];
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Hough-lines",time,edges));

        // Imgcodecs.imwrite("images/outputImgs/"+imgData.getName(), imgEdges);


        // //-----------------------------------------------Hough-circles-----------------------------------------------
        // linesTime = time;
        // //Start time measurement
        // time=System.nanoTime();
        // //Variable to hold circles
        // Mat circles = new Mat();
        // //Detect the circles
        // Imgproc.HoughCircles(sourceImage, circles, Imgproc.HOUGH_GRADIENT, 1.0, sourceImage.width()/16, 100, 30, 1, 100);
        // imgEdges = new Mat(sourceImage.height(), sourceImage.width(), CV_32F);
        // //Draw circles
        // for (int i = 0; i < circles.cols(); i++) {
        //     double[] c = circles.get(0, i);
        //     Point center = new Point(Math.round(c[0]), Math.round(c[1]));
        //     // circle center
        //     Imgproc.circle(imgEdges, center, 1, new Scalar(0,100,100), 3, 8, 0 );
        //     // circle outline
        //     int radius = (int) Math.round(c[2]);
        //     Imgproc.circle(imgEdges, center, radius, new Scalar(255,0,255), 3, 8, 0 );
        // }
        // //Finish time measurement
        // time=(System.nanoTime()-time)/1000000.0;
        // System.out.println(time);
        // // time+=cannyTime;
        // //Calculate the amount of detected edges
        // Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        // edges=stDev.get(0,0)[0];
        // //Save the result
        // imgData.getAlgorithms().add(new AlgorithmParameters("Hough-circles",time,edges));
        //
        // Imgcodecs.imwrite("images/outputImgs/"+imgData.getName(), imgEdges);


        // //-----------------------------------------------Hough-combined-----------------------------------------------
        // circlesTime = time;
        //
        // //Save the result
        // imgData.getAlgorithms().add(new AlgorithmParameters("Hough-combined",time,edges));


        //-----------------------------------------------High pass filter-----------------------------------------------
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("High pass filter",time,edges));

        //-----------------------------------------------Contours-----------------------------------------------
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Contours",time,edges));
    }
}

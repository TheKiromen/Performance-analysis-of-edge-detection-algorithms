package com.dkrucze.core.Util;

import com.dkrucze.core.Data.AlgorithmParameters;
import com.dkrucze.core.Data.ImageParameters;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_32F;
import static org.opencv.core.CvType.CV_8UC1;


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
        double cannyTime = time;
        //Start time measurement
        time=System.nanoTime();
        //Variable to hold lines
        Mat lines = new Mat();
        //Detect the lines
        Imgproc.HoughLinesP(imgEdges, lines, 1, Math.PI/180, 50, 50, 10);
        // Draw the lines
        imgEdges = new Mat(sourceImage.rows(), sourceImage.cols(), CV_32F);
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
        //FIXME
        // System.out.println(imgData.getName() + " | " + edges);
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Hough-lines",time,edges));

        // Imgcodecs.imwrite("outputImgs/"+imgData.getName(), imgEdges);


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


        //-----------------------------------------------Contours-----------------------------------------------
        //Start time measurement
        time=System.nanoTime();
        //Perform binary thresholding
        Mat binary =  new Mat(sourceImage.rows(), sourceImage.cols(), sourceImage.type(), new Scalar(0));
        Imgproc.threshold(sourceImage, binary, 100, 255, Imgproc.THRESH_BINARY);
        //Find contours of the image
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(binary, contours, new Mat(), Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        //Draw the contours
        imgEdges = new Mat(sourceImage.rows(), sourceImage.cols(), CV_32F);
        Imgproc.drawContours(imgEdges, contours, -1, new Scalar(255,255,255), 1);
        //Finish time measurement
        time=(System.nanoTime()-time)/1000000.0;
        //Calculate the amount of detected edges
        Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        //FIXME
        edges=stDev.get(0,0)[0];
        // System.out.println(imgData.getName() + " | " + edges);
        //Save the result
        imgData.getAlgorithms().add(new AlgorithmParameters("Contours",time,edges));

        // Imgcodecs.imwrite("outputImgs/"+imgData.getName(), imgEdges);

        //-----------------------------------------------High pass filter-----------------------------------------------
        // //Start time measurement
        // time=System.nanoTime();
        // //Variables
        // Mat complexImage = new Mat();
        // Mat padded = new Mat();
        // List<Mat> planes = new ArrayList<>();
        // //Get optimal image dimensions for dft
        // int addPixelRows = Core.getOptimalDFTSize(sourceImage.rows());
        // int addPixelCols = Core.getOptimalDFTSize(sourceImage.cols());
        // //Expand image to optimal size by adding black border
        // Core.copyMakeBorder(sourceImage, padded, 0, addPixelRows - sourceImage.rows(), 0, addPixelCols - sourceImage.cols(),Core.BORDER_CONSTANT, Scalar.all(0));
        // //Create place to store complex and real values
        // padded.convertTo(padded, CvType.CV_32F);
        // planes.add(padded);
        // planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        // Core.merge(planes, complexImage);
        // //Perform the dft
        // Core.dft(complexImage, complexImage);
        // //Optimize magnitude
        // List<Mat> newPlanes = new ArrayList<>();
        // Mat magnitude = new Mat();
        // //Split the complex image in two planes
        // Core.split(complexImage, newPlanes);
        // //Compute the magnitude
        // Core.magnitude(newPlanes.get(0), newPlanes.get(1), magnitude);
        // //Move to a logarithmic scale
        // Core.add(Mat.ones(magnitude.size(), CvType.CV_32F), magnitude, magnitude);
        // Core.log(magnitude, magnitude);
        // //Rearrange the 4 quadrants of the magnitude image (move zero to the center of the image)
        // magnitude = magnitude.submat(new Rect(0, 0, magnitude.cols() & -2, magnitude.rows() & -2));
        // int cx = magnitude.cols() / 2;
        // int cy = magnitude.rows() / 2;
        // //Quadrants
        // Mat q0 = new Mat(magnitude, new Rect(0, 0, cx, cy));
        // Mat q1 = new Mat(magnitude, new Rect(cx, 0, cx, cy));
        // Mat q2 = new Mat(magnitude, new Rect(0, cy, cx, cy));
        // Mat q3 = new Mat(magnitude, new Rect(cx, cy, cx, cy));
        // //Rearrange
        // Mat tmp = new Mat();
        // q0.copyTo(tmp);
        // q3.copyTo(q0);
        // tmp.copyTo(q3);
        // q1.copyTo(tmp);
        // q2.copyTo(q1);
        // tmp.copyTo(q2);
        // //Normalize the magnitude image for the visualization since both JavaFX
        // //and OpenCV need images with value between 0 and 255, convert back to CV_8UC1
        // magnitude.convertTo(magnitude, CvType.CV_8UC1);
        // Core.normalize(magnitude, magnitude, 0, 255, Core.NORM_MINMAX, CvType.CV_8UC1);
        //
        // List<Mat> maskPlanes = new ArrayList<>();
        // Core.split(complexImage, maskPlanes);
        // // Mat mask = new Mat();
        // // maskPlanes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        // // maskPlanes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        // // Core.merge(maskPlanes, mask);
        // System.out.println(Core.minMaxLoc(maskPlanes.get(0)).maxVal);
        // maskPlanes.get(0).matMul(Mat.ones(padded.size(), padded.type()));
        // // maskPlanes.get(0).mul(Mat.ones(padded.size(), padded.type()));
        // // maskPlanes.get(1).mul(Mat.ones(padded.size(), padded.type()));
        // System.out.println(Core.minMaxLoc(maskPlanes.get(0)).maxVal);
        //
        // Core.merge(maskPlanes, complexImage);
        //
        // //Inverse the dft
        // Core.idft(complexImage, complexImage);
        // Mat restoredImage = new Mat();
        // Core.split(complexImage, planes);
        // Core.normalize(planes.get(0), restoredImage, 0, 255, Core.NORM_MINMAX);
        //
        // //Calculate the amount of detected edges
        // Core.meanStdDev(imgEdges,new MatOfDouble(), stDev);
        // edges=stDev.get(0,0)[0];
        // //Save the result
        // imgData.getAlgorithms().add(new AlgorithmParameters("High pass filter",time,edges));
        //
        // // Imgcodecs.imwrite("outputImgs/"+imgData.getName(), magnitude);
        // Imgcodecs.imwrite("outputImgs/"+imgData.getName(), restoredImage);
    }
}

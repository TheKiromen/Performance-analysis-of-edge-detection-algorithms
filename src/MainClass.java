import com.dkrucze.core.Data.ImageParameters;
import com.dkrucze.core.Util.Analyzer;
import com.dkrucze.core.Util.CSVConverter;
import com.dkrucze.core.Util.ImageLoader;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainClass {

    //Initialize native library.
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) {
        //Variables
        ArrayList<ImageParameters> outputData = new ArrayList<>();
        Mat noise_kernel = new Mat(3,3, CvType.CV_32F);
        noise_kernel.put(0,0,1,-2,1,-2,4,-2,1,-2,1);

        //Load all the images from images folder
        ArrayList<String> imagesPaths = ImageLoader.loadFiles("images/Variety");

        //For each image calculate its parameters
        for (String path : imagesPaths) {
            //Create the image
            Mat input = Imgcodecs.imread(path);
            Mat img = new Mat();
            Mat tmp = new Mat();
            //Convert the image to greyscale
            Imgproc.cvtColor(input,img,Imgproc.COLOR_RGB2GRAY);

            //Create new parameters for each image
            ImageParameters parameters = new ImageParameters();

            //Get image's name from the path
            parameters.setName(path.substring(path.lastIndexOf("\\")+1));

            //Calculate image properties
            //-----------------------------Image size-----------------------------
            parameters.setSize(img.width()*img.height());

            //-----------------------------Image noise-----------------------------
            //Convolve with kernel
            Imgproc.filter2D(img, tmp, -1, noise_kernel);
            //Get sum of the elements
            double sum = Core.sumElems(tmp).val[0];
            //Calculate the noise
            double noise = Math.sqrt(Math.PI/2)*(sum/(6*(img.width()-2)*(img.height()-2)));
            //Save the result
            parameters.setNoise(noise);
            //Variables for noise calculation
            // MatOfDouble sourceStdDev = new MatOfDouble();
            // MatOfDouble smoothStdDev = new MatOfDouble();
            //
            // //Blur the source image
            // Imgproc.blur(img,tmp,new Size(5,5));
            //
            // //Calculate the standard deviation of source and blurred image
            // Core.meanStdDev(img,new MatOfDouble(),sourceStdDev);
            // Core.meanStdDev(tmp,new MatOfDouble(),smoothStdDev);
            //
            // //Calculate the noise by comparing both deviations
            // //More noise = higher deviation
            // double noise = sourceStdDev.get(0,0)[0]/smoothStdDev.get(0,0)[0];

            //-----------------------------Image focus-----------------------------
            //Convolve image with Laplacian
            Imgproc.Laplacian(img,tmp,-1);
            //Calculate the standard deviation of image
            MatOfDouble focus = new MatOfDouble();
            Core.meanStdDev(tmp,new MatOfDouble(),focus);
            //Set focus equal to deviation
            parameters.setFocus(focus.get(0,0)[0]);

            //-----------------------------Image contrast-----------------------------
            MatOfDouble sourceStdDev = new MatOfDouble();
            //Get standard deviation of the source image
            Core.meanStdDev(img,new MatOfDouble(),sourceStdDev);
            //Calculate the RMS contrast and normalize it
            //RMS contrast is the same as standard deviation of colors
            //Maximum value is 127.5 which is half of the spectrum
            double contrast = sourceStdDev.get(0,0)[0]/(255.0/2);
            //Save the result
            parameters.setContrast(contrast);


            //-----------------------------Algorithms-----------------------------
            //Analyze the performance of different edge detection algorithms
            Analyzer algorithmsAnalyzer = new Analyzer(img,parameters);
            algorithmsAnalyzer.analyze();

            //Save file parameters
            outputData.add(parameters);

            //Display best performing algorithm
            //double max=0.0;
            //String name="";
            //double time=0;
            //for (AlgorithmParameters p : parameters.getAlgorithms()) {
            //    if(p.getPerformanceFactor()>max){
            //        max=p.getPerformanceFactor();
            //        name=p.getName();
            //        time=p.getTime();
            //    }
            //}
            //System.out.println(parameters.getName()+" "+name+" "+max+" "+time);


            //Display canny results
            // System.out.println(parameters.getName()+"   Canny: "+parameters.getAlgorithms().get(5).getEdges());
        }

        //Save data to csv file.
        CSVConverter converter = new CSVConverter(outputData);
        List<String> csvData = converter.convertToCSV();
        try {
            converter.saveCSVDataToFile(new File("output/data.csv"), csvData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

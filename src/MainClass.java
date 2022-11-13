import com.dkrucze.core.Data.ImageParameters;
import com.dkrucze.core.Util.Analyzer;
import com.dkrucze.core.Util.ImageLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Arrays;


public class MainClass {

    //Initialize native library.
    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void main(String[] args) {
        //Variables
        ArrayList<ImageParameters> outputData = new ArrayList<>();

        //Load all the images from images folder
        ArrayList<String> imagesPaths = ImageLoader.loadFiles("images");

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
            //Variables for noise calculation
            MatOfDouble sourceMean = new MatOfDouble();
            MatOfDouble sourceStdDev = new MatOfDouble();
            MatOfDouble smoothMean = new MatOfDouble();
            MatOfDouble smoothStdDev = new MatOfDouble();

            //Blur the source image
            Imgproc.blur(img,tmp,new Size(5,5));

            //Calculate the standard deviation of source and blurred image
            Core.meanStdDev(img,sourceMean,sourceStdDev);
            Core.meanStdDev(tmp,smoothMean,smoothStdDev);

            //Calculate the noise by comparing both deviations
            //More noise = higher deviation
            double noise = sourceStdDev.get(0,0)[0]/smoothStdDev.get(0,0)[0];
            //Save the result
            parameters.setNoise(noise);

            //-----------------------------Image blur-----------------------------
            //TODO calculate image blur

            //-----------------------------Image contrast-----------------------------
            //TODO compare result of Weber and Michelson contrasts, pick better one out of the three
            //Calculate the RMS contrast and normalize it
            double contrast = sourceStdDev.get(0,0)[0]/255.0;
            //Save the result
            parameters.setContrast(contrast);

            System.out.println(parameters.getName()+" "+parameters.getNoise()+" "+parameters.getBlur()+" "+parameters.getContrast());


            //Analyze the performance of different edge detection algorithms
            Analyzer algorithmsAnalyzer = new Analyzer(img);
            parameters.setAlgorithms(algorithmsAnalyzer.analyze());

            //Save file parameters
            outputData.add(parameters);
        }

        //TODO write custom object to csv converter?
        //TODO or save it as a JSON using toString methods?
        outputData.stream().forEach(System.out::println);
    }
}

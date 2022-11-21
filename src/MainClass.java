import com.dkrucze.core.Data.AlgorithmParameters;
import com.dkrucze.core.Data.ImageParameters;
import com.dkrucze.core.Util.Analyzer;
import com.dkrucze.core.Util.ImageLoader;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;


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
            MatOfDouble sourceStdDev = new MatOfDouble();
            MatOfDouble smoothStdDev = new MatOfDouble();

            //Blur the source image
            Imgproc.blur(img,tmp,new Size(5,5));

            //Calculate the standard deviation of source and blurred image
            Core.meanStdDev(img,new MatOfDouble(),sourceStdDev);
            Core.meanStdDev(tmp,new MatOfDouble(),smoothStdDev);

            //Calculate the noise by comparing both deviations
            //More noise = higher deviation
            double noise = sourceStdDev.get(0,0)[0]/smoothStdDev.get(0,0)[0];
            //Save the result
            parameters.setNoise(noise);

            //-----------------------------Image focus-----------------------------
            //Convolve image with Laplacian
            Imgproc.Laplacian(img,tmp,-1);
            //Calculate the standard deviation of image
            MatOfDouble focus = new MatOfDouble();
            Core.meanStdDev(tmp,new MatOfDouble(),focus);
            //Set focus equal to deviation
            parameters.setFocus(focus.get(0,0)[0]);

            //-----------------------------Image contrast-----------------------------
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
            double max=0.0;
            String name="";
            double time=0;
            for (AlgorithmParameters p : parameters.getAlgorithms()) {
                if(p.getPerformanceFactor()>max){
                    max=p.getPerformanceFactor();
                    name=p.getName();
                    time=p.getTime();
                }
            }
            System.out.println(parameters.getName()+" "+name+" "+max+" "+time);
        }

        //TODO write custom object to csv converter?
        //TODO or save it as a JSON using toString methods?
        //outputData.forEach(System.out::println);
    }
}

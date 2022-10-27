import com.dkrucze.core.Data.ImageParameters;
import com.dkrucze.core.Util.Analyzer;
import com.dkrucze.core.Util.ImageLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

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
            Mat img = Imgcodecs.imread(path);

            //Create new parameters for each image
            ImageParameters parameters = new ImageParameters();

            //Get image's name from the path
            parameters.setName(path.substring(path.lastIndexOf("\\")+1));

            //Calculate image properties
            //Image size
            parameters.setSize(img.width()*img.height());
            //Image noise
            //TODO
            //Image blur
            //TODO
            //Image contrast
            //TODO

            //Analyze the performance of different edge detection algorithms
            Analyzer algorithmsAnalyzer = new Analyzer(img);
            parameters.setAlgorithms(algorithmsAnalyzer.analyze());

            //Save file parameters
            outputData.add(parameters);
        }

        //TODO write custom object to csv converter?
        //TODO or save it as a JSON using toString methods?
        System.out.println(outputData.get(0));
    }
}

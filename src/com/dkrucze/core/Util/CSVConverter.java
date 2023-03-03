package com.dkrucze.core.Util;

import com.dkrucze.core.Data.ImageParameters;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class CSVConverter {

    private List<ImageParameters> params;

    public CSVConverter(List<ImageParameters> params) {
        this.params = params;
    }

    public List<String> convertToCSV(){
        List<String> data = new LinkedList<>();
        StringBuilder builder = new StringBuilder();

        //Initialize column names:
        //General image info
        builder.append("Img_name,Size,Noise,Focus,Contrast,");
        //Sobel columns
        builder.append("");
        //Prewitt_2K columns
        builder.append("");
        //Prewitt_4K columns
        builder.append("");
        //Roberts columns
        builder.append("");
        //Laplacian columns
        builder.append("");
        //Canny columns
        builder.append("");

        //Add columns info to csv file.
        data.add(builder.toString());

        //Extract data from objects
        for(ImageParameters img : params){
            //Clear data buffer
            //Clears objects data buffer, equivalent to resetting the string to "".
            //This is faster than creating new StringBuilder instance.
            builder.setLength(0);

            //Image Data
            builder.append("");
            //Sobel Data
            builder.append("");
            //Prewitt_2K Data
            builder.append("");
            //Prewitt_4K Data
            builder.append("");
            //Roberts Data
            builder.append("");
            //Laplacian Data
            builder.append("");
            //Canny Data
            builder.append("");

            //Add a row of data
            data.add(builder.toString());
        }

        return data;
    }

    public void saveCSVDataToFile(File outputFile, List<String> data) throws IOException {
        try (PrintWriter pw = new PrintWriter(outputFile)) {
            //TODO test if this works good
            data.stream().forEach(pw::println);
        }
    }

}


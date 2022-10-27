package com.dkrucze.core.Util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageLoader {

    public static ArrayList<String> loadFiles(String folderPath) {
        //Walk through the given folder
        try(Stream<Path> paths = Files.walk(Paths.get(folderPath))){
            //Filter out files and convert them into a list of paths
            ArrayList<String> imagesPaths = paths.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toCollection(ArrayList::new));
            return imagesPaths;
        } catch (Exception e) {
            //If there are no files in the path return empty list
            return new ArrayList<>();
        }
    }
}

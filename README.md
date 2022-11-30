# Edge detection algorithms performance analysis

Algorithm for extracting performance data of various edge detection algorithms and image parameters.
It analyzes all provided images and return the result which allows for analysis of performance.


## Documentation

Application measures the amount of noise in the image, focus and contrast.

Then on smoothed, grayscale image it performs various edge detection algorithms and measures their performance.

Implemented Edge Detection algorithms are:
- Sobel
- Prewitt
- Advanced Prewitt
- Roberts cross
- Laplacian of Gaussian
- Canny

Overall performance of algorithm is calculated accoring to this formula:

performance = (detected edges * contrast) / (time * noise * focus) 


## Features

- Extraction of noise, focus and contrast data from images
- Performance measuring of various edge detecion algorithms
- Ability to save each result as an image
- Ability to export data to .csv file  (WORK IN PROGRESS)


## Technologies

- Java
- OpenCV


## Authors

- [@dkrucze](https://github.com/TheKiromen)

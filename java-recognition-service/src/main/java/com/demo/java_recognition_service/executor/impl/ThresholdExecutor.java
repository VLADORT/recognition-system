package com.demo.java_recognition_service.executor.impl;

import com.demo.java_recognition_service.executor.ImageProcessorExecutor;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

@Component
public class ThresholdExecutor implements ImageProcessorExecutor {

    @Override
    public byte[] execute(byte[] data) {
        // TODO: 01.09.2020 MOVE HARDCODED VALUES TO REQUEST PARAMS
        boolean erosion = true;
        boolean blurSelected = true;
        int value = 122;


        Mat img = Imgcodecs.imdecode(new MatOfByte(data), Imgcodecs.IMREAD_UNCHANGED);
        if (erosion) {
            Imgproc.erode(img, img, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        }
        if (blurSelected) {
            Imgproc.GaussianBlur(img, img, new Size(5, 5), 0);
        }
//        Core.addWeighted(img, 1.4, img, 0, 0, img);
        Imgproc.threshold(img, img, value, 255, 1);
//        Imgproc.adaptiveThreshold(img,img,150,Imgproc.ADAPTIVE_THRESH_MEAN_C,1, 5,1.2);
//        Imgproc.threshold(img, img, 120, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        int counter = calculateWhiteArea(img);

//        String currentDirectory = System.getProperty("user.dir").replaceAll("\\\\", "\\\\\\\\");
//        Imgcodecs.imwrite(currentDirectory + "\\results\\" + filename + "-thresholding-" + counter + ".jpg", img);
//        Imgcodecs.imwrite("C:\\Users\\dell\\PycharmProjects\\surface-crack-detection\\dataset\\craquelures\\train\\label\\" + filename + ".jpg", img);

        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpeg", img, matOfByte);
        return matOfByte.toArray();
        // TODO: 01.09.2020  return percentage of area in response
    }

    private int calculateWhiteArea(Mat img) {
        int counter = 0;
        for (int i = 0; i < img.rows(); i++) {
            for (int j = 0; j < img.cols(); j++) {
                if (((img.get(i, j)[0] == 255) && img.get(i, j)[2] == 0 && img.get(i, j)[1] == 0) || (img.get(i, j)[0] == 0 && img.get(i, j)[1] == 0 && img.get(i, j)[2] == 0)) {
                    img.put(i, j, 255, 255, 255);
                    counter++;
                } else {
                    img.put(i, j, 0, 0, 0);
                }
            }
        }
        counter = (int) (((double) counter) / (img.rows() * img.cols()) * 100);

        return counter;
    }
}

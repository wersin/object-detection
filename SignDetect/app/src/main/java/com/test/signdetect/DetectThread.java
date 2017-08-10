package com.test.signdetect;

import android.app.ActivityManager;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 * Created by mark on 28/07/17.
 * this was just a test to draw the found object in a separate thread to leave hte main process
 * The problem of this is explained in the SignDetection Activity
 */

public class DetectThread implements Runnable {

    private Mat mRGBA;

    public DetectThread(Mat mRGBA) {
        this.mRGBA = mRGBA;
    }

    public void run() {
        Imgproc.rectangle(mRGBA, new Point(50, 50),
                new Point(100, 100),
                new Scalar(255, 0, 0), 5);
    }
}

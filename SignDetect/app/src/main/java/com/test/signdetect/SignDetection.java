package com.test.signdetect;

/**
 * Created by bley on 23.07.2017.
 * This Activity controls the camera, loads the classifier to find specified sign and draw a
 * rectangle if something is found
 * */

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignDetection extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    JavaCameraView jcv;
    Mat mRgba;
    Mat gray;
    /**
     * the idea was to be able to find more than one object at a time
     * you need one CascadeClassifier object for each different Real Life Object you want to
     * detect
     * for some reason the amount of time it takes to process the image and to render the rectangle
     * is too long so only one classifier at a time can be used
     * */
    private CascadeClassifier thirty_cascade;
    private CascadeClassifier forty_cascade;
    private CascadeClassifier fifty_cascade;
    private CascadeClassifier sixty_cascade;
    private CascadeClassifier onetwenty_cascade;
    BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    jcv.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                }
            }
        }
    };

    /**
     * initialize OpenCV this lines MUST be included
     * */
    static {
        if (OpenCVLoader.initDebug())
            Log.d("OpenCV", "success");
        else
            Log.d("OpenCV", "failed");
    }

    private boolean[] speeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        speeds = intent.getBooleanArrayExtra(MainActivity.SPEEDS);

        setContentView(R.layout.activity_sign_detection);
        jcv = (JavaCameraView) findViewById(R.id.java_camera_view);
        jcv.setVisibility(SurfaceView.VISIBLE);
        jcv.setCvCameraViewListener(this);

        //loads all the cascades
        thirty_cascade = loadCascade("cascade30.xml");
        forty_cascade = loadCascade("cascade40.xml");
        fifty_cascade = loadCascade("cascade50.xml");
        sixty_cascade = loadCascade("cascade60.xml");
        onetwenty_cascade = loadCascade("cascade120.xml");

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (jcv != null)
            jcv.disableView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jcv != null)
            jcv.disableView();
    }

    //if app comes back from background it resumes the camera feed
    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "success");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else {
            Log.d("OpenCV", "failed");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(width, height, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    Runnable rn = new DetectThread(mRgba);

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        gray = new Mat();

        //try out different gray scales and blur settings to bring forth the contours
        /*
        Imgproc.cvtColor(mRgba, gray, Imgproc.COLOR_BGR2GRAY);

        Mat imageBlur = new Mat();
        Mat imageA = new Mat();
        Imgproc.medianBlur(gray, gray, 5);

        Imgproc.GaussianBlur(gray, imageBlur, new Size(3, 3), 100);
        Imgproc.adaptiveThreshold(imageBlur, imageA, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 7, 5);
        */

        /**
         * if object detection is run in a thread it takes too long to detect the object and to draw
         * on the actual frame since a new one will already be displayed.
         */
        /*
        if (speeds[0]) {
            new Thread(rn).start();
        }
        */

        if (speeds[0])
            detectAndDisplay(thirty_cascade, mRgba, new Scalar(255, 0, 0));
        if (speeds[1])
            detectAndDisplay(forty_cascade, gray, new Scalar(212, 14, 228));
        if (speeds[2])
            detectAndDisplay(fifty_cascade, gray, new Scalar(0, 255, 0));
        if (speeds[3])
            detectAndDisplay(sixty_cascade, gray, new Scalar(0, 0, 255));
        if (speeds[4])
            detectAndDisplay(onetwenty_cascade, gray, new Scalar(26, 241, 241));


        return mRgba;
    }

    /**
     * loads specified classifier file form the resource folder
     * */
    public CascadeClassifier loadCascade(String file_name){
        CascadeClassifier cc = null;
        try {
            /**
             * read everything into a file buffer then load it into a CascadeClassifier
             * */
            InputStream is = getResources().openRawResource(R.raw.cascade);
            File cascadeDir = getDir("raw", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, file_name);
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();


            cc = new CascadeClassifier(mCascadeFile.getAbsolutePath());
            cc.load(mCascadeFile.getAbsolutePath());


            if(cc.empty())
            {
                Log.v("MyActivity","--(!)Error loading A\n");
                return null;
            }
            else
            {
                Log.v("MyActivity",
                        "Loaded cascade classifier from " + mCascadeFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("MyActivity", "Failed to load cascade. Exception thrown: " + e);
        }
        return  cc;
    }

    /**
     * This function takes the classifier the actual frame and the Color for this specific Sign
     * it goes applies the classifier to the current frame and if at least one object is found
     * it will be stored in the MatOfRect signs array. Then you find the middle of the square and
     * draw accordingly to the OpenCv draw Rectangle function a rectangle around the found object(s)
     * */
    private void detectAndDisplay(CascadeClassifier cc, Mat frame, Scalar color ) {
        MatOfRect signs = new MatOfRect();

        cc.detectMultiScale(frame, signs, 1.3, 5, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(200, 200), new Size(300, 300));

            Rect[] sings_ar = signs.toArray();
            if (sings_ar.length > 0) {

                double h = sings_ar[0].height * 0.5;
                double w = sings_ar[0].width * 0.5;
                double radius = Math.sqrt(h * h + w * w);

                Log.d("signs length", "length of arr: " + radius);
                Imgproc.rectangle(mRgba, new Point(sings_ar[0].x, sings_ar[0].y),
                        new Point(sings_ar[0].x + sings_ar[0].width, sings_ar[0].y + sings_ar[0].height),
                        color, 5);
            }

    }


}

package com.test.signdetect;

/**
 * Created by bley on 23.07.2017.
 * This is the Main Activity of the Sign Detect App.
 * It handles the selected Classifier and launches a new Activity if required
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    public static final String SPEEDS = "SPEEDS";
    JavaCameraView jcv;
    Mat mRgba;
    private RequestQueue requestQueue;

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

    static {
        if (OpenCVLoader.initDebug())
            Log.d("OpenCV", "success");
        else
            Log.d("OpenCV", "failed");
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this); // 'this' is the Context

        /***
         * Buttons in Main Activity View
         */
        Button button_camera = (Button) findViewById(R.id.start_camera);
        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v);
            }
        });

        Button button_sped = (Button) findViewById(R.id.speed);
        button_sped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpeedMeter(v);
            }
        });

        //tries to send a json File to Server
        Button button_send = (Button) findViewById(R.id.send_speed);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendJsonSpeed();
            }
        });

    }

    /**
     * when everything is ready user may start the camera with the desired classifier
     * in this Intent the speeds (the selected classifier) are passed onto the Activity which
     * handles the camera
     * */
    public void openCamera(View view) {
        Intent intent = new Intent(this, SignDetection.class);
        intent.putExtra(SPEEDS, speeds);
        startActivity(intent);
    }

    public void openSpeedMeter(View v) {
        Intent intent = new Intent(this, Speed.class);
        startActivity(intent);
    }

    /**
     * its used to store the classifier selected by the user
     * 30, 40, 50, 60, 120
     * */
    private boolean[] speeds = new boolean[5];

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_thirty:
                if (checked)
                    speeds[0] = true;
                else
                    speeds[0] = false;
                break;
            case R.id.checkbox_forty:
                if (checked)
                    speeds[1] = true;
                else
                    speeds[1] = false;
                break;
            case R.id.checkbox_fifty:
                if (checked)
                    speeds[2] = true;
                else
                    speeds[2] = false;
                break;
            case R.id.checkbox_sixty:
                if (checked)
                    speeds[3] = true;
                else
                    speeds[3] = false;
                break;
            case R.id.checkbox_one_twenty:
                if (checked)
                    speeds[4] = true;
                else
                    speeds[4] = false;
                break;
        }
    }

    /**
     * this is sending just some test values to test whether the server is running
     * */
    private void sendJsonSpeed() {
        //Toast.makeText(this, "Sending", Toast.LENGTH_SHORT).show();
        //final String url = "131.159.211.132:9080/maxSpeed";
        //final String url = "131.159.201.46:8745";
        Map<String, String> jsonParams = new HashMap<String, String>();

        jsonParams.put("speedMax", "20");
        jsonParams.put("username", "user");
        jsonParams.put("password", "pass");

        try {
            URL url = new URL("http://131.159.201.46:8745"); //in the real code, there is an ip and a port

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.getInputStream();

            Log.v("JSON", "test");

            JSONObject jsonParam = new JSONObject();
            jsonParam.put("uname", 20);

            Log.v("JSON", "test " + jsonParam.toString());

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            conn.connect();
            conn.disconnect();
        } catch (Exception e) {

        }
    } //sendJsonSpeed

}

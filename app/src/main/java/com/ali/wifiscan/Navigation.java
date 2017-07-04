package com.ali.wifiscan;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.content.Context;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.ali.wifiscan.R.id.textView;

import static java.security.AccessController.getContext;

/**
 * Created by ALI on 31-May-17.
 */

public class Navigation extends AppCompatActivity implements SensorEventListener {

    //Step Counter Stuff
    SensorManager sensorManager;
    Sensor aSensor;
    boolean flag = true;
    float steps;
    float temp;
    int temp2;

    //Compass Stuff
    float[] mGravity;
    float[] mGeomagnetic;
    float azimut = 0f;

    SensorManager mSensorManager;
    Sensor mSensor;

    ImageView arrow;
    ImageView DCEFloor;
    private float currentDegree = 0f;
    int self = Animation.RELATIVE_TO_SELF;


    int y;
    int x;

    //TextView reading;

    String Floor;
    String currentLocation;
    String destinationLocation;
    ImageView destinationMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toast.makeText(getApplicationContext(), "You can Fine Tune your location by touching or draging the pointer to your precise location" ,

                Toast.LENGTH_LONG).show();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        arrow = (ImageView) findViewById(R.id.imageView);

        DCEFloor = (ImageView) findViewById(R.id.imageView2);
        Bundle bundle = getIntent().getExtras();
        Floor = bundle.getString("key1");
        currentLocation = bundle.getString("key2");
        destinationLocation = bundle.getString("key3");
        //reading = (TextView) findViewById(R.id.textView5);
        destinationMarker = (ImageView) findViewById(R.id.imageView3);



        if(Floor.equals("Top")){
            DCEFloor.setImageResource(R.drawable.top_final);
        }
        else if(Floor.equals("Bottom")){
            DCEFloor.setImageResource(R.drawable.bottom_final);
        }
        else {
            Toast.makeText(getApplicationContext(), "Error" + Floor,

            Toast.LENGTH_SHORT).show();
        }
        setDestinationMarker();
        setCurrentMarker();


        //final TextView coor = (TextView) findViewById(textView6);
        final View touchView = findViewById(R.id.touchID);
        touchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    //oor.setText("Touch coordinates : " +
                            //String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
                    int y = Math.round(event.getY()) - arrow.getTop();
                    arrow.setTop(Math.round(event.getY()));
                    arrow.setBottom(arrow.getBottom() + y);

                    int x = Math.round(event.getX()) - arrow.getLeft();
                    arrow.setLeft(Math.round(event.getX()));
                    arrow.setRight(arrow.getRight() + x);
                    return true;

            }
        });


    }

    public void setDestinationMarker() {
        switch(Floor) {
            case "Bottom":
            switch (destinationLocation) {
                case "Enterance":
                    destinationMarker.setY(606);
                    destinationMarker.setX(266);
                    break;
                case "Reception":
                    destinationMarker.setY(614);
                    destinationMarker.setX(438);
                    break;
                case "DIP Lab":
                    destinationMarker.setY(441);
                    destinationMarker.setX(367);
                    break;
                case "DSP Lab":
                    destinationMarker.setY(764);
                    destinationMarker.setX(348);
                    break;
                case "Faculty Conference Room":
                    destinationMarker.setY(317);
                    destinationMarker.setX(340);
                    break;
                case "PA to HOD":
                    destinationMarker.setY(918);
                    destinationMarker.setX(351);
                    break;
                case "ERP":
                    destinationMarker.setY(785);
                    destinationMarker.setX(460);
                    break;
                case "Networks Lab":
                    destinationMarker.setY(447);
                    destinationMarker.setX(455);
                    break;
                case "Electronics Lab":
                    destinationMarker.setY(921);
                    destinationMarker.setX(439);
                    break;
                case "FYP Room":
                    destinationMarker.setY(228);
                    destinationMarker.setX(474);
                    break;
                case "Faculty Offices Bottom Floor Right":
                    destinationMarker.setY(1074);
                    destinationMarker.setX(408);
                    break;
                case "Faculty Offices Bottom Floor Left":
                    destinationMarker.setY(97);
                    destinationMarker.setX(341);
                    break;
                case "Top Floor":
                    final View view = findViewById(R.id.touchID);
                    view.getViewTreeObserver().addOnGlobalLayoutListener(
                            new ViewTreeObserver.OnGlobalLayoutListener() {
                                @Override
                                public void onGlobalLayout() {
                                    // Layout has happened here.
                                    //reading.setText(Float.toString(arrow.getY()));
                                    if(arrow.getTop() < 500){
                                        destinationMarker.setY(174);
                                        destinationMarker.setX(335);
                                    }
                                    else if (arrow.getTop() >= 500 && arrow.getTop() < 950){
                                        destinationMarker.setY(610);
                                        destinationMarker.setX(502);
                                    }
                                    else {
                                        destinationMarker.setY(979);
                                        destinationMarker.setX(342);
                                    }
                                    // Don't forget to remove your listener when you are done with it.
                                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                }
                            });
                    break;
            }
                break;

            case "Top":
                switch (destinationLocation) {
                    case "Faculty Cabins":
                        destinationMarker.setY(606);
                        destinationMarker.setX(266);
                        break;
                    case "ECR":
                        destinationMarker.setY(441);
                        destinationMarker.setX(367);
                        break;
                    case "CRC-11":
                        destinationMarker.setY(764);
                        destinationMarker.setX(348);
                        break;
                    case "Admin Office":
                        destinationMarker.setY(918);
                        destinationMarker.setX(351);
                        break;
                    case "CRC-14":
                        destinationMarker.setY(785);
                        destinationMarker.setX(460);
                        break;
                    case "CRC-15":
                        destinationMarker.setY(447);
                        destinationMarker.setX(455);
                        break;
                    case "CRC-13":
                        destinationMarker.setY(921);
                        destinationMarker.setX(439);
                        break;
                    case "CRC-16":
                        destinationMarker.setY(228);
                        destinationMarker.setX(474);
                        break;
                    case "Computation (CTC) Lab":
                        destinationMarker.setY(1074);
                        destinationMarker.setX(408);
                        break;
                    case "Faculty Offices":
                        destinationMarker.setY(97);
                        destinationMarker.setX(341);
                        break;
                    case "Bottom Floor":

                        final View view = findViewById(R.id.touchID);
                        view.getViewTreeObserver().addOnGlobalLayoutListener(
                                new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        //reading.setText(Float.toString(arrow.getY()));
                                        // Layout has happened here.
                                        if(arrow.getTop() < 500){
                                            destinationMarker.setY(174);
                                            destinationMarker.setX(335);
                                        }
                                        else if (arrow.getTop() >= 500 && arrow.getTop() < 950){
                                            destinationMarker.setY(610);
                                            destinationMarker.setX(502);
                                        }
                                        else {
                                            destinationMarker.setY(979);
                                            destinationMarker.setX(342);
                                        }
                                        // Don't forget to remove your listener when you are done with it.
                                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                    }
                                });


                        break;
                }
                break;

        }

    }

    public void setCurrentMarker() {


            final View view = findViewById(R.id.touchID);
            view.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            switch(Floor) {
                                case "Bottom":
                                    switch (currentLocation) {
                                        case "Enterance":
                                            int y = 606 - arrow.getTop();
                                            arrow.setTop(606);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            int x = 266 - arrow.getLeft();
                                            arrow.setLeft(266);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "Reception":
                                            y = 614 - arrow.getTop();
                                            arrow.setTop(614);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 438 - arrow.getLeft();
                                            arrow.setLeft(438);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "DIP Lab":
                                            y = 441 - arrow.getTop();
                                            arrow.setTop(441);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 367 - arrow.getLeft();
                                            arrow.setLeft(367);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "DSP Lab":
                                            y = 764 - arrow.getTop();
                                            arrow.setTop(764);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 348 - arrow.getLeft();
                                            arrow.setLeft(348);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "Faculty Conference Room":
                                            y = 317 - arrow.getTop();
                                            arrow.setTop(317);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 340 - arrow.getLeft();
                                            arrow.setLeft(340);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "PA to HOD":
                                            y = 918 - arrow.getTop();
                                            arrow.setTop(918);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 351 - arrow.getLeft();
                                            arrow.setLeft(351);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "ERP":
                                            y = 785 - arrow.getTop();
                                            arrow.setTop(785);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 460 - arrow.getLeft();
                                            arrow.setLeft(460);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "Networks Lab":
                                            y = 447 - arrow.getTop();
                                            arrow.setTop(447);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 455 - arrow.getLeft();
                                            arrow.setLeft(455);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "Electronics Lab":
                                            y = 921 - arrow.getTop();
                                            arrow.setTop(921);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 439 - arrow.getLeft();
                                            arrow.setLeft(439);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "FYP Room":
                                            y = 228 - arrow.getTop();
                                            arrow.setTop(228);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 474 - arrow.getLeft();
                                            arrow.setLeft(474);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "Faculty Offices Bottom Floor Right":
                                            y = 1074 - arrow.getTop();
                                            arrow.setTop(1074);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 408 - arrow.getLeft();
                                            arrow.setLeft(408);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;

                                        case "Faculty Offices Bottom Floor Left":
                                            y = 97 - arrow.getTop();
                                            arrow.setTop(97);
                                            arrow.setBottom(arrow.getBottom() + y);

                                            x = 341 - arrow.getLeft();
                                            arrow.setLeft(341);
                                            arrow.setRight(arrow.getRight() + x);
                                            break;
                                        case "Top Floor":

                                            break;
                                    }

                                    break;

                                    case "Top":
                                        switch (currentLocation) {
                                            case "Faculty Cabins":
                                                int y = 606 - arrow.getTop();
                                                arrow.setTop(606);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                int x = 266 - arrow.getLeft();
                                                arrow.setLeft(266);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "ECR":
                                                y = 441 - arrow.getTop();
                                                arrow.setTop(441);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 367 - arrow.getLeft();
                                                arrow.setLeft(367);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "CRC-11":
                                                y = 764 - arrow.getTop();
                                                arrow.setTop(764);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 348 - arrow.getLeft();
                                                arrow.setLeft(348);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "Admin Office":
                                                y = 918 - arrow.getTop();
                                                arrow.setTop(918);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 351 - arrow.getLeft();
                                                arrow.setLeft(351);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "CRC-14":
                                                y = 785 - arrow.getTop();
                                                arrow.setTop(785);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 460 - arrow.getLeft();
                                                arrow.setLeft(460);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "CRC-15":
                                                y = 447 - arrow.getTop();
                                                arrow.setTop(447);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 455 - arrow.getLeft();
                                                arrow.setLeft(455);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "CRC-13":
                                                y = 921 - arrow.getTop();
                                                arrow.setTop(921);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 439 - arrow.getLeft();
                                                arrow.setLeft(439);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "CRC-16":
                                                y = 228 - arrow.getTop();
                                                arrow.setTop(228);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 474 - arrow.getLeft();
                                                arrow.setLeft(474);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "Computation (CTC) Lab":
                                                y = 1074 - arrow.getTop();
                                                arrow.setTop(1074);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 408 - arrow.getLeft();
                                                arrow.setLeft(408);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                            case "Faculty Offices":
                                                y = 97 - arrow.getTop();
                                                arrow.setTop(97);
                                                arrow.setBottom(arrow.getBottom() + y);

                                                x = 341 - arrow.getLeft();
                                                arrow.setLeft(341);
                                                arrow.setRight(arrow.getRight() + x);
                                                break;
                                        }

                                    break;
                            }


                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });



    }





    @Override
        protected void onResume() {
            super.onResume();

            aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (aSensor != null) {
                sensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_UI);
            } else {
                Toast.makeText(getApplicationContext(), "No Step Counter Sensor Detected", Toast.LENGTH_LONG).show();
            }

            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (mSensor != null) {
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
            } else {
                Toast.makeText(getApplicationContext(), "No Orientation Sensor Detected", Toast.LENGTH_LONG).show();
            }

            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mSensor != null) {
                mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
            } else {
                Toast.makeText(getApplicationContext(), "No Accelerometer Sensor Detected", Toast.LENGTH_LONG).show();
            }


        }

    @Override
        protected void onPause() {

            super.onPause();
            mSensorManager.unregisterListener(this);
        }


        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            if (sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                if (flag == true) {
                    steps = sensorEvent.values[0];
                    flag = false;
                }
                temp = sensorEvent.values[0] - steps;
                temp2 = Math.round(temp);
                double var = Math.cos(Math.toRadians(currentDegree));
                var = var*14.293;
                float var2 = (float) var;
                double var3 = Math.sin(Math.toRadians(currentDegree));
                var3 = var3 * 14.293;
                float var4 = (float) var3;

                int var5 = Math.round(var2);
                int var6 = Math.round(var4);
                arrow.setTop(arrow.getTop() -  var5);
                arrow.setBottom(arrow.getBottom() - var5);
                arrow.setRight(arrow.getRight() +  var6);
                arrow.setLeft(arrow.getLeft() + var6);
                //reading.setText(currentLocation + " " + destinationLocation);

            }


            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                mGravity = sensorEvent.values;
            if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mGeomagnetic = sensorEvent.values;
            if (mGravity != null && mGeomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];

                boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
                if (success) {
                    float orientation[] = new float[3];

                    SensorManager.getOrientation(R, orientation);
                    azimut = orientation[0];
                }


                azimut = azimut * 60;
                azimut = (float) (int) azimut;
                if (flag == true) {
                    x = self;
                    y = self;
                    flag = false;
                }


                RotateAnimation ra = new RotateAnimation(currentDegree, azimut, self, 0.5f,
                        self, 0.5f);

                ra.setDuration(1000);

                ra.setFillAfter(true);

                arrow.startAnimation(ra);

                currentDegree = azimut;

            }



        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }



}



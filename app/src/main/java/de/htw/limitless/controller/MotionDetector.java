package de.htw.limitless.controller;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MotionDetector implements SensorEventListener {

    private static Context appContext;
    private static MotionDetector instance;
    private ChangeListener listener;

    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private float[] mGyroscopeData = new float[3];
    private int count = 0;

    private static final String TILTED_ONCE = "tilted once vertically";
    private static final String TILT_HORIZONTALLY = "tilting horizontally";
    private static final String ROTATE_180 = "rotated 180 degree";

    private MotionDetector() {
        appContext = Game.getAppContext();
        mSensorManager = (SensorManager) appContext.getSystemService(
                Context.SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    public static MotionDetector getInstance() {
        if (instance == null) {
            instance = new MotionDetector();
        }
        return instance;
    }

    public interface ChangeListener {
        public void onChanged(String motion);
    }

    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void detectStarted() {
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void detectPaused() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values == null || listener == null) {
            throw new RuntimeException("No listener for MotionDetector");
        } else {
            if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
                mGyroscopeData = event.values.clone();

                float x = mGyroscopeData[0];
                if (x > 1.0f || x < -1.0f) {
                    listener.onChanged(TILTED_ONCE);
                }

                float y = mGyroscopeData[1];
                if (y > 1.0f) {
                    count++;
                } else if (y < -1.0f) {
                    count++;
                }

                if (count >= 10) {
                    count = 0;
                    listener.onChanged(TILT_HORIZONTALLY);
                }

                float z = mGyroscopeData[2];
                if (z > 2.0f || z < -2.0f) {
                    listener.onChanged(ROTATE_180);
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

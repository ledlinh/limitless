package de.htw.limitless;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.htw.limitless.controller.MotionDetector;

public class MotionDetectorTest {

    public DummyDetector motionDetector;
    public DummyListener listener;
    public float[] mockValues;
    private static final String TILTED_ONCE = "tilted once vertically";
    private static final String TILT_HORIZONTALLY = "tilting horizontally";
    private static final String ROTATE_180 = "rotated 180 degree";

    class DummyListener implements MotionDetector.ChangeListener {

        private String motion;

        @Override
        public void onChanged(String motion) {
            this.motion = motion;
        }

        public String getMotion() {
            return this.motion;
        }
    }

    interface DummyDetector {
        public void onSensorChanged(float[] gyroscopeData);
        public void setChangeListener(DummyListener listener);
    }

    @Before
    public void setUp() {
        listener = new DummyListener();
        motionDetector = new DummyDetector() {

            public DummyListener listener;
            private int count;

            @Override
            public void onSensorChanged(float[] mGyroscopeData) {

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

            @Override
            public void setChangeListener(DummyListener listener) {
                this.listener = listener;
            }
        };

        motionDetector.setChangeListener(listener);
    }


    @Test
    public void motionTestTiltedOnce() {
        mockValues = new float[3];

        mockValues[0] = 1.5f;
        mockValues[1] = mockValues[2] = 0;
        motionDetector.onSensorChanged(mockValues);
        Assert.assertEquals(TILTED_ONCE, listener.getMotion());
    }

    @Test
    public void motionTestTiltingHorizontally() {
        mockValues = new float[3];
        mockValues[0] = mockValues[2] = 0;
        mockValues[1] = 1.2f;

        for (int i = 0; i <= 5; i++) {
            motionDetector.onSensorChanged(mockValues);
        }

        mockValues[1] = -1.2f;
        for (int i = 0; i <= 5; i++) {
            motionDetector.onSensorChanged(mockValues);
        }

        Assert.assertEquals(TILT_HORIZONTALLY, listener.getMotion());
    }

    @Test
    public void motionTestRotation() {
        mockValues = new float[3];
        mockValues[0] = mockValues[1] = 0;
        mockValues[2] = 2.1f;

        motionDetector.onSensorChanged(mockValues);
        Assert.assertEquals(ROTATE_180, listener.getMotion());
    }

}

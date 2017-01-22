package zhexian.app.myapplication;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 陀螺仪传感器监听
 * Created by 陈俊杰 on 2015/12/8.
 */
public class GyroScopeSensorListener implements SensorEventListener {
    public Context context;
    private SensorManager sensorManager;
    private Sensor sensor;
    // 手机上一个位置时重力感应坐标
    private ISensorListener sensorListener;

    public GyroScopeSensorListener(Context context) {
        this.context = context;
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager == null)
            return;

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    public void setSensorListener(ISensorListener sensorListener) {
        this.sensorListener = sensorListener;
    }

    public void start() {
        if (sensor == null)
            return;

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        if (sensor == null)
            return;

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[SensorManager.DATA_X];
        float y = event.values[SensorManager.DATA_Y];

        //为什么要互换 x,y；是因为陀螺仪在y轴上的左右倾斜数据，最终会反应在图片在水平位置上的变化；同理x轴上下倾斜反应在垂直位置上
        //为调用端统一坐标，我们在这里就做了调换，调用端按照正常的坐标系使用即可
        if (sensorListener != null)
            sensorListener.onGyroScopeChange(y, x);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 坐标参考http://www.cnblogs.com/octobershiner/archive/2011/11/06/2237880.html
     */
    public interface ISensorListener {
        /**
         * @param horizontalShift 在X轴上的加速度，用于计算垂直位移
         * @param verticalShift   在Y轴上的加速度，用于计算水平位移
         */
        void onGyroScopeChange(float horizontalShift, float verticalShift);
    }
}

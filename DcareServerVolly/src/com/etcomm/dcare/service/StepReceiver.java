package com.etcomm.dcare.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import me.chunyu.pedometerservice.IntentConsts;

/**
 * Created by Hello on 2016/8/26.
 */
public class StepReceiver extends BroadcastReceiver {
    private String action;


    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        doAfterBroadcast(intent);
    }

    private void doAfterBroadcast(Intent intent) {
        switch (action) {
            // 计步传感器的步数增加广播
            case IntentConsts.STEP_COUNTER_SENSOR_VALUE_FILTER:
                // 计步传感器的步数
                int mCYStepCounterSensorValue = intent.getIntExtra(IntentConsts.CY_STEP_SENSOR_VALUE_EXTRA, 0);
                // 计步传感器的时间
                long mSCSMotionTime = intent.getLongExtra(IntentConsts.MOTION_TIME_STEP_SENSOR_EXTRA, 0L);
                break;

            // 加速度传感器的步数增加广播
            case IntentConsts.ACCELERATE_SENSOR_VALUE_FILTER:
                // 加速度传感器的步数
                int mCYAccelerateSensorValue = intent.getIntExtra(IntentConsts.CY_ACCELERATE_SENSOR_VALUE_EXTRA, 0);
                // 加速度传感器的时间
                long mASMotionTime = intent.getLongExtra(IntentConsts.MOTION_TIME_ACCELERATE_EXTRA, 0L);
                break;
            // ...
        }
    }
}

package com.etcomm.dcare.widget;

import com.etcomm.dcare.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

public class DcareSwitchPreference extends SwitchPreference {
    private static final String CLASS_TAG = DcareSwitchPreference.class.getSimpleName();

    private Switch mSwitch;
    private boolean mChecked = false;
    private Context mContext;

    // // ///////////////////////////////////////////Custom Listenr Start
    // private OnRadioButtonCheckedListener mOnRadioButtonCheckedListener;
    //
    // public interface OnRadioButtonCheckedListener {
    // public void OnRadioButtonChecked(boolean isScreenOffView);
    // }
    //
    // public void setOnRadioButtonCheckedListener(OnRadioButtonCheckedListener
    // listener) {
    // mOnRadioButtonCheckedListener = listener;
    // }
    //
    // // ///////////////////////////////////////////Custom Listenr End

    public DcareSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
//        setWidgetLayoutResource(R.layout.dcare_switch);
    }

    public DcareSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //通过调用setWidgetLayoutResource方法来更新preference的widgetLayout,即更新控件区域
//        setWidgetLayoutResource(R.layout.dcare_switch);
    }

    public DcareSwitchPreference(Context context) {
        super(context);
        mContext = context;
        //通过调用setWidgetLayoutResource方法来更新preference的widgetLayout,即更新控件区域
//        setWidgetLayoutResource(R.layout.dcare_switch);
    }
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    protected void onBindView(View view) {
//        mSwitch = (Switch) view.findViewById(R.id.prefrence_switch_id);
//        //view即是代表的preference整个区域,可以对该view进行事件监听,也就是实现了preference整个区域的点击事件
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                showToast("section-all");
//                //此处调用自定义的监听器A方法,该监听器A接口应由使用GestureSwitchPreference的类来实现,从而实现
//                //preference整个区域的点击事件.注:监听器A的定义可以参考OnRadioButtonCheckedListener接口的定义
//            }
//        });
//
//        //switch开关的点击事件
//        if (mSwitch != null) {
//            mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//                @Override
//                public void onCheckedChanged(CompoundButton button, boolean checked) {
//                    mChecked = checked;
//                    showToast("only-switch-section");
//                    //此处调用自定义的监听器B方法,该监听器B接口应由使用GestureSwitchPreference的类来实现,从而实现
//                    //preference的switch点击事件.注:监听器B的定义可以参考OnRadioButtonCheckedListener接口的定义
//                }
//            });
//        }
//        setChecked(mChecked);
        Log.i(CLASS_TAG, "onBindView");
//		if (view instanceof ViewGroup) {
        ViewGroup vg = (ViewGroup) view.findViewById(android.R.id.widget_frame);
//			ViewGroup vg = (ViewGroup) view;
        for (int i = 0; i < vg.getChildCount(); i++) {
            View checkableView = vg.getChildAt(i);

            // View checkableView =
            // view.findViewById(com.android.internal.R.id.switchWidget);

            if (checkableView != null && checkableView instanceof Checkable) {
                if (checkableView instanceof Switch) {
                    final Switch switchView = (Switch) checkableView;
                    switchView.setOnCheckedChangeListener(null);
                    //                android:thumb="@drawable/switch_rank_thumb"
                    //		          android:track="@drawable/switch_rank_track"
                    Log.i(CLASS_TAG, "onBindView  switchView");
                    if (Build.VERSION.SDK_INT < 21) {
                        switchView.setThumbDrawable(
                                mContext.getResources().getDrawable(R.drawable.switch_rank_thumb));
                        switchView.setTrackDrawable(
                                mContext.getResources().getDrawable(R.drawable.switch_rank_track));
                    }else{
                        Log.i(CLASS_TAG, "onBindView  switchView  setTrackDrawable");
                        switchView.setTrackDrawable(mContext.getResources().getDrawable(R.drawable.switch_rank_track,null));
                        switchView.setThumbDrawable(mContext.getResources().getDrawable(R.drawable.switch_rank_thumb,null));
                    }
                }

//					((Checkable) checkableView).setChecked(mChecked);
//
//					if (checkableView instanceof Switch) {
//						final Switch switchView = (Switch) checkableView;
////						switchView.setTextOn(mSwitchOn);
////						switchView.setTextOff(mSwitchOff);
////						switchView.setOnCheckedChangeListener(mListener);
//					}
            }
        }
//		}
        super.onBindView(view);
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean bChecked) {
        mChecked = bChecked;
        if (mSwitch != null) {
            mSwitch.setChecked(bChecked);
        }
    }

    private void showToast(String info) {
        Toast mToast = null;
        if (mToast == null) {
            mToast = Toast.makeText(mContext, info, 5000);
        }
        mToast.setText(info);
        mToast.show();
    }
}

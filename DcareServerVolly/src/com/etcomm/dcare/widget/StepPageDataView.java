package com.etcomm.dcare.widget;

import android.animation.TypeEvaluator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.TrendRecordActivity;
import com.etcomm.dcare.app.model.PedometerItem;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.service.StepDataUploadService;

public class StepPageDataView extends RelativeLayout {
    public class MyTypeEvaluator implements TypeEvaluator<Integer> {
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            // TODO Auto-generated method stub
            Log.i("CircleSeekBar", "endValue: " + endValue);
            return (int) (endValue * fraction);
        }

    }

    protected static final String tag = null;
    /**
     * CY记步传感器步数
     */
    private int stepCount;
    /**
     * CY记步传感器运动时间
     */
    private float runDuration;
    CircleSeekBar circleseekbar;
    TextView tv_date;
    TextView tv_stepcount;
    TextView tv_recomstepcount;
    View v;
    LinearLayout time_step_target_li;
    private Context mContext;
    private PedometerItem mPedometer;
    private int mSuggestSteps = 10000;
    private boolean isToday = false;

    public StepPageDataView(Context context) {
        super(context);
        this.mContext = context;
        initView(mContext);
    }

    private void initView(Context context) {
        // TODO Auto-generated method stub
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        LayoutInflater mInflater = LayoutInflater.from(context);
        v = mInflater.inflate(R.layout.page_item_main, null);
        circleseekbar = (CircleSeekBar) v.findViewById(R.id.circleseekbar);
        tv_date = (TextView) v.findViewById(R.id.tv_date);
        tv_stepcount = (TextView) v.findViewById(R.id.tv_stepcount);
        time_step_target_li = (LinearLayout) v.findViewById(R.id.time_step_target_li);
        tv_recomstepcount = (TextView) v.findViewById(R.id.tv_recomstepcount);
        initData();
        addView(v, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }


    private void initData() {
        // TODO Auto-generated method stub
        if (mPedometer != null) {
            tv_date.setText(mPedometer.getDate());
            tv_stepcount.setText(mPedometer.getStep());
            Log.i("WalkPageFragmentView", "TodayStep sp : " + mPedometer.getStep());
            if (!StringUtils.isEmpty(mPedometer.getTarget())) {
                tv_recomstepcount.setText(mPedometer.getTarget() + "");
            } else {
                tv_recomstepcount.setText(mSuggestSteps + "");
            }
            if (Float.parseFloat(mPedometer.getStep()) > 0) {
                if (isToday) {
                    circleseekbar.setCurProcess(0);
                } else {
                    circleseekbar.setCurProcess(0);
                }
            }
            if (isToday) {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        Rect delegateArea = new Rect();
                        LinearLayout time_step_target_li = (LinearLayout) findViewById(R.id.time_step_target_li);
                        time_step_target_li.setEnabled(true);
                        time_step_target_li.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // 先上传首页数据
                                Intent service = new Intent(mContext, StepDataUploadService.class);
                                service.putExtra("isexit", true);
                                mContext.startService(service);
                                Intent intent = new Intent(mContext, TrendRecordActivity.class);
                                intent.putExtra("Token", SharePreferencesUtil.getToken(mContext));
                                mContext.startActivity(intent);

                            }
                        });
                        time_step_target_li.getHitRect(delegateArea);
                        Log.i(tag, "delegateArea : " + delegateArea.toString());
                        delegateArea.left -= 200;
                        delegateArea.top -= 100;
                        delegateArea.right += 300;
                        delegateArea.bottom += 100;

                        Log.i(tag, "delegateArea : " + delegateArea.toString());
                        TouchDelegate touchDelegate = new TouchDelegate(delegateArea,
                                time_step_target_li);
                        if (View.class.isInstance(time_step_target_li.getParent())) {
                            ((View) time_step_target_li.getParent()).setTouchDelegate(touchDelegate);
                        }
                    }
                });

            }
        }
    }

    public void setData(PedometerItem pedometerItem, int steps, boolean isday) {
        this.mPedometer = pedometerItem;
        this.mSuggestSteps = steps;
        if (mSuggestSteps <= 0) {
            mSuggestSteps = 10000;
        }
        this.isToday = isday;
        initData();
    }

    /**
     * 暂时保留
     */
//	public void setData(PedometerItem pedometerItem,int steps,boolean isday) {
//		this.mPedometer = pedometerItem;
//		this.mSuggestSteps = steps;
//		if(mSuggestSteps<=0){
//			mSuggestSteps = 10000;
//		}
//		this.isToday = isday;
//
//		initData();
//	}
    public PedometerItem getmPedometer() {
        return mPedometer;
    }

    public void setmPedometer(PedometerItem mPedometer) {
        this.mPedometer = mPedometer;
    }

    public void startProcessAnimation(int mTarget) {
        // TODO Auto-generated method stub
        if (circleseekbar != null) {
            Log.i("CircleSeekBar", "startProcessAnimation");
            circleseekbar.startAnimation(mTarget);
        } else {
            Log.i("CircleSeekBar", "circleseekbar==null");
        }
    }

    public void setCurStep(int blueStep2) {
        // TODO Auto-generated method stub
        if (circleseekbar != null) {
            if (!circleseekbar.isAni()) {
                circleseekbar.setCurProcess((int) (blueStep2 * 100 / mSuggestSteps));
            }
        } else {
            Log.e("CircleSeekBar", "circleseekbar null");
        }
        if (tv_stepcount != null) {
            tv_stepcount.setText(blueStep2+"");
            Log.i("WalkPageFragmentView2", "TodayStep sp : " + blueStep2);
        } else {
            Log.e("CircleSeekBar", "tv_stepcount null");
        }
    }


}

package com.etcomm.dcare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.etcomm.dcare.app.activity.login.LoginActivity;
import com.etcomm.dcare.app.activity.setting.EnterUserNameChooseAvatarActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.utils.LogUtil;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.service.StepDataUploadService;
import com.igexin.sdk.PushManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class SplashActivity extends BaseActivity {
    private static final String tag = "SplashActivity";
    // 引导图片资源
    private static final int[] pics = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};
    private ArrayList<View> views = new ArrayList<View>();
    private SharedPreferences sp;
    private long startTime;
    private long endTime;
    private ImageView flyman, flymanbackground;
    private AlphaAnimation animation;

    // 在开启一个服务之前应该判断该服务知否已经在运行

    private void initData() {
        // TODO Auto-generated method stub
        // 定义一个布局并设置参数
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            // 防止图片不能填满屏幕
            iv.setScaleType(ScaleType.FIT_XY);
            // 加载图片资源
            iv.setImageResource(pics[i]);
            if (i == pics.length - 1) {
                iv.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            sp.edit().putBoolean("isShowedGuide", true).commit();
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        return true;
                    }
                });
            }
            views.add(iv);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // setContentView(R.layout.activity_splash);
        PushManager.getInstance().initialize(this.getApplicationContext());
        // 注册广播
        Log.i(tag, "client_id  : " + client_id);
        startService(new Intent(this, StepDataUploadService.class));
        sp = getPreferences(MODE_PRIVATE);
        boolean isShowedGuide = sp.getBoolean("isShowedGuide", false);
        ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
        RelativeLayout loading = (RelativeLayout) findViewById(R.id.loading);
        flyman = (ImageView) findViewById(R.id.flyman);
        flymanbackground = (ImageView) findViewById(R.id.flymanbackground);
        if (isShowedGuide) {
            loading.setVisibility(View.VISIBLE);
            viewpager.setVisibility(View.GONE);
            Log.e("个人信息是否完整", "" + !StringUtils.isEmpty(SharePreferencesUtil.getToken(mContext)));
            if (SharePreferencesUtil.getOpen(mContext) != true) {
                flyman.setVisibility(View.GONE);
                // 替换企业
                if (SharePreferencesUtil.getImage(mContext) != null && SharePreferencesUtil.getImage(mContext) != "") {
                    ImageLoader.getInstance().displayImage(SharePreferencesUtil.getImage(mContext), flymanbackground);
                } else {
                    flymanbackground.setImageResource(R.drawable.loading_chaoren);
                }
                animation = new AlphaAnimation(1.0f, 1.0f);
                animation.setDuration(1500);
                flymanbackground.setAnimation(animation);
                animation.startNow();
                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!getVersion().equals(SharePreferencesUtil.getVersionInfo(mContext))) {
                            SharePreferencesUtil.saveReData(mContext, true);
                        } else {
                            SharePreferencesUtil.saveReData(mContext, false);
                        }
                        SharePreferencesUtil.saveVersionInfo(mContext, getVersion());
                        if (!StringUtils.isEmpty(SharePreferencesUtil.getToken(mContext))) {
                            if (SharePreferencesUtil.getInfoState(mContext)) {
                                // 信息完整
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashActivity.this, EnterUserNameChooseAvatarActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        SplashActivity.this.finish();

                    }
                });

            } else {
                // 第一次启动
                flyman.setImageResource(R.drawable.flyman);
                flymanbackground.setImageResource(R.drawable.loading_empty);
                final Animation animation = new ScaleAnimation(0.2f, 1.0f, 0.2f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(1500);// 设置动画持续时间
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.setRepeatCount(0);// 设置重复次数
                animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
                animation.setStartOffset(200);// 执行前的等待时间
                SharePreferencesUtil.saveOpen(mContext, false);
                flyman.setAnimation(animation);
                animation.startNow();
                animation.setAnimationListener(new AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (!getVersion().equals(SharePreferencesUtil.getVersionInfo(mContext))) {
                            SharePreferencesUtil.saveReData(mContext, true);
                        } else {
                            SharePreferencesUtil.saveReData(mContext, false);
                        }
                        SharePreferencesUtil.saveVersionInfo(mContext, getVersion());
                        if (!StringUtils.isEmpty(SharePreferencesUtil.getToken(mContext))) {
                            if (SharePreferencesUtil.getInfoState(mContext)) {
                                // 信息完整
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashActivity.this, EnterUserNameChooseAvatarActivity.class);
                                startActivity(intent);
                            }

                        } else {

                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        SplashActivity.this.finish();

                    }
                });
            }

        } else {
            viewpager.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
            initData();
            viewpager.setAdapter(new PagerAdapter() {
                @Override
                public boolean isViewFromObject(View arg0, Object arg1) {
                    return (arg0 == arg1);
                }

                @Override
                public int getCount() {
                    if (views != null) {
                        return views.size();
                    } else
                        return 0;
                }

                /**
                 * 初始化position位置的界面
                 */
                @Override
                public Object instantiateItem(View container, int position) {
                    ((ViewPager) container).addView(views.get(position), 0);
                    return views.get(position);
                }

                /**
                 * 销毁position位置的界面
                 */
                @Override
                public void destroyItem(View container, int position, Object object) {
                    ((ViewPager) container).removeView(views.get(position));
                }
            });
        }
        Log.i(tag, "Uid: " + android.os.Process.myUid() + " Tid: " + android.os.Process.myTid() + " Pid: " + android.os.Process.myPid());
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private String getVersion() {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            LogUtil.e(tag, "version>>>>" + version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

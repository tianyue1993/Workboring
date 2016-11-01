package com.etcomm.dcare.app.activity.setting;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.R;
import com.etcomm.dcare.R.string;
import com.etcomm.dcare.app.activity.login.ChooseComStructureActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.app.widget.WheelView;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.StructureContent;
import com.etcomm.dcare.netresponse.StructureItems;
import com.etcomm.dcare.util.DcareUtils;
import com.etcomm.dcare.util.GetPathFromUri4kitkat;
import com.etcomm.dcare.widget.CircleImageView;
import com.etcomm.dcare.widget.SimpleTitleBar;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * 个人资料设置
 *
 * @author iexpressbox
 */
// @EActivity(R.layout.activitypersonal)
public class SettingPersonalDataActivity extends BaseActivity implements OnClickListener {
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private static final int SEX = 0;
    public static final int PIC = 1;
    public static final int TAKEPHOTO = 11;
    public static final int PICKPHOTO = 12;
    public static final int PICK_PHOTO_FROM_MEIZU = 16;
    public static final int Crop_PICK_PHOTO_FROM_MEIZU = 17;
    private static final int NICKNAME = 2;
    private static final int DEPARTMENT = 3;
    private static final int HEIGHT = 4;
    private static final int WEIGHT = 5;
    private static final int AGE = 6;
    private static final int PICCrop = 13;
    private static final int TAKE_PHOTO = 10;
    private static final int USERNAME = 7;

    TextView age;
    TextView weight;
    TextView height;
    TextView cancel;
    TextView sure;
    LinearLayout layout_wl;
    TextView choosetext;
    WheelView wl_pickerage;
    WheelView wl_pickerweight;
    WheelView wl_pickerheight;
    private boolean isShow = true;

    SimpleTitleBar titlebar;

    RelativeLayout personalavator_rl;

    CircleImageView personalavator_ciriv;

    RelativeLayout personal_nickname_rl, personal_username_rl;

    TextView personal_nickname_tv, personal_username_tv;

    RelativeLayout personal_sex_rl;

    TextView personal_sex_tv, job_number_tv;

    RelativeLayout personal_deapartment_rl;

    TextView personal_deapartment_tv;

    RelativeLayout personal_height_rl;

    TextView personal_height_tv;

    RelativeLayout personal_weight_rl;

    TextView personal_weight_tv;

    RelativeLayout personal_age_rl, job_number_rl;

    ImageView personal_deapartment_imageView1;

    TextView personal_age_tv;
    private String filename;
    protected Bitmap photo;
    protected File mCurrentPhotoFile;
    // private Dialog dialog;
    private Uri photoUri;
    private File mUriFile;
    private String editweight = "50";
    private String editage = "1980";
    private String editheight = "160";
    private SharedPreferences sp;
    // private Login login;
    private static final int CAMERA_PIC = 21;
    private static final int CROP_PIC = 22;
    protected static final int SetStructure = 25;
    private static final int CODE_CAMERA_REQUEST = 30;
    private static final int CODE_RESULT_REQUEST = 33;
    protected static final int ToTakePhoto = 0;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case ToTakePhoto:
                    // doTakePhoto();
                    choseHeadImageFromCameraCapture();
                    // takePic();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(tag);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(tag);
    }

    private void initviewfromlayout() {
        // TODO Auto-generated method stub
        titlebar = (SimpleTitleBar) findViewById(R.id.titlebar);
        personalavator_rl = (RelativeLayout) findViewById(R.id.personalavator_rl);
        job_number_tv = (TextView) findViewById(R.id.job_number_tv);
        personalavator_ciriv = (CircleImageView) findViewById(R.id.personalavator_ciriv);
        personal_nickname_rl = (RelativeLayout) findViewById(R.id.personal_nickname_rl);
        personal_nickname_tv = (TextView) findViewById(R.id.personal_nickname_tv);
        personal_username_tv = (TextView) findViewById(R.id.personal_username_tv);
        personal_sex_rl = (RelativeLayout) findViewById(R.id.personal_sex_rl);
        personal_sex_tv = (TextView) findViewById(R.id.personal_sex_tv);
        personal_deapartment_rl = (RelativeLayout) findViewById(R.id.personal_deapartment_rl);
        personal_deapartment_tv = (TextView) findViewById(R.id.personal_deapartment_tv);
        personal_height_rl = (RelativeLayout) findViewById(R.id.personal_height_rl);
        personal_height_tv = (TextView) findViewById(R.id.personal_height_tv);
        personal_weight_rl = (RelativeLayout) findViewById(R.id.personal_weight_rl);
        personal_weight_tv = (TextView) findViewById(R.id.personal_weight_tv);
        personal_age_rl = (RelativeLayout) findViewById(R.id.personal_age_rl);
        personal_age_tv = (TextView) findViewById(R.id.personal_age_tv);
        job_number_rl = (RelativeLayout) findViewById(R.id.job_number_rl);
        personal_username_rl = (RelativeLayout) findViewById(R.id.personal_username_rl);
        personal_deapartment_imageView1 = (ImageView) findViewById(R.id.personal_deapartment_imageView1);
        age = (TextView) findViewById(R.id.age);
        height = (TextView) findViewById(R.id.height);
        weight = (TextView) findViewById(R.id.weight);
        choosetext = (TextView) findViewById(R.id.choosetext);
        sure = (TextView) findViewById(R.id.sure);
        cancel = (TextView) findViewById(R.id.cancel);
        layout_wl = (LinearLayout) findViewById(R.id.layout_wl);
        wl_pickerage = (WheelView) findViewById(R.id.wl_pickerage);
        wl_pickerheight = (WheelView) findViewById(R.id.wl_pickerheight);
        wl_pickerweight = (WheelView) findViewById(R.id.wl_pickerweight);
    }

    void initView() {
        titlebar.setTitle(R.string.people_data);
        titlebar.setLeftLl(backClickListener);
        sp = getSharedPreferences(Preferences.SignIn, MODE_PRIVATE);
        personal_nickname_tv.setText(AppSharedPreferencesHelper.getNick_name());
        personal_username_tv.setText(AppSharedPreferencesHelper.getReal_name());
        job_number_tv.setText(AppSharedPreferencesHelper.getJob_number());
        personal_sex_tv.setText(AppSharedPreferencesHelper.getGender().equals("1") ? "男" : "女");
        Log.i(tag, "login.getAvatar(): " + AppSharedPreferencesHelper.getAvatar());
        ImageLoader.getInstance().displayImage(AppSharedPreferencesHelper.getAvatar(), personalavator_ciriv);
        personal_deapartment_tv.setText(AppSharedPreferencesHelper.getStructure());
        personal_height_tv.setText(AppSharedPreferencesHelper.getHeight() + "cm");
        personal_weight_tv.setText(AppSharedPreferencesHelper.getWeight() + "kg");
        personal_age_tv.setText(AppSharedPreferencesHelper.getBirth_year() + "年");

        if (SharePreferencesUtil.getIslevel(mContext)) {
            job_number_rl.setVisibility(View.GONE);
            personal_deapartment_imageView1.setVisibility(View.INVISIBLE);
            personal_deapartment_rl.setEnabled(false);
        } else {
            personal_deapartment_imageView1.setVisibility(View.VISIBLE);
            job_number_rl.setVisibility(View.VISIBLE);
            personal_deapartment_rl.setEnabled(true);
        }

        // }
    }

    private void setclickevent() {
        // TODO Auto-generated method stub
        personalavator_rl.setOnClickListener(this);
        personal_username_rl.setOnClickListener(this);
        personal_nickname_rl.setOnClickListener(this);
        personal_sex_rl.setOnClickListener(this);
        personal_deapartment_rl.setOnClickListener(this);
        personal_height_rl.setOnClickListener(this);
        personal_weight_rl.setOnClickListener(this);
        personal_age_rl.setOnClickListener(this);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.personalavator_rl:
                personalavator_rl();
                break;
            case R.id.personal_nickname_rl:
                personal_nickname_rl();
                break;
            case R.id.personal_username_rl:
                personal_username_rl();
                break;
            case R.id.personal_sex_rl:
                personal_sex_rl();
                break;
            case R.id.personal_deapartment_rl:
                personal_deapartment_rl();
                break;
            case R.id.personal_height_rl:
                personal_height_rl();
                break;
            case R.id.personal_weight_rl:
                personal_weight_rl();
                break;
            case R.id.personal_age_rl:
                personal_age_rl();
                break;
            case R.id.sure:
                sure();
                break;
            case R.id.cancel:
                cancel();
                break;
            default:
                break;
        }
    }

    private void cancel() {
        isShow = true;
        layout_wl.setVisibility(View.GONE);
        wl_pickerweight.setSeletion(0);
        wl_pickerage.setSeletion(0);
        wl_pickerheight.setSeletion(0);

    }

    private void sure() {

        if (choosetext.getText().toString().equals("选择年龄")) {
            if (editage != "") {
                editUserInfo("birth_year", editage);
                AppSharedPreferencesHelper.setBirth_year(editage);
                personal_age_tv.setText(editage + "年");
                wl_pickerweight.setSeletion(0);
                wl_pickerage.setSeletion(0);
                wl_pickerheight.setSeletion(0);
            }

        } else if (choosetext.getText().toString().equals("选择身高")) {
            if (editheight != "") {
                editUserInfo("height", editheight);
                AppSharedPreferencesHelper.setHeight(editheight);
                personal_height_tv.setText(editheight + "cm");
                wl_pickerweight.setSeletion(0);
                wl_pickerage.setSeletion(0);
                wl_pickerheight.setSeletion(0);
            }

        } else if (choosetext.getText().toString().equals("选择体重")) {
            if (editweight != "") {
                editUserInfo("weight", editweight);
                AppSharedPreferencesHelper.setWeight(editweight);
                personal_weight_tv.setText(editweight + "kg");
                wl_pickerweight.setSeletion(0);
                wl_pickerage.setSeletion(0);
                wl_pickerheight.setSeletion(0);
            }

        }
        isShow = true;
        layout_wl.setVisibility(View.GONE);
    }

    // 头像设置
    void personalavator_rl() {
        Intent intent = new Intent(this, SelectPicPopupWindowActivity.class);
        startActivityForResult(intent, PIC);
    }

    // 姓名设置
    void personal_nickname_rl() {
        Intent intent = new Intent(this, ChangeNickNameActivity.class);
        intent.putExtra(Preferences.NICK_NAME, AppSharedPreferencesHelper.getNick_name());
        startActivityForResult(intent, NICKNAME);
    }

    void personal_username_rl() {
        Intent intent = new Intent(this, ChangeUserNameActivity.class);
        intent.putExtra(Preferences.USER_NAME, AppSharedPreferencesHelper.getReal_name());
        startActivityForResult(intent, USERNAME);
    }

    // 性别设置
    void personal_sex_rl() {
        Intent intent = new Intent(this, SelectSexPopupWindowActivity.class);
        startActivityForResult(intent, SEX);
    }

    // 部门设置
    void personal_deapartment_rl() {// 部门选择这个已经切换了新的界面 首先加载部门信息，然后
        // Intent intent = new Intent(this,DepartMentActivity.class);
        // startActivityForResult(intent, DEPARTMENT);
        // 获取组织结构
        getSubStructure();
    }

    // 身高设置
    void personal_height_rl() {
        choosetext.setText(string.chooseheight);
        if (isShow) {
            layout_wl.setVisibility(View.VISIBLE);
            wl_pickerage.setVisibility(View.GONE);
            wl_pickerheight.setVisibility(View.VISIBLE);
            wl_pickerweight.setVisibility(View.GONE);
            isShow = false;

            ArrayList<String> heightList = new ArrayList<String>();
            for (int i = 0; i < 111; i++) {
                // 120--230
                int weight = i + 120;
                heightList.add(weight + "");
            }
            wl_pickerheight.setOffset(1);
            wl_pickerheight.setItems(heightList);
            int height = Integer.parseInt(AppSharedPreferencesHelper.getHeight());
            if (height != 0) {
                wl_pickerheight.setSeletion(height - 120);

            } else {
                wl_pickerheight.setSeletion(40);
            }

            wl_pickerheight.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                    editheight = item;
                }
            });
        } else {
            isShow = true;
            wl_pickerweight.setSeletion(0);
            wl_pickerage.setSeletion(0);
            wl_pickerheight.setSeletion(0);
            layout_wl.setVisibility(View.GONE);
        }
    }

    // 体重设置
    void personal_weight_rl() {
        choosetext.setText(string.chooseweight);
        if (isShow) {
            layout_wl.setVisibility(View.VISIBLE);
            wl_pickerage.setVisibility(View.GONE);
            wl_pickerheight.setVisibility(View.GONE);
            wl_pickerweight.setVisibility(View.VISIBLE);
            isShow = false;

            ArrayList<String> weightList = new ArrayList<String>();
            for (int i = 0; i < 276; i++) {
                int weight = i + 25;
                weightList.add(weight + "");
            }
            wl_pickerweight.setOffset(1);
            wl_pickerweight.setItems(weightList);
            int weight = Integer.parseInt(AppSharedPreferencesHelper.getWeight());
            if (weight != 0) {
                wl_pickerweight.setSeletion(weight - 25);
            } else {
                wl_pickerweight.setSeletion(25);
            }

            wl_pickerweight.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                    editweight = item;
                }
            });
        } else {
            isShow = true;
            wl_pickerweight.setSeletion(0);
            wl_pickerage.setSeletion(0);
            wl_pickerheight.setSeletion(0);
            layout_wl.setVisibility(View.GONE);
        }
    }

    // 年龄设置
    void personal_age_rl() {
        choosetext.setText(string.chooseage);
        if (isShow) {
            layout_wl.setVisibility(View.VISIBLE);
            wl_pickerage.setVisibility(View.VISIBLE);
            wl_pickerheight.setVisibility(View.GONE);
            wl_pickerweight.setVisibility(View.GONE);
            isShow = false;
            ArrayList<String> ageList = new ArrayList<String>();
            for (int i = 0; i < 71; i++) {
                int age = i + 1930;
                ageList.add(age + "");
            }
            wl_pickerage.setOffset(1);
            wl_pickerage.setItems(ageList);

            int age = Integer.parseInt(AppSharedPreferencesHelper.getBirth_year());
            if (age != 0) {
                wl_pickerage.setSeletion(age - 1930);
            } else {
                wl_pickerage.setSeletion(50);
            }

            wl_pickerage.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Log.d("", "selectedIndex: " + selectedIndex + ", item: " + item);
                    editage = item;
                }
            });

        } else {
            isShow = true;
            wl_pickerweight.setSeletion(0);
            wl_pickerage.setSeletion(0);
            wl_pickerheight.setSeletion(0);
            layout_wl.setVisibility(View.GONE);
        }
    }

    private void getSubStructure() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("parent_id", "0");
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetStructure, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        StructureContent disscomment = JSON.parseObject(response.getString("content"), StructureContent.class);
                        String structurename = disscomment.getCustomer();
                        List<StructureItems> lists = disscomment.getStructure();
                        Intent intent = new Intent(mContext, ChooseComStructureActivity.class);
                        intent.putExtra(Preferences.ActivitionStructureCode, "0");
                        intent.putExtra(Preferences.LastStructureName, structurename);
                        intent.putExtra(Preferences.LastStructureItems, (Serializable) lists);
                        intent.putExtra(Preferences.IsSettingPersonalData, true);
                        startActivityForResult(intent, SetStructure);

                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });
    }

    private void editUserInfo(final String field, final String value) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("field", field);
        params.put("value", value);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyPost(Constants.EditUserInfo, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, "修改失败，请检查网络连接", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        if (field.equals("nick_name")) {
                            personal_nickname_tv.setText(value);
                            AppSharedPreferencesHelper.setNick_name(value);
                        }
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }
                cancelmDialog();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.i(tag, "resultCode Activity.RESULT_CANCELED  resultCode:" + resultCode);
            return;
        } else {
            Log.i(tag, "resultCode Activity.RESULT_OK");
            switch (requestCode) {
                case TAKE_PHOTO:
                    Log.i(tag, "onActivityResult  TAKE_PHOTO");
                    String originalPath = null;
                    Uri uri = null;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    }
                    // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri (魅族无法获取到URI)
                    if (uri == null) {
                        Log.i(tag, "uri  null");
                        if (photoUri != null) {
                            uri = photoUri;
                        }
                    }
                    // 头像正常情况 下，需要剪裁
                    tryCropProfileImage(uri);
                    break;
                default:
                    break;
            }

        }
        if (data != null) {
            switch (requestCode) {
                case PIC:
                    String pic = data.getStringExtra(Preferences.PICMethod);
                    Log.i(tag, "onActivityResult  PIC: " + pic);
                    if (pic.equals("TAKEPHOTO")) {
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent takeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            System.gc();
                            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMM_dd_HH_mm_ss");
                            String filename = timeStampFormat.format(new Date());
                            ContentValues values = new ContentValues();
                            values.put(Media.TITLE, filename);
                            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            takeintent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            Log.i(tag, "take_photo");
                            startActivityForResult(takeintent, TAKE_PHOTO);
                        } else {
                            Toast.makeText(SettingPersonalDataActivity.this, "没有SD卡", Toast.LENGTH_SHORT).show();
                        }
                    } else if (pic.equals("PICKPHOTO")) {
                        doPickPhoto();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    if (data != null) {
                        setImageToHeadView(data);
                    }
                    break;
                case CODE_CAMERA_REQUEST:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        cropRawPhoto(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Crop_PICK_PHOTO_FROM_MEIZU:
                    break;
                case PICK_PHOTO_FROM_MEIZU:
                    Log.i(tag, "PICK_PHOTO_FROM_MEIZU");
                    photo = data.getParcelableExtra("data");
                    if (photo == null) {
                        Uri originalUri;
                        if (data.getData() == null) {
                            String filePath = data.getStringExtra("filePath");
                            Log.i(tag, "filePath: " + filePath);
                            originalUri = Uri.fromFile(new File(filePath));
                        } else {
                            originalUri = data.getData();
                        }
                        Log.i(tag, "originalUri: " + originalUri + "  " + originalUri.toString());
                        String originalPath = uri2filePath(originalUri);
                        Log.i(tag, "originalPath:  " + originalPath);
                        originalPath = GetPathFromUri4kitkat.getPath(mContext, originalUri);
                        Log.i(tag, "originalPath:  " + originalPath);
                        tryCropProfileImage(originalUri);
                    } else {
                        mCurrentPhotoFile = new File(DcareUtils.getTmpCachePath() + "screenshot.png");
                        try {
                            saveBitmap(mCurrentPhotoFile.getAbsolutePath(), photo);

                            onHeaderSelectedCallBack(photo);
                            updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case PICKPHOTO:
                    Log.i(tag, "onActivityResult  PICKPHOTO");
                    if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists()) {
                        mCurrentPhotoFile.delete();
                    }

                    photo = data.getParcelableExtra("data");
                    if (photo == null) {
                        // get photo url
                        Uri originalUri;
                        if (data.getData() == null) {
                            String filePath = data.getStringExtra("filePath");
                            originalUri = Uri.fromFile(new File(filePath));
                        } else {
                            originalUri = data.getData();
                        }
                        try {
                            onLicenseSelectedCallBack(originalUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mCurrentPhotoFile = new File(DcareUtils.getTmpCachePath() + "screenshot.png");
                        try {
                            saveBitmap(mCurrentPhotoFile.getAbsolutePath(), photo);

                            onHeaderSelectedCallBack(photo);
                            updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case TAKE_PHOTO:
                    Log.i(tag, "onActivityResult  TAKE_PHOTO");
                    if (resultCode == RESULT_OK) {
                        String originalPath = null;
                        Uri uri = null;
                        if (data != null && data.getData() != null) {
                            uri = data.getData();
                        }
                        // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
                        if (uri == null) {
                            if (photoUri != null) {
                                uri = photoUri;
                            }
                        }
                        originalPath = GetPathFromUri4kitkat.getPath(mContext, uri);
                        originalPath = uri2filePath(uri);
                        if (StringUtils.isEmpty(originalPath)) {
                            Log.i(tag, "originalPath:  " + originalPath);
                            updateAvatorByFile(originalPath);
                        } else {
                            Log.i(tag, "error  拍照失败  ");
                            Toast.makeText(mContext, "拍照失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case TAKEPHOTO:
                    Uri originalUri = data.getData();
                    Log.i(tag, "onActivityResult  TAKEPHOTO URI: " + originalUri.toString());
                    // onLicenseSelectedCallBack(originalUri);
                    tryCropProfileImage(Uri.fromFile(mCurrentPhotoFile));
                    break;
                case CAMERA_PIC:
                    Log.i(tag, "CAMERA_PIC");
                    cropPhoto(Uri.fromFile(mUriFile));
                    break;
                case CROP_PIC:
                    Log.d(tag, "error");
                    break;
                case PICCrop:

                    break;
                case SEX:
                    String sex = data.getStringExtra(Preferences.SelectSex);
                    Log.i(tag, "onActivityResult  SEX : " + sex);
                    if (sex != null) {
                        personal_sex_tv.setText(sex);
                        AppSharedPreferencesHelper.setGender(sex.equals("男") ? "1" : "2");
                        editUserInfo("gender", sex.equals("男") ? "1" : "2");
                    }

                    break;
                case NICKNAME:
                    String nickname = data.getStringExtra(Preferences.SelectNickName);
                    Log.i(tag, "onActivityResult  NICKNAME : " + nickname);
                    if (nickname != null && !nickname.isEmpty()) {
                        personal_nickname_tv.setText(nickname);
                    }
                    break;
                case USERNAME:
                    String username = data.getStringExtra(Preferences.SelectNickName);
                    Log.i(tag, "onActivityResult  NICKNAME : " + username);
                    if (username != null && !username.isEmpty()) {
                        personal_username_tv.setText(username);
                    }
                    break;
                case DEPARTMENT:
                case SetStructure:
                    String department = data.getStringExtra(Preferences.SelectDepart);
                    String departmentid = data.getStringExtra(Preferences.SelectDepart_Id);
                    Log.i(tag, "onActivityResult  DEPARTMENT : " + department);
                    if (department != null && departmentid != null) {
                        personal_deapartment_tv.setText(department);
                        AppSharedPreferencesHelper.setStructure(department);
                        editUserInfo("structure_id", departmentid);
                    }

                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Log.i(tag, "setImageToHeadView");
        }
    }

    protected void onLicenseSelectedCallBack(Uri url) {

        personalavator_ciriv.setImageURI(url);
        updateAvatorByUrl(url);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intentFromCapture.putExtra("scale", true);
        mUriFile = new File(Environment.getExternalStorageDirectory() + "/dcare/" + System.currentTimeMillis() + ".jpg");
        try {
            mUriFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mUriFile));
        intentFromCapture.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intentFromCapture.putExtra("noFaceDetection", true);
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }

    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    private void updateAvatorByUrl(Uri url) {
        updateAvatorByFile(getAbsoluteImagePath(url));
    }

    private void updateAvatorByFile(String absolutePath) {
        if (!StringUtils.isEmpty(absolutePath)) {
            if (new File(absolutePath).isFile()) {
                Log.e(tag, "startUpload PIC File : " + absolutePath);
                RequestParams params = new RequestParams();
                params.put("access_token", SharePreferencesUtil.getToken(mContext));
                try {
                    params.put("avatar", new File(absolutePath));
                } catch (FileNotFoundException e1) {
                    Log.e(tag, "文件没有找到");
                    e1.printStackTrace();
                    return;
                }
                cancelmDialog();
                showProgress(DIALOG_DEFAULT, true);
                Log.i(tag, "params: " + params.toString());
                DcareRestClient.post(Constants.UploadUserAvator, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
                    @Override
                    public void onCancel() {
                        Log.w(tag, "post cancel" + this.getRequestURI());
                        cancelmDialog();
                        super.onCancel();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                        cancelmDialog();
                        Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                        try {
                            int code = response.getInteger("code");
                            String message = response.getString("message");
                            Log.i(tag, "onSuccess  code: " + code + " message: " + message);
                            if (code == 0) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                String avatar = response.getString("content");
                                AppSharedPreferencesHelper.setAvatar(avatar);
                                SharePreferencesUtil.saveAvatar(mContext, avatar);
                                SharedPreferences sp = getSharedPreferences(Preferences.CommonLoginInfo, MODE_PRIVATE);
                                sp.edit().putString(Preferences.USER_AVATOR, avatar).commit();

                            } else {// code不为0 发生异常
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(tag, "JSONException:");
                            e.printStackTrace();
                        }
                        cancelmDialog();

                    }
                });
            } else {
                Toast.makeText(mContext, "查找文件出错，上传头像失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "文件路径出错，上传头像失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 把Uri转化成文件路径
     */
    private String uri2filePath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        // Cursor cursor = managedQuery(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        return path;
    }

    protected String getAbsoluteImagePath(final Uri uri) {
        // can post image
        final String[] proj = {MediaStore.Images.Media.DATA};
        String path = GetPathFromUri4kitkat.getPath(mContext, uri);
        if (!StringUtils.isEmpty(path)) {
            return path;
        }
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // 获取状态栏的高度
    private int getStatusBarHeight() {
        int x = 0;
        int statusBarHeight = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            java.lang.reflect.Field field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getResources().getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    protected void onHeaderSelectedCallBack(Bitmap bp) {
        if (bp == null) {
            Log.e(tag, "发生异常");
        }
        personalavator_ciriv.setImageBitmap(bp);
    }

    private void tryCropProfileImage(Uri uri) {
        try {
            // start gallery to crop photo
            Log.i(tag, "tryCropProfileImage: " + uri + "  " + uri.toString());
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            Log.i(tag, "tryCropProfileImage  PICKPHOTO");
            startActivityForResult(intent, SettingPersonalDataActivity.PICKPHOTO);// test
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void saveBitmap(String filePath, Bitmap bitmap) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap = compressImage(bitmap);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);// 图片设置为压缩一半
            // 11.24 18：50
            out.flush();
            out.close();
        }
    }

    protected Bitmap compressImage(Bitmap image) {
        Log.i(tag, "压缩图片  把图片压缩到50k以下");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        Log.i(tag, "压缩图片" + baos.size());
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private void doPickPhoto() {
        Log.i(tag, Build.BOARD + " " + Build.MODEL + " " + Build.BRAND + " ");
        if (Build.BRAND.equalsIgnoreCase("Meizu")) {
            Intent itentFromGallery = new Intent();
            itentFromGallery.setType("image/*");
            itentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(itentFromGallery, PICK_PHOTO_FROM_MEIZU);
            return;
        } else {

        }

        try {
            // Launch picker to choose photo for selected contact
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(Uri.parse("content://media/internal/images/media"));
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, SettingPersonalDataActivity.PICKPHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doTakePhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent takeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMM_dd_HH_mm_ss");
            String filename = timeStampFormat.format(new Date());
            photoUri = Uri.fromFile(new File(filename));
            takeintent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            Log.i(tag, "startActivityForResult TAKE_PHOTO");
            startActivityForResult(takeintent, TAKE_PHOTO);
        } else {
            Toast.makeText(mContext, "没有SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }

    private void takePic() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mUriFile = new File(Environment.getExternalStorageDirectory() + "/dcare/" + System.currentTimeMillis() + ".jpg");
        try {
            mUriFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(tag, "takePic mUriFile:" + mUriFile.getAbsolutePath());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mUriFile));
        startActivityForResult(intent, CAMERA_PIC);
    }

    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_PIC);
    }

    // ////魅族

    /**
     * 用于截取大图
     *
     * @param ctx
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    public static void cropImageUri(Activity ctx, Uri uri, int outputX, int outputY, int requestCode, boolean scale) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// 发送裁剪信号
        intent.putExtra("outputX", outputX);// 裁剪区的宽
        intent.putExtra("outputY", outputY);// 裁剪区的高
        intent.putExtra("aspectX", 1);// X方向上的比例
        intent.putExtra("aspectY", 1);// Y方向上的比例
        intent.putExtra("scale", scale);// 是否保留比例
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);//直接输出文件
        intent.putExtra("return-data", true); // 是否返回数据
        // intent.putExtra("outputFormat",
        // Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // 关闭人脸检测
        ctx.startActivityForResult(intent, requestCode);
    }

    @Override
    protected Activity atvBind() {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void initDatas() {
        initviewfromlayout();
        setclickevent();
        initView();
    }

    @Override
    protected int setLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_personal;
    }
}

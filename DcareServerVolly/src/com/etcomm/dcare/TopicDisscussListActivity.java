package com.etcomm.dcare;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.etcomm.dcare.adapter.CircleAdapter;
import com.etcomm.dcare.adapter.TopicDisscussListAdapter;
import com.etcomm.dcare.adapter.TopicDisscussListAdapter.DeleteOnClickListener;
import com.etcomm.dcare.adapter.TopicDisscussListAdapter.LikeOrUnLikeClickListener;
import com.etcomm.dcare.app.activity.TopicMemberActivity;
import com.etcomm.dcare.app.activity.setting.SelectPicPopupWindowActivity;
import com.etcomm.dcare.app.activity.setting.SettingPersonalDataActivity;
import com.etcomm.dcare.app.activity.setting.TopicDiscussSettingActivity;
import com.etcomm.dcare.app.activity.setting.TopicReportPopActivity;
import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.model.TopicUser;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.DisscussContent;
import com.etcomm.dcare.netresponse.DisscussItems;
import com.etcomm.dcare.util.DcareUtils;
import com.etcomm.dcare.util.GetPathFromUri4kitkat;
import com.etcomm.dcare.widget.CircleImageView;
import com.etcomm.dcare.widget.DialogFactory;
import com.etcomm.dcare.widget.HorizontalListView;
import com.etcomm.dcare.widget.SimpleTitleBarWithRightText;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 话题下的讨论页面
 *
 * @author iexpressbox
 */
public class TopicDisscussListActivity extends BaseActivity implements OnTouchListener {

    protected static final int GeList_END = 0;
    private Intent intent;
    boolean isAttentioned;
    String topic_id;
    String user_id = "";
    String discuse;
    String activity_rank;
    String topic_avator;

    private int _xDelta;
    private int _yDelta;
    @Bind(R.id.adddisscuss_iv)
    ImageView adddisscuss_iv;
    @Bind(R.id.titlebar)
    SimpleTitleBarWithRightText titlebar;
    @Bind(R.id.pulllistview)
    PullToRefreshListView pulllistview;
    @Bind(R.id.root)
    RelativeLayout _root;
    @Bind(R.id.topic_image)
    CircleImageView topic_image;
    @Bind(R.id.topic_discuss)
    EditText topic_discuss;
    @Bind(R.id.attion_count)
    TextView attion_count;
    @Bind(R.id.attion_image)
    HorizontalListView image_list;
    @Bind(R.id.emptyview)
    View emptyview;
    @Bind(R.id.attention_member)
    LinearLayout attention_member;
    @Bind(R.id.depart_rank)
    ImageView depart_rank;

    ListView listview;
    private String topic_name;
    private int mScreenWidth;
    private int attion;
    protected Dialog deletedisscuss;
    private List<TopicUser> image;
    private TopicDisscussListAdapter mAdapter;
    private CircleAdapter circleAdapter;
    protected int page_size = 6;
    protected int page_number = 1;
    public static final int PIC = 1;
    private static final int TAKE_PHOTO = 10;
    public static final int PICK_PHOTO_FROM_MEIZU = 16;
    public static final int Crop_PICK_PHOTO_FROM_MEIZU = 17;
    private static final int CAMERA_PIC = 21;
    private static final int CODE_CAMERA_REQUEST = 30;
    private static final int CODE_RESULT_REQUEST = 33;
    protected static final int ToTakePhoto = 0;
    public static final int TAKEPHOTO = 11;
    public static final int PICKPHOTO = 12;
    private static final int CROP_PIC = 22;
    private static final int Right = 3;
    private static final int LongClick = 4;

    protected Bitmap photo;
    protected File mCurrentPhotoFile;
    private Uri photoUri;
    private boolean isSave = true;
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    private File mUriFile;
    private ArrayList<DisscussItems> mList = new ArrayList<DisscussItems>();
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GeList_END:
                    mAdapter.notifyDataSetChanged();
                    if (mList.size() == 0) {
                        emptyview.setVisibility(View.VISIBLE);
                    } else {
                        emptyview.setVisibility(View.INVISIBLE);
                    }
                    break;

                default:
                    break;
            }
        }

    };


    @Override
    public void onResume() {
        super.onResume();
        getList(true, page_size, 1);
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

    private void initView() {
        titlebar.setTitle(topic_name);
        titlebar.setLeftLl(backClickListener);
//         设置控件拖拽效果
        if (Integer.valueOf(android.os.Build.VERSION.SDK) >= 19 && !android.os.Build.MODEL.equals("SM-G6000")) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(180, 180);
            Log.d("屏幕", "屏幕宽高" + getWindowManager().getDefaultDisplay().getWidth() + "---" + getWindowManager().getDefaultDisplay().getHeight());
            layoutParams.leftMargin = getWindowManager().getDefaultDisplay().getWidth() / 4 * 3;// 查看到源码:The left margin in pixels of
            layoutParams.topMargin = getWindowManager().getDefaultDisplay().getHeight() / 4 * 3; // 说明这里也是px
            layoutParams.bottomMargin = -250;
            layoutParams.rightMargin = -250;
            adddisscuss_iv.setLayoutParams(layoutParams);
            adddisscuss_iv.setOnTouchListener(this);
        }


        listview = pulllistview.getRefreshableView();
        DeleteOnClickListener deleteOnClickListener = new DeleteOnClickListener() {

            @Override
            public void delete(DisscussItems mInfo) {
                // TODO Auto-generated method stub

                if (mList.contains(mInfo)) {
                    mList.remove(mInfo);
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        LikeOrUnLikeClickListener likeOrUnLikeClickListener = new LikeOrUnLikeClickListener() {

            @Override
            public void delete(boolean islike, DisscussItems mInfo) {
                // TODO Auto-generated method stub
                for (int i = 0; i < mList.size(); i++) {
                    DisscussItems d = mList.get(i);
                    if (d.getDiscussion_id() == mInfo.getDiscussion_id()) {
                        if (islike) {
                            d.setLike((Integer.parseInt(d.getLike()) + 1) + "");
                            d.setIs_like("1");
                        } else {
                            d.setLike((Integer.parseInt(d.getLike()) - 1) + "");
                            d.setIs_like("0");
                        }
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        };
        mAdapter = new TopicDisscussListAdapter(mContext, mList, mScreenWidth, topic_id, deleteOnClickListener, likeOrUnLikeClickListener);

        listview.setAdapter(mAdapter);
        listview.setDividerHeight(5);

//长按举报帖子
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DisscussItems m = mAdapter.getItem(position - 1);
                if (m.getShare_type().equals("0")) {
                    Intent intent = new Intent(mContext, TopicReportPopActivity.class);
                    intent.putExtra("discussion_id", m.getDiscussion_id());
                    intent.putExtra("type", "discussion");
                    startActivity(intent);
                }
                return true;
            }
        });

        pulllistview.setMode(Mode.BOTH);
        pulllistview.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number = 1;
                getList(true, page_size, page_number);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page_number++;
                getList(false, page_size, page_number);

            }
        });

        attention_member.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopicMemberActivity.class);
                intent.putExtra("topic_id", topic_id);
                Log.d(tag, "attion==" + attion);
                intent.putExtra("attion", attion);
                startActivity(intent);
            }
        });

        /**
         * 键盘隐藏的时候，如果小组内容为空，设置原文本
         */
        _root.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Log.v(TAG, "detailMainRL.getHeight() =" + _root.getHeight());

                        if (isSave) {
                            SharePreferencesUtil.saveHeigh(mContext, _root.getHeight());
                            isSave = false;
                        }
                        if (_root.getHeight() < SharePreferencesUtil.getHeigh(mContext)) { // 说明键盘是弹出状态
                            Log.v(TAG, "键盘弹出状态");

                        } else {
                            Log.v(TAG, "键盘收起状态");
                            topic_discuss.setText(discuse);
                            topic_discuss.setCursorVisible(false);
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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
                            values.put(MediaStore.Images.Media.TITLE, filename);
                            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            takeintent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            Log.i(tag, "take_photo");
                            startActivityForResult(takeintent, TAKE_PHOTO);
                        } else {
                            Toast.makeText(mContext, "没有SD卡", Toast.LENGTH_SHORT).show();
                        }
                    } else if (pic.equals("PICKPHOTO")) {
                        doPickPhoto();
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
                            editUserInfo("avatar", mCurrentPhotoFile.getAbsolutePath());
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
                            editUserInfo("avatar", mCurrentPhotoFile.getAbsolutePath());
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
                            editUserInfo("avatar", originalPath);
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
                case Right:
                    String type = data.getStringExtra(Preferences.TOPICSET);
                    if (type.equals("1")) {
                        //取关
                        if (user_id.equals(AppSharedPreferencesHelper.getUserId())) {
                            int lefttextcolor;
                            int righttextcolor;

                            if (Build.VERSION.SDK_INT >= 23) {
                                lefttextcolor = mContext.getColor(R.color.common_dialog_btn_textcolor);
                                righttextcolor = mContext.getColor(R.color.common_dialog_btn_textcolor);
                            } else {
                                lefttextcolor = mContext.getResources().getColor(R.color.common_dialog_btn_textcolor);
                                righttextcolor = mContext.getResources().getColor(R.color.common_dialog_btn_textcolor);
                            }

                            deletedisscuss = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确认删除这个小组吗？", "取消", "确认", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deletedisscuss.dismiss();
                                }
                            }, new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (deletedisscuss != null && deletedisscuss.isShowing()) {
                                        deletedisscuss.dismiss();
                                        deleteTopic();
                                    }
                                }
                            }, lefttextcolor, righttextcolor);

                        } else {
                            if (isAttentioned) {
                                unfollow();
                            } else {
                                follow();
                            }
                        }


                    } else if (type.equals("2")) {
                        //举报
                        showToast("举报");
                    }
                    break;
            }
        }
    }

    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 50);
        intent.putExtra("outputY", 50);
        intent.putExtra("scale", true);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_PIC);
    }

    protected void onLicenseSelectedCallBack(Uri url) {

        topic_image.setImageURI(url);
        updateAvatorByUrl(url);
    }

    private void updateAvatorByUrl(Uri url) {
        editUserInfo("avatar", getAbsoluteImagePath(url));
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

    protected void onHeaderSelectedCallBack(Bitmap bp) {
        if (bp == null) {
            Log.e(tag, "发生异常");
        }
        topic_image.setImageBitmap(bp);
    }

//


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

    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
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
        intent.putExtra("outputX", 50);
        intent.putExtra("outputY", 50);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
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
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            intent.putExtra("return-data", true);
            Log.i(tag, "tryCropProfileImage  PICKPHOTO");
            startActivityForResult(intent, SettingPersonalDataActivity.PICKPHOTO);// test
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, SettingPersonalDataActivity.PICKPHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void deleteTopic() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic_id", topic_id);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DeleteTopic, params, new FastJsonHttpResponseHandler() {
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
                // TODO Auto-generated method stub
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        finish();
                        return;
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cancelmDialog();
            }
        });
    }

    protected void follow() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic_id", topic_id);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.FollowTopic, params, new FastJsonHttpResponseHandler() {
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
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        finish();
                        return;
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        isAttentioned = true;
                        if (isAttentioned) {
                            adddisscuss_iv.setVisibility(View.VISIBLE);
                        } else {
                            adddisscuss_iv.setVisibility(View.INVISIBLE);
                        }
                        attion_count.setText(attion + 1 + "个成员   >");
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cancelmDialog();
            }
        });
    }

    protected void unfollow() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("topic_id", topic_id);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.UNFollowTopic, params, new FastJsonHttpResponseHandler() {
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
                // TODO Auto-generated method stub
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        exceptionCode(code);
                        return;
                    }
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    // + response.getString("content").toString());
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        isAttentioned = false;
                        if (isAttentioned) {
                            adddisscuss_iv.setVisibility(View.VISIBLE);
                        } else {
                            adddisscuss_iv.setVisibility(View.INVISIBLE);
                        }
                        attion_count.setText(attion - 1 + "个成员   >");
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                cancelmDialog();
            }
        });
    }

    protected void getList(final boolean b, int pagesize, int pagenumber) {
        pulllistview.setRefreshing();
        Map<String, String> params = new HashMap<String, String>();
        params.put("type", "1");
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        params.put("page_size", String.valueOf(pagesize));
        params.put("topic_id", topic_id);
        params.put("page_number", String.valueOf(pagenumber));
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.GetTopicDisscussList, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                // TODO Auto-generated method stub
                Log.w(tag, "post cancel" + this.getRequestURI());
                cancelmDialog();
                super.onCancel();
                pulllistview.onRefreshComplete();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                if (errorResponse != null) {
                    Log.e(tag, "  errorResponse: " + errorResponse.toString());
                }
                cancelmDialog();
                Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_SHORT).show();
                pulllistview.onRefreshComplete();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // TODO Auto-generated method stub
                Log.i(tag, "getList: " + response.toString());
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    String contents = response.getString("content");
                    if (code != 0) {
                        exceptionCode(code);
                        finish();
                        return;
                    }
                    Log.e(tag, "onSuccess  code: " + code + " message: " + message + "content: " + contents);
                    if (code == 0) {
                        DisscussContent content = JSON.parseObject(response.getJSONObject("content").toString(), DisscussContent.class);
                        List<DisscussItems> list = content.getItems();
                        List<TopicUser> list1 = content.getTopic().user;
                        image = list1;
                        circleAdapter = new CircleAdapter(mContext, image);
                        image_list.setAdapter(circleAdapter);
                        image = content.getTopic().user;
                        Log.d(tag, "image" + image.toString());
                        topic_discuss.setText(content.getTopic().desc + "");
                        if (content.getTopic().is_activity.equals("0")) {
                            depart_rank.setVisibility(View.GONE);
                        } else {
                            depart_rank.setVisibility(View.VISIBLE);
                        }
                        activity_rank = content.getTopic().activity_rank;
                        discuse = content.getTopic().desc + "";
                        attion = Integer.parseInt(content.getTopic().user_number);
                        user_id = content.getTopic().user_id;
                        if (content.getTopic().is_followed.equals("0")) {
                            isAttentioned = false;
                        } else {
                            isAttentioned = true;
                        }
                        /**如果是自己创建的小组，可修改头像，可点击修改相关信息
                         * * */
                        if (user_id.equals(AppSharedPreferencesHelper.getUserId())) {
                            topic_discuss.setEnabled(true);
                            topic_image.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, SelectPicPopupWindowActivity.class);
                                    startActivityForResult(intent, PIC);
                                }
                            });


                            topic_discuss.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    topic_discuss.setText("");
                                    topic_discuss.setSelection(topic_discuss.getText().toString().length());
                                    topic_discuss.setCursorVisible(true);
                                    topic_discuss.requestFocus();
                                }
                            });
                            /**
                             * 击完成按钮，修改小组描述信息
                             */
                            topic_discuss.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                    if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                                        if (topic_discuss.getText().toString() != null) {
                                            editUserInfo("description ", topic_discuss.getText().toString());
                                        }
                                        return true;

                                    }
                                    return false;
                                }
                            });

                        }

                        attion_count.setText(attion + "个成员   >");
                        topic_avator = content.getTopic().avatar;
                        if (!content.getTopic().avatar.isEmpty()) {
                            ImageLoader.getInstance().displayImage(topic_avator, topic_image);
                        }
                        Log.d(tag, content.getTopic().toString());
                        if (b && list != null) {
                            mList.clear();
                        }
                        if (list != null) {
                            mList.addAll(list);
                        }
                        Log.i(tag, "mList size: " + mList.size());
                        mHandler.sendEmptyMessage(GeList_END);
                        pulllistview.onRefreshComplete();
                    } else {// code不为0 发生异常
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pulllistview.onRefreshComplete();
            }
        });
    }

    @Override
    protected Activity atvBind() {
        return this;
    }

    @Override
    protected void initDatas() {
        WindowManager wm = getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        mScreenWidth = width;
        Log.e(tag, "mScreenWidth: " + mScreenWidth);
        intent = getIntent();
        topic_id = intent.getStringExtra("topic_id");
        topic_name = intent.getStringExtra("topic_name");
        boolean isFromMSG = intent.getBooleanExtra("isFromMSG", false);
        if (isFromMSG) {
            AppSharedPreferencesHelper.setHaveReceiveUnReadData(false);
        }
/**
 *点击活动小组的活动图标，进入活动排名页面
 **/
        depart_rank.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityRanktActivity.class);
                intent.putExtra("activity_rank", activity_rank);
                startActivity(intent);
            }
        });
        titlebar.setRightImage(R.drawable.ic_more);
        titlebar.setRightClick(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TopicDiscussSettingActivity.class);
                intent.putExtra("id", user_id);
                intent.putExtra("topic_id", topic_id);
                intent.putExtra("isAttentioned", isAttentioned);
                intent.putExtra("image", topic_avator);
                intent.putExtra("discuse", discuse);
                intent.putExtra("topic_name", topic_name);
                startActivityForResult(intent, Right);
            }
        });
        initView();
    }

    @Override

    protected int setLayoutId() {
        return R.layout.activity_topicdisscusslist;
    }

    @SuppressLint("NewApi")
    @OnClick({R.id.adddisscuss_iv, R.id.titlebar,})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.adddisscuss_iv:
                if (isAttentioned) {
                    Intent intent = new Intent(mContext, AddTopicDisscussActivity2.class);
                    intent.putExtra("topic_id", topic_id);
                    intent.putExtra("topic_name", topic_name);
                    startActivity(intent);
                } else {
                    Toast.makeText(mContext, "关注之后才可以发布帖子！", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }


    private boolean isMove; // 解决onTouch和onClick事件冲突的问题
    private float startx;
    private float starty;

    /**
     * 关注的小组里面，发帖按钮点击可移动
     */
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                startx = event.getX();
                starty = event.getY();
                Log.i(TAG, "startx====" + startx + ",,,,starty=" + starty);
                isMove = false;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                // 防止超出左边界
                if (X - _xDelta > 0) {
                    layoutParams.leftMargin = X - _xDelta;
                } else {
                    layoutParams.leftMargin = 0;
                }
                // 防止超出上边界
                if (Y - _yDelta > 0) {
                    layoutParams.topMargin = Y - _yDelta;
                } else {
                    layoutParams.topMargin = 0;
                }
                Log.i(TAG, "X - _xDelta ==" + (X - _xDelta) + "Y - _yDelta ==" + (Y - _yDelta));
                // 防止超出右边界
                if (X - _xDelta > getWindowManager().getDefaultDisplay().getWidth() - 182) {
                    layoutParams.leftMargin = getWindowManager().getDefaultDisplay().getWidth() - 182;
                }
                // 防止超出下边界
                if (Y - _yDelta > getWindowManager().getDefaultDisplay().getHeight() - 255) {
                    layoutParams.topMargin = getWindowManager().getDefaultDisplay().getHeight() - 255;
                }

                view.setLayoutParams(layoutParams);
                if (Math.abs(event.getX() - startx) > 0.5 || Math.abs(event.getY() - starty) > 0.5) {
                    isMove = true;// 一旦有move的动作，就不触发onclick
                } else {
                    isMove = false;// move的距离过短也执行onclick
                }

                break;
        }
        _root.invalidate();
        return isMove;
    }

    /**
     * 用户自己创建的小组，小组头像和简介点击可修改，修改接口调用
     */
    private void editUserInfo(final String field, final String value) {
        RequestParams params = new RequestParams();
        params.put("field", field);
        params.put("topic_id", topic_id);
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        cancelmDialog();
        showProgress(DIALOG_DEFAULT, true);
        if (field.equals("avatar")) {
            Log.e(tag, "startUpload PIC File : " + value);
            if (new File(value).isFile()) {
                try {
                    params.put("value", new File(value));
                } catch (FileNotFoundException e1) {
                    Log.e(tag, "文件没有找到");
                    e1.printStackTrace();
                    return;
                }
            } else {
                cancelmDialog();
                Toast.makeText(mContext, "查找文件出错，上传头像失败", Toast.LENGTH_SHORT).show();
            }

        } else {
            params.put("value", value);
        }

        Log.i(tag, "params: " + params.toString() + "Constants.UploadTopicNews" + Constants.UploadTopicNews);

        DcareRestClient.post(Constants.UploadTopicNews, SharePreferencesUtil.getToken(mContext), params, new FastJsonHttpResponseHandler() {
            @Override
            public void onCancel() {
                cancelmDialog();
                Log.w(tag, "post cancel" + this.getRequestURI());

                super.onCancel();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                topic_discuss.setText(discuse);
                showToast(R.string.network_error);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e(tag, "onSuccess:" + this.getRequestURI() + " statusCode" + statusCode + " response " + response.toString());
                cancelmDialog();
                try {
                    int code = response.getInteger("code");
                    String message = response.getString("message");
                    if (code != 0) {
                        topic_discuss.setText(discuse);
                    }
                    showToast(message);
                    getList(true, page_size, 1);

                } catch (JSONException e) {
                    Log.e(tag, "JSONException:");
                    e.printStackTrace();
                }

            }
        });
    }
}

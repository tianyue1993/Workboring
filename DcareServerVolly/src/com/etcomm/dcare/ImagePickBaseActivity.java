package com.etcomm.dcare;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.etcomm.dcare.app.base.BaseActivity;
import com.etcomm.dcare.util.DcareUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @brief 选择图片base页面
 */
public abstract class ImagePickBaseActivity extends BaseActivity {

    private final static String TAG = ImagePickBaseActivity.class
            .getSimpleName();
    // private ArrayList<BottomMenuItem> bottomMenuItems;
    public static final int CAMERA_WITH_DATA = 3011;
    public static final int PHOTO_PICKED_WITH_DATA = 3012;
    protected Bitmap photo;
    protected File mCurrentPhotoFile;
    protected boolean isUploadHeader = true;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void doTakePhoto() {
        try {
            // Launch camera to take photo for selected contact
            DcareUtils.PHOTO_DIR.mkdirs();
            mCurrentPhotoFile = new File(DcareUtils.PHOTO_DIR,
                    DcareUtils.getPhotoFileName());
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(mCurrentPhotoFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doPickPhotoFromGallery() {
        try {
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
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void tryCropProfileImage(Uri uri) {
        try {
            // start gallery to crop photo
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA: {
                if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists()) {
                    mCurrentPhotoFile.delete();
                }

                photo = data.getParcelableExtra("data");

                if (photo == null) {
                    // get photo url
                    Uri originalUri = data.getData();
                    // 将图片内容解析成字节数组
                    // byte[] mContent;
                    try {
                        if (isUploadHeader) {
                            tryCropProfileImage(Uri.parse(originalUri.toString()));
                        } else {
                            onLicenseSelectedCallBack(originalUri);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mCurrentPhotoFile = new File(DcareUtils.getTmpCachePath()
                            + "screenshot.png");
                    try {
                        saveBitmap(mCurrentPhotoFile.getAbsolutePath(), photo);

                        onHeaderSelectedCallBack(photo);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            case CAMERA_WITH_DATA: {
                if (isUploadHeader) {
                    tryCropProfileImage(Uri.fromFile(mCurrentPhotoFile));
                } else {
                    onLicenseSelectedCallBack(Uri.fromFile(mCurrentPhotoFile));
                }
                break;
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected void onHeaderSelectedCallBack(Bitmap bp) {
    }

    protected void onLicenseSelectedCallBack(Uri url) {
    }

    protected void saveBitmap(String filePath, Bitmap bitmap)
            throws IOException {
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
        Log.i(TAG, "压缩图片  把图片压缩到50k以下");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        Log.i(TAG, "压缩图片" + baos.size());
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

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


}

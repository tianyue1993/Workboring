package com.etcomm.dcare.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.etcomm.dcare.CommonWebViewActivity;
import com.etcomm.dcare.DisscussConentListActivity;
import com.etcomm.dcare.HealthConsultActivity;
import com.etcomm.dcare.R;
import com.etcomm.dcare.TopicDisscussListActivity;
import com.etcomm.dcare.app.activity.setting.TopicReportPopActivity;
import com.etcomm.dcare.app.common.config.Constants;
import com.etcomm.dcare.app.common.config.Preferences;
import com.etcomm.dcare.app.utils.SharePreferencesUtil;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.app.widget.ProgressDialog;
import com.etcomm.dcare.common.AppSharedPreferencesHelper;
import com.etcomm.dcare.http.DcareRestClient;
import com.etcomm.dcare.listener.FastJsonHttpResponseHandler;
import com.etcomm.dcare.netresponse.DisscussItems;
import com.etcomm.dcare.netresponse.DisscussPhotosItems;
import com.etcomm.dcare.util.DensityUtil;
import com.etcomm.dcare.widget.CircleImageView;
import com.etcomm.dcare.widget.DialogFactory;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class TopicDisscussListAdapter extends DcareBaseAdapter<DisscussItems> {

    private int mScreenWidth;
    protected String topic_id;
    protected ProgressDialog mProgress;

    public interface DeleteOnClickListener {
        public void delete(DisscussItems mInfo);
    }

    private DeleteOnClickListener deleteOnClickListener;

    public interface LikeOrUnLikeClickListener {
        public void delete(boolean islike, DisscussItems mInfo);
    }

    private LikeOrUnLikeClickListener likeOrUnLikeClickListener;

    public TopicDisscussListAdapter(Context context, ArrayList<DisscussItems> mList, int width, String topic_id, DeleteOnClickListener deleteOnClickListener, LikeOrUnLikeClickListener likeOrUnLikeClickListener) {
        super(context);
        this.mList = mList;
        this.mScreenWidth = width;
        this.topic_id = topic_id;
        this.deleteOnClickListener = deleteOnClickListener;
        this.likeOrUnLikeClickListener = likeOrUnLikeClickListener;
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_picture_loading).showImageForEmptyUri(R.drawable.ic_picture_loading).showImageOnFail(R.drawable.ic_picture_loading).cacheOnDisc(true).cacheInMemory(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
        // imageLoader.
        imageLoader.clearMemoryCache();
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final DisscussItems mInfo = mList.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.disscuss_item, null);
            holder = new ViewHolder();
            holder.root = (RelativeLayout) convertView.findViewById(R.id.root);
            holder.disscuss_content_tv = (TextView) convertView.findViewById(R.id.disscuss_content_tv);
            holder.disscuss_delete_iv = (ImageView) convertView.findViewById(R.id.disscuss_delete_iv);
            holder.disscuss_like_iv = (ImageView) convertView.findViewById(R.id.disscuss_like_iv);
            holder.disscuss_like_tv = (TextView) convertView.findViewById(R.id.disscuss_like_tv);
            holder.disscuss_messages_tv = (TextView) convertView.findViewById(R.id.disscuss_messages_tv);
            holder.disscuss_messages_iv = (ImageView) convertView.findViewById(R.id.disscuss_messages_iv);
            holder.disscuss_pics_gridview = (GridView) convertView.findViewById(R.id.disscuss_pics_gridview);
            holder.disscuss_time_tv = (TextView) convertView.findViewById(R.id.disscuss_time_tv);
            holder.disscuss_user_depart_tv = (TextView) convertView.findViewById(R.id.disscuss_user_depart_tv);
            holder.disscuss_user_name_tv = (TextView) convertView.findViewById(R.id.disscuss_user_name_tv);
            holder.disscuss_useravator = (CircleImageView) convertView.findViewById(R.id.disscuss_useravator);
            holder.item_healthnews_sumary = (TextView) convertView.findViewById(R.id.item_healthnews_sumary);
            holder.item_healthnews_image = (ImageView) convertView.findViewById(R.id.item_healthnews_image);
            holder.item_healthnews_title = (TextView) convertView.findViewById(R.id.item_healthnews_title);
            holder.share_health_news = (RelativeLayout) convertView.findViewById(R.id.share_health_news);
            holder.topic = (TextView) convertView.findViewById(R.id.topic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (null != mInfo) {

            if (mInfo.getShare_type().equals("0")) {
//用户发的帖子
                if (!StringUtils.isEmpty(mInfo.getContent())) {
                    holder.disscuss_content_tv.setVisibility(View.VISIBLE);
                } else {
                    Log.i(tag, mInfo.getContent());
                    holder.disscuss_content_tv.setVisibility(View.GONE);
                }
                holder.share_health_news.setVisibility(View.GONE);
                holder.disscuss_pics_gridview.setVisibility(View.VISIBLE);
            } else if (mInfo.getShare_type().equals("1")) {
                // 分享的资讯
                holder.disscuss_content_tv.setVisibility(View.GONE);
                holder.share_health_news.setVisibility(View.VISIBLE);
                holder.disscuss_pics_gridview.setVisibility(View.GONE);
                holder.topic.setVisibility(View.GONE);
                holder.item_healthnews_title.setText(mInfo.getTitle());
                holder.item_healthnews_sumary.setText(mInfo.getContent());
                if (mInfo.getPhotos().size() > 0) {
                    imageLoader.displayImage(mInfo.getPhotos().get(0).getThumb_image(), holder.item_healthnews_image);
                }
                holder.share_health_news.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle extras = new Bundle();
                        extras.putSerializable(Preferences.HealthNewsDetail, mInfo);
                        Intent intent = new Intent(mContext, HealthConsultActivity.class);
                        intent.putExtra("IsFromTopic", true);
                        intent.putExtras(extras);
                        mContext.startActivity(intent);
                    }
                });

            } else if (mInfo.getShare_type().equals("3")) {
                /**
                 *  分享的小组
                 */
                holder.disscuss_content_tv.setVisibility(View.GONE);
                holder.share_health_news.setVisibility(View.VISIBLE);
                holder.disscuss_pics_gridview.setVisibility(View.GONE);
                holder.topic.setVisibility(View.VISIBLE);
                holder.topic.setText("我和他们都关注了这个小组，你还在等什么？");
                if (mInfo.getPhotos().size() > 0) {
                    imageLoader.displayImage(mInfo.getPhotos().get(0).getThumb_image(), holder.item_healthnews_image);
                }
                if (!mInfo.getTitle().equals("")) {
                    holder.item_healthnews_title.setText(mInfo.getTitle());
                }
                if (mInfo.getContent() != null) {
                    holder.item_healthnews_sumary.setText(mInfo.getContent());
                } else {
                    holder.item_healthnews_sumary.setText("该创建者很懒，什么都没留下！");
                }

                Log.i(tag, "getView: " + mInfo.getContent());
                holder.share_health_news.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, TopicDisscussListActivity.class);
                        intent.putExtra("topic_id", mInfo.getShare_id());
                        intent.putExtra("topic_name", mInfo.getTitle());
                        intent.putExtra("Activity_id", mInfo.getShare_type());
                        intent.putExtra("activity_rank", mInfo.getDetail_url());
                        mContext.startActivity(intent);
                    }
                });
            } else if (mInfo.getShare_type().equals("2")) {
//                分享的活动
                holder.disscuss_content_tv.setVisibility(View.GONE);
                holder.share_health_news.setVisibility(View.VISIBLE);
                holder.disscuss_pics_gridview.setVisibility(View.GONE);
                holder.topic.setVisibility(View.VISIBLE);
                holder.topic.setText("我和他们都报名了这个活动，你还在等什么？");
                holder.item_healthnews_title.setText(mInfo.getTitle());
                holder.item_healthnews_sumary.setText(mInfo.getContent());
                if (mInfo.getPhotos().size() > 0) {
                    imageLoader.displayImage(mInfo.getPhotos().get(0).getThumb_image(), holder.item_healthnews_image);
                }
                holder.share_health_news.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, CommonWebViewActivity.class);
                        intent.putExtra(Preferences.CommonWebViewUrl,
                                mInfo.getDetail_url());
                        intent.putExtra(Preferences.CommonWebViewTitle, "活动详情");
                        intent.putExtra("IsFromTopic", true);
                        intent.putExtra("topic_name", mInfo.getTitle());
                        intent.putExtra("image", mInfo.getPhotos().get(0).getThumb_image());
                        intent.putExtra("discuse", mInfo.getContent());
                        intent.putExtra("topic_id", mInfo.getShare_id());
                        intent.putExtra("url", mInfo.getShare_url());
                        mContext.startActivity(intent);
                    }
                });
            }

            holder.disscuss_content_tv.setText(mInfo.getContent());
            if (AppSharedPreferencesHelper.getUserId().equals(mInfo.getUser_id())) {
                holder.disscuss_delete_iv.setVisibility(View.VISIBLE);
            } else {
                holder.disscuss_delete_iv.setVisibility(View.GONE);

            }
            holder.disscuss_delete_iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    disscussdeleteDialog(mInfo);
                }
            });
            if (mInfo.getIs_like().equals("1")) {
                holder.disscuss_like_iv.setImageResource(R.drawable.liked);
                holder.disscuss_like_tv.setTextColor(Color.parseColor("#e88439"));
            } else {
                holder.disscuss_like_iv.setImageResource(R.drawable.like);
                holder.disscuss_like_tv.setTextColor(Color.parseColor("#808080"));
            }

            holder.disscuss_like_iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if (mInfo.getIs_like().equals("1")) {
                        disscussulike(mInfo, holder.disscuss_like_tv);
                    } else {
                        disscusslike(mInfo, holder.disscuss_like_tv);
                    }
                }
            });
            holder.disscuss_like_tv.setText(mInfo.getLike());
            holder.disscuss_messages_iv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mContext, DisscussConentListActivity.class);
                    intent.putExtra("disscuss_id", mInfo.getDiscussion_id());
                    intent.putExtra("topic_id", topic_id);
                    mContext.startActivity(intent);
                }
            });
            holder.disscuss_messages_tv.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(mContext, DisscussConentListActivity.class);
                    intent.putExtra("disscuss_id", mInfo.getDiscussion_id());
                    intent.putExtra("topic_id", topic_id);
                    mContext.startActivity(intent);
                }
            });
            holder.disscuss_messages_tv.setText(mInfo.getComment_number());
            Log.i(tag, "mScreenWidth: " + mScreenWidth + " picwidth " + (mScreenWidth * 2 / 9 - DensityUtil.dip2px(mContext, 10)));
            int piccount = mInfo.getPhotos().size();

            int column = (int) (piccount / 3) + piccount % 3 == 0 ? 0 : 1 + 1;
            String model = android.os.Build.MODEL;
            String brand = android.os.Build.BRAND;
            Log.i(tag, "model:" + model + " brand: " + brand);
            if (model.contains("1SW")) {
                Log.i(tag, "contains 1sw");
                android.view.ViewGroup.MarginLayoutParams source = (MarginLayoutParams) holder.disscuss_pics_gridview.getLayoutParams();
                source.width = mScreenWidth * 2 / 3;
                source.height = mScreenWidth * 2 / 9 * column;
                source.bottomMargin = DensityUtil.dip2px(mContext, 10);
                source.topMargin = DensityUtil.dip2px(mContext, 10);
                holder.disscuss_pics_gridview.setLayoutParams(source);
                Log.i(tag, "setLayoutParams");
            } else {
                // // 4.4一下系统不能这样写
                // if (Integer.valueOf(android.os.Build.VERSION.SDK) > 19) {
                // RelativeLayout.LayoutParams source = (LayoutParams)
                // holder.disscuss_pics_gridview.getLayoutParams();
                // LayoutParams layoutparams = new LayoutParams(source);
                // layoutparams.width = mScreenWidth * 2 / 3;
                // layoutparams.height = mScreenWidth * 2 / 9 * column;
                // holder.disscuss_pics_gridview.setLayoutParams(layoutparams);
                // } else {
                RelativeLayout.LayoutParams source = (LayoutParams) holder.disscuss_pics_gridview.getLayoutParams();
                source.width = mScreenWidth * 2 / 3;
                source.height = mScreenWidth * 2 / 9 * column;
                holder.disscuss_pics_gridview.setLayoutParams(source);

                // }

            }
            final DisscussPhotoGridAdapter adapter = new DisscussPhotoGridAdapter(mContext, mInfo.getPhotos(), mScreenWidth * 2 / 9 - DensityUtil.dip2px(mContext, 10));
            holder.disscuss_pics_gridview.setAdapter(adapter);
            holder.disscuss_pics_gridview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle = new Bundle();
                    List<PhotoModel> photos = new ArrayList<PhotoModel>();
                    List<DisscussPhotosItems> list = mInfo.getPhotos();
                    for (int i = 0; i < list.size(); i++) {
                        photos.add(new PhotoModel(list.get(i).getImage()));
                    }
                    bundle.putSerializable("photos", (Serializable) photos);
                    bundle.putBoolean("ispicfromnet", true);
                    bundle.putInt(Preferences.TOPIC_PHOTO_ID, position);
                    CommonUtils.launchActivity(mContext, PhotoPreviewActivity.class, bundle);

                }
            });

            holder.disscuss_pics_gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, TopicReportPopActivity.class);
                    intent.putExtra("discussion_id", mInfo.getDiscussion_id());
                    intent.putExtra("type", "discussion");
                    mContext.startActivity(intent);
                    return true;
                }
            });
            holder.root.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mInfo.getShare_type().equals("0")) {
                        Intent intent = new Intent(mContext, TopicReportPopActivity.class);
                        intent.putExtra("discussion_id", mInfo.getDiscussion_id());
                        intent.putExtra("type", "discussion");
                        mContext.startActivity(intent);
                    }

                    return true;
                }
            });
            holder.disscuss_time_tv.setText(mInfo.getCreated_at());
            holder.disscuss_user_depart_tv.setText(mInfo.getStructure());
            holder.disscuss_user_name_tv.setText(mInfo.getNick_name());
            imageLoader.displayImage(mInfo.getAvatar(), holder.disscuss_useravator, options);
        }
        return convertView;
    }

    Dialog deletedisscuss = null;

    @SuppressLint("NewApi")
    protected void disscussdeleteDialog(final DisscussItems mInfo) {
        deletedisscuss = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确认删除这篇帖子？", "取消", "确认", new OnClickListener() {

            @SuppressWarnings("unused")
            @Override
            public void onClick(View v) {
                deletedisscuss.dismiss();
            }
        }, new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (deletedisscuss != null && deletedisscuss.isShowing()) {
                    deletedisscuss.dismiss();
                    disscussdelete(mInfo);
                }
            }
        }, mContext.getResources().getColor(R.color.common_dialog_btn_textcolor), mContext.getResources().getColor(R.color.common_dialog_btn_textcolor));

    }

    protected void disscussdelete(final DisscussItems mInfo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("discussion_id", mInfo.getDiscussion_id());
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        showProgress(0, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.DeleteDisscuss, params, new JsonHttpResponseHandler() {
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
                Toast.makeText(mContext, "删除失败，请稍后再试", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");// response.getJSONObject("content").toString());
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        if (deleteOnClickListener != null) {
                            deleteOnClickListener.delete(mInfo);
                        }
                        mList.remove(mInfo);
                        notifyDataSetChanged();
                        super.onSuccess(statusCode, headers, response);
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

    protected void disscussulike(final DisscussItems mInfo, final TextView disscuss_like_tv) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("discussion_id", mInfo.getDiscussion_id());
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        showProgress(0, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.UNLikeDisscuss, params, new JsonHttpResponseHandler() {
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
                Toast.makeText(mContext, "点赞失败，请稍后再试", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int code = response.getInt("code");
                    String message = response.getString("message");
                    Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: ");
                    if (code == 0) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                        if (likeOrUnLikeClickListener != null) {
                            likeOrUnLikeClickListener.delete(false, mInfo);
                        }
                        notifyDataSetChanged();
                        super.onSuccess(statusCode, headers, response);
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

    protected void disscusslike(final DisscussItems mInfo, final TextView disscuss_like_tv) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("discussion_id", mInfo.getDiscussion_id());
        params.put("access_token", SharePreferencesUtil.getToken(mContext));
        showProgress(0, true);
        Log.i(tag, "params: " + params.toString());
        DcareRestClient.volleyGet(Constants.LikeDisscuss, params, new FastJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, com.alibaba.fastjson.JSONObject response) {
                cancelmDialog();
                int code = response.getInteger("code");
                String message = response.getString("message");
                Log.i(tag, "onSuccess  code: " + code + " message: " + message + "content: " + response.getString("content"));
                if (code == 0) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    if (likeOrUnLikeClickListener != null) {
                        likeOrUnLikeClickListener.delete(true, mInfo);
                    }
                    notifyDataSetChanged();
                } else {// code不为0 发生异常
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] arg1, Throwable arg3, com.alibaba.fastjson.JSONObject jsonobject) {
                Log.e(tag, "OnFailure:" + this.getRequestURI() + " statusCode" + statusCode);
                cancelmDialog();
                Toast.makeText(mContext, "取消赞失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }
        });
    }

    private static class ViewHolder {
        TextView disscuss_time_tv, topic;
        CircleImageView disscuss_useravator;
        TextView disscuss_user_name_tv;
        TextView disscuss_user_depart_tv;
        TextView disscuss_content_tv;
        GridView disscuss_pics_gridview;
        TextView disscuss_like_tv;
        TextView disscuss_messages_tv, item_healthnews_title, item_healthnews_sumary;
        ImageView disscuss_messages_iv;
        ImageView disscuss_delete_iv;
        ImageView disscuss_like_iv, item_healthnews_image;
        RelativeLayout share_health_news, root;

    }

    public void showProgress(int resId, boolean cancel) {
        mProgress = new ProgressDialog(mContext);
        if (resId <= 0) {
            mProgress.setMessage(R.string.loading_data, cancel);
        } else {
            mProgress.setMessage(resId, cancel);
        }
        mProgress.show();
    }

    public void cancelmDialog() {
        if (mProgress != null) {
            mProgress.dismiss();
        }

    }
}

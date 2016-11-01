package com.etcomm.dcare.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.utils.StringUtils;
import com.etcomm.dcare.common.PointsRule;

public class DialogFactory {

    private static DialogFactory factory = null;

    public static synchronized DialogFactory getDialogFactory() {

        if (null == factory) {
            factory = new DialogFactory();
            return factory;
        } else {
            return factory;
        }

    }

    public Dialog showCationDialog(Context context, String msg, int resImageId) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_cation, null);
        final Dialog customDialog = new Dialog(context, R.style.DismissDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        if (msg != null) {
            TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
            tv_msg.setText("" + msg);
        }
        if (resImageId != 0) {
            ImageView iv_cation = (ImageView) view.findViewById(R.id.iv_cation);
            iv_cation.setImageResource(resImageId);
        }
        customDialog.show();
        return customDialog;
    }

    public Dialog showCommonDialog(Context context, String msg, String leftbtnStr, String rightbtnStr, android.view.View.OnClickListener leftBtnClickListener, android.view.View.OnClickListener rightBtnClickListener, int leftTextColor, int rightTextColor) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_ui, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView span_view = (TextView) view.findViewById(R.id.span_view);
        TextView msgtv = (TextView) view.findViewById(R.id.tv_msg);
        msgtv.setText(msg);
        // left btn
        TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        if (StringUtils.isEmpty(leftbtnStr)) {
            btnLeft.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnLeft.setText(leftbtnStr);
            if (leftBtnClickListener != null) {
                btnLeft.setOnClickListener(leftBtnClickListener);
            } else {
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.btn_right);
        if (StringUtils.isEmpty(rightbtnStr)) {
            btnRight.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnRight.setText(rightbtnStr);
            if (rightBtnClickListener != null) {
                btnRight.setOnClickListener(rightBtnClickListener);
            } else {
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }

        if (leftTextColor > 0) {
            btnLeft.setTextColor(context.getResources().getColor(leftTextColor));
        }
        if (rightTextColor > 0) {
            btnRight.setTextColor(context.getResources().getColor(rightTextColor));
        }

        customDialog.show();
        return customDialog;
    }

    public Dialog showCommonWithCancelableDialog(Context context, String msg, String leftbtnStr, String rightbtnStr, OnCancelListener onCancelListener, android.view.View.OnClickListener leftBtnClickListener, android.view.View.OnClickListener rightBtnClickListener, int leftTextColor, int rightTextColor) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_common_ui, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        customDialog.setOnCancelListener(onCancelListener);
        TextView span_view = (TextView) view.findViewById(R.id.span_view);
        TextView msgtv = (TextView) view.findViewById(R.id.tv_msg);
        msgtv.setText(msg);
        // left btn
        TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        if (StringUtils.isEmpty(leftbtnStr)) {
            btnLeft.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnLeft.setText(leftbtnStr);
            if (leftBtnClickListener != null) {
                btnLeft.setOnClickListener(leftBtnClickListener);
            } else {
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.btn_right);
        if (StringUtils.isEmpty(rightbtnStr)) {
            btnRight.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnRight.setText(rightbtnStr);
            if (rightBtnClickListener != null) {
                btnRight.setOnClickListener(rightBtnClickListener);
            } else {
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }

        if (leftTextColor > 0) {
            btnLeft.setTextColor(context.getResources().getColor(leftTextColor));
        }
        if (rightTextColor > 0) {
            btnRight.setTextColor(context.getResources().getColor(rightTextColor));
        }

        customDialog.show();
        return customDialog;
    }

    public Dialog showMustUpdateVersionDialog(Context context, String title, String updatecontent, String rightbtnStr, OnClickListener rightBtnClickListener, int rightTextColor) {
        // TODO Auto-generated method stub

        View view = LayoutInflater.from(context).inflate(R.layout.mustupdate_version, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(false);
        customDialog.setContentView(view);
        TextView span_view = (TextView) view.findViewById(R.id.update_title);
        TextView content = (TextView) view.findViewById(R.id.update_content);
        // TextView msgtv = (TextView) view.findViewById(R.id.tv_msg);
        span_view.setText(title);
        content.setText(updatecontent);
        customDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    customDialog.dismiss();
                    System.out.println("========dialog.dismiss()===========");
                    // 姝ゅ鎶奷ialog dismiss鎺夛紝鐒跺悗鎶婃湰韬殑activity finish鎺�
                    System.exit(0);
                    return true;
                } else {
                    return false;
                }
            }

        });
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.update_now);
        if (StringUtils.isEmpty(rightbtnStr)) {
            btnRight.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnRight.setText(rightbtnStr);
            if (rightBtnClickListener != null) {
                btnRight.setOnClickListener(rightBtnClickListener);
            } else {
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }

        if (rightTextColor > 0) {
            btnRight.setTextColor(context.getResources().getColor(rightTextColor));
        }

        customDialog.show();
        return customDialog;
    }

    public Dialog showUpdateVersionDialog(Context context, String title, String updatecontent, String leftbtnStr, String rightbtnStr, OnClickListener leftBtnClickListener, OnClickListener rightBtnClickListener, int leftTextColor, int rightTextColor) {
        // TODO Auto-generated method stub

        View view = LayoutInflater.from(context).inflate(R.layout.update_version, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView span_view = (TextView) view.findViewById(R.id.update_title);
        TextView content = (TextView) view.findViewById(R.id.update_content);
        span_view.setText(title);
        content.setText(updatecontent);
        TextView btnLeft = (TextView) view.findViewById(R.id.update_later);
        if (StringUtils.isEmpty(leftbtnStr)) {
            btnLeft.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnLeft.setText(leftbtnStr);
            if (leftBtnClickListener != null) {
                btnLeft.setOnClickListener(leftBtnClickListener);
            } else {
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }
        TextView btnRight = (TextView) view.findViewById(R.id.update_now);
        if (StringUtils.isEmpty(rightbtnStr)) {
            btnRight.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnRight.setText(rightbtnStr);
            if (rightBtnClickListener != null) {
                btnRight.setOnClickListener(rightBtnClickListener);
            } else {
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }

        if (leftTextColor > 0) {
            btnLeft.setTextColor(context.getResources().getColor(leftTextColor));
        }
        if (rightTextColor > 0) {
            btnRight.setTextColor(context.getResources().getColor(rightTextColor));
        }

        customDialog.show();
        return customDialog;
    }

    public Dialog showProgressDialog(Context context, String title, String btnStr, OnClickListener BtnClickListener, int rightTextColor) {
        // TODO Auto-generated method stub

        View view = LayoutInflater.from(context).inflate(R.layout.update_progress, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);

        // left btn
        TextView btnLeft = (TextView) view.findViewById(R.id.update_later);
        return customDialog;
    }

    public Dialog showCommentOptDialog(Context mContext, String upstr, String bottomstr, OnClickListener upBtnClickListener, OnClickListener bottomBtnClickListener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_commentopt_ui, null);
        final Dialog customDialog = new Dialog(mContext, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView btn_up = (TextView) view.findViewById(R.id.btn_up);
        btn_up.setText(upstr);
        if (upBtnClickListener != null) {
            btn_up.setOnClickListener(upBtnClickListener);
        } else {
            btn_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                }
            });
        }
        ImageView dialog_centerline = (ImageView) view.findViewById(R.id.dialog_centerline);
        TextView btn_bottom = (TextView) view.findViewById(R.id.btn_bottom);
        if (StringUtils.isEmpty(bottomstr)) {
            dialog_centerline.setVisibility(View.GONE);
            btn_bottom.setVisibility(View.GONE);
        } else {
            btn_bottom.setText(bottomstr);
            if (upBtnClickListener != null) {
                btn_bottom.setOnClickListener(bottomBtnClickListener);
            } else {
                btn_bottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }
        customDialog.show();
        return customDialog;
    }

    public Dialog showTipsDialog(Context mContext, String title, String button, String content) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_tips, null);
        final Dialog customDialog = new Dialog(mContext, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView tips_title = (TextView) view.findViewById(R.id.tips_title);
        tips_title.setText(title);
        TextView tips_content = (TextView) view.findViewById(R.id.tips_content);
        tips_content.setText(content);
        TextView tips_button = (TextView) view.findViewById(R.id.tips_button);
        tips_button.setText(button);
        tips_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                customDialog.dismiss();
            }
        });
        customDialog.show();
        return customDialog;
    }

    public Dialog showSignedDialog(Context mContext, String days, String score) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_signin, null);
        final Dialog customDialog = new Dialog(mContext, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        TextView tips_button = (TextView) view.findViewById(R.id.tips_button);
        tips_button.setText(PointsRule.getPointsByDay(days));
        TextView signin_content = (TextView) view.findViewById(R.id.signin_content);
        signin_content.setText(days);
        customDialog.show();
        return customDialog;

    }

    public Dialog showSettingDialog(Context context, String title, String msg, String leftbtnStr, String rightbtnStr, android.view.View.OnClickListener leftBtnClickListener, android.view.View.OnClickListener rightBtnClickListener, int leftTextColor, int rightTextColor) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_ui, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(false); // 点击其他区域关闭
        customDialog.setContentView(view);
        customDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // customDialog.dismiss();
                    // System.out.println("========dialog.dismiss()===========");
                    // //姝ゅ鎶奷ialog dismiss鎺夛紝鐒跺悗鎶婃湰韬殑activity finish鎺�
                    // System.exit(0);
                    return true;
                } else {
                    return false;
                }
            }

        });
        TextView span_view = (TextView) view.findViewById(R.id.span_view);
        TextView msgtv = (TextView) view.findViewById(R.id.tv_msg);
        TextView dialog_title = (TextView) view.findViewById(R.id.dialog_title);
        msgtv.setText(msg);
        dialog_title.setText(title);
        // left btn
        TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        if (StringUtils.isEmpty(leftbtnStr)) {
            btnLeft.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnLeft.setText(leftbtnStr);
            if (leftBtnClickListener != null) {
                btnLeft.setOnClickListener(leftBtnClickListener);
            } else {
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.btn_right);
        if (StringUtils.isEmpty(rightbtnStr)) {
            btnRight.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnRight.setText(rightbtnStr);
            if (rightBtnClickListener != null) {
                btnRight.setOnClickListener(rightBtnClickListener);
            } else {
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }

        if (leftTextColor > 0) {
            btnLeft.setTextColor(context.getResources().getColor(leftTextColor));
        }
        if (rightTextColor > 0) {
            btnRight.setTextColor(context.getResources().getColor(rightTextColor));
        }

        customDialog.show();
        return customDialog;
    }

    // 绑定序列号dialog
    public Dialog showbindDialog(Context context, String msg, String leftbtnStr, String rightbtnStr, android.view.View.OnClickListener leftBtnClickListener, android.view.View.OnClickListener rightBtnClickListener, int leftTextColor, int rightTextColor) {
        View view = LayoutInflater.from(context).inflate(R.layout.bindhc_activity, null);
        final Dialog customDialog = new Dialog(context, R.style.commonDialog);
        WindowManager.LayoutParams localLayoutParams = customDialog.getWindow().getAttributes();
        customDialog.onWindowAttributesChanged(localLayoutParams);
        customDialog.setCanceledOnTouchOutside(true);
        customDialog.setCancelable(true);
        customDialog.setContentView(view);
        customDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    // customDialog.dismiss();
                    // System.out.println("========dialog.dismiss()===========");
                    // //姝ゅ鎶奷ialog dismiss鎺夛紝鐒跺悗鎶婃湰韬殑activity finish鎺�
                    // System.exit(0);
                    return true;
                } else {
                    return false;
                }
            }

        });
        TextView span_view = (TextView) view.findViewById(R.id.span_view);
        TextView msgtv = (TextView) view.findViewById(R.id.tv_msg);
        msgtv.setText(msg);
        // left btn
        TextView btnLeft = (TextView) view.findViewById(R.id.btn_left);
        if (StringUtils.isEmpty(leftbtnStr)) {
            btnLeft.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnLeft.setText(leftbtnStr);
            if (leftBtnClickListener != null) {
                btnLeft.setOnClickListener(leftBtnClickListener);
            } else {
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }
        // right btn
        TextView btnRight = (TextView) view.findViewById(R.id.btn_right);
        if (StringUtils.isEmpty(rightbtnStr)) {
            btnRight.setVisibility(View.GONE);
            span_view.setVisibility(View.GONE);
        } else {
            btnRight.setText(rightbtnStr);
            if (rightBtnClickListener != null) {
                btnRight.setOnClickListener(rightBtnClickListener);
            } else {
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
            }
        }

        if (leftTextColor > 0) {
            btnLeft.setTextColor(context.getResources().getColor(leftTextColor));
        }
        if (rightTextColor > 0) {
            btnRight.setTextColor(context.getResources().getColor(rightTextColor));
        }

        customDialog.show();
        return customDialog;
    }

}

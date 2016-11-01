package com.etcomm.dcare.app.widget;


import com.etcomm.dcare.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 
 * @ClassName: CustomProgressDialog 
 * @Description: 自定义Dialog
 * @author etc 
 * @date 7 Apr, 2016 2:17:39 PM
 */
public class ProgressDialog extends Dialog {


    public ProgressDialog(Context context) {
        super(context ,R.style.m_dialog);
        init();
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    public void init(){
        this.setContentView(R.layout.progress_dialog);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
    }

    public ProgressDialog setMessage(String strMessage, boolean cancel) {
        this.setCancelable(cancel);//按返回键能否消失
        this.setCanceledOnTouchOutside(false);//点击其它区域不关闭
        TextView tvMsg = (TextView) this.findViewById(R.id.text);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return this;
    }

    public ProgressDialog setMessage(int resid, boolean cancel) {
        this.setCancelable(cancel);
        this.setCanceledOnTouchOutside(false);
        TextView tvMsg = (TextView) this.findViewById(R.id.text);
        if (tvMsg != null) {
            tvMsg.setText(resid);
        }
        return this;
    }
}

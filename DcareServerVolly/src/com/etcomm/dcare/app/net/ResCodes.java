package com.etcomm.dcare.app.net;

import com.etcomm.dcare.R;
import com.etcomm.dcare.app.common.DcareApplication;

import android.content.Context;

/**
 * @ClassName: ResCode 
 * @Description: 网络请求错误码
 * @author etc 慢慢添加
 * @date 13 Apr, 2016 11:27:42 AM
 */
public class ResCodes {
	public static final int Success = 0;
    public static final int Error_Code = 2;
    public static String getTipString(int status) {
        Context con = DcareApplication.mCon;
        String res = con.getString(R.string.network_error);

        return res;
    }
}

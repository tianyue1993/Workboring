package com.etcomm.dcare.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import android.util.Log;
import cz.msebera.android.httpclient.Header;

@SuppressWarnings("deprecation")
public abstract class FastJsonHttpResponseHandler extends AsyncHttpResponseHandler {
	private static final String tag = "FastJsonHttpResponseHandler";

	// @Override
	// public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable
	// arg3) {
	// // TODO Auto-generated method stub
	// if (arg2 == null) {
	// onFailure(arg0, arg1, arg3, null);
	// return;
	// // throw new NullPointerException("没有返回数据");
	// }
	// Log.e(tag, "arg2: " + arg2.length + " " + arg2.toString());
	// String str = new String(arg2);
	// Log.e(tag, "str: " + str);
	// JSONObject jsonobject = JSON.parseObject(str);
	// onFailure(arg0, arg1, arg3, jsonobject);
	// }
	//
	// @Override
	// public void onSuccess(int arg0, org.apache.http.Header[] arg1, byte[]
	// arg2) {
	// // TODO Auto-generated method stub
	// String str = new String(arg2);
	// JSONObject jsonobject = JSON.parseObject(str);
	// onSuccess(arg0, arg1, jsonobject);
	// }
	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		if (arg2 == null) {
			onFailure(arg0, arg1, arg3, null);
			return;
			// throw new NullPointerException("没有返回数据");
		}
		Log.e(tag, "arg2: " + arg2.length + " " + arg2.toString());
		String str = new String(arg2);
		Log.e(tag, "str: " + str);
		JSONObject jsonobject = JSON.parseObject(str);
		onFailure(arg0, arg1, arg3, jsonobject);

	}

	public abstract void onFailure(int arg0, Header[] arg1, Throwable arg3, JSONObject jsonobject);

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
		String str = new String(arg2);
		JSONObject jsonobject = JSON.parseObject(str);
		onSuccess(arg0, arg1, jsonobject);
	}

	public abstract void onSuccess(int statusCode, Header[] headers, JSONObject response);

}
// public class fjson extends BaseJsonHttpResponseHandler{
//
// @Override
// public void onFailure(int arg0, Header[] arg1, Throwable arg2, String arg3,
// Object arg4) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// public void onSuccess(int arg0, Header[] arg1, String arg2, Object arg3) {
// // TODO Auto-generated method stub
//
// }
//
// @Override
// protected Object parseResponse(String arg0, boolean arg1) throws Throwable {
// // TODO Auto-generated method stub
// return null;
// }
//
// }

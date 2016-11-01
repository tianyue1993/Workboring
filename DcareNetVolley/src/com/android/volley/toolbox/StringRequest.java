/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.volley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.cookie.SetCookie;

/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class StringRequest extends Request<String> {
	//增、删、改等操作超时时间
	public static final int CUD_SOCKET_TIMEOUT = 6000;
	//查询超时时间
	public static final int R_SOCKET_TIMEOUT = 5000;
	//最大重试请求次数
	public static final int MAX_RETRIES = 5;
	private static final String tag = "StringRequest";
	private final Listener<String> mListener;
	private String mHeader;
	private static String cookieFromResponse;
	private static Map<String, String> sendHeader = new HashMap<String, String>();

	/**
	 * Creates a new request with the given method.
	 *
	 * @param method the request {@link Method} to use
	 * @param url URL to fetch the string at
	 * @param listener Listener to receive the String response
	 * @param errorListener Error listener, or null to ignore errors
	 */
	public StringRequest(int method, String url, Listener<String> listener,
						 ErrorListener errorListener) {
		super(method, url, errorListener);
		setRetryPolicy(new DefaultRetryPolicy(CUD_SOCKET_TIMEOUT,
				MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mListener = listener;
	}

	/**
	 * Creates a new GET request.
	 *
	 * @param url URL to fetch the string at
	 * @param listener Listener to receive the String response
	 * @param errorListener Error listener, or null to ignore errors
	 */
	public StringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
		this(Method.GET, url, listener, errorListener);
	}
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// TODO Auto-generated method stub
		if(cookieFromResponse!=null&&!cookieFromResponse.equals("")){
//    		if(!sendHeader.containsKey("Cookie")){
			Log.i(tag, "Cookie:  "+cookieFromResponse);
			sendHeader.put("Cookie", cookieFromResponse);
//    		}
		}else{
			Log.i(tag, "Cookie: cookieFromResponse null ");
		}
		return sendHeader;
	}
	public void SetCookie(){
		sendHeader.put("Cookie", cookieFromResponse);
	}
	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		mHeader = response.headers.toString();
		Log.w(tag,"get headers in parseNetworkResponse "+response.headers.toString());
		//使用正则表达式从reponse的头中提取cookie内容的子串
		Pattern pattern=Pattern.compile("Set-Cookie.*?;");
		Matcher m=pattern.matcher(mHeader);
		if(m.find()){
			cookieFromResponse =m.group();
			Log.w(tag,"cookie from server "+ cookieFromResponse);
		}
		//去掉cookie末尾的分号
		if(cookieFromResponse!=null&&cookieFromResponse.length()>11){
			cookieFromResponse = cookieFromResponse.substring(11,cookieFromResponse.length()-1);
			Log.w(tag,"cookie substring "+ cookieFromResponse);
			SetCookie();
		}
		return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
	}

	public String getmHeader() {
		return mHeader;
	}

	public void setmHeader(String mHeader) {
		this.mHeader = mHeader;
	}

	public static String getCookieFromResponse() {
		return cookieFromResponse;
	}

	public static void setCookieFromResponse(String cookieFromResponse) {
		StringRequest.cookieFromResponse = cookieFromResponse;
	}

	public Map<String, String> getSendHeader() {
		SetCookie();
		Log.i(tag, "getSendHeader");
		return sendHeader;
	}

	public void setSendHeader(Map<String, String> sendHeader) {
		this.sendHeader = sendHeader;
	}

	public Listener<String> getmListener() {
		return mListener;
	}


}

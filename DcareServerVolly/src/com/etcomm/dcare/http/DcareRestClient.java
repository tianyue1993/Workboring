package com.etcomm.dcare.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.etcomm.dcare.app.common.config.Constants;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.client.CloseableHttpClient;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class DcareRestClient {

    private static final String tag = "DcareRestClient";
    private static AsyncHttpClient client;

    static {
        client = new AsyncHttpClient();
        Log.e(tag, "new AsyncHttpClient");
        client.setTimeout(5 * 1000);
        client.setMaxConnections(2);

    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    public static String getAbsoluteUrl(String relativeUrl) {
        return Constants.BASE_URL + relativeUrl;
    }

    public static void post(String url, String accessToken, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url + "&access_token=" + accessToken, params, responseHandler);

    }

    public static void volleyPost(String url, String accessToken, final Map<String, String> params, AsyncHttpResponseHandler responseHandler) {
        volleyPost(url + "&access_token=" + accessToken, params, responseHandler);

    }

    private static RequestQueue mQueue;

    public static void initVolley(Context mContext) {
        // @SuppressWarnings("deprecation")
        // DefaultHttpClient httpclient = new DefaultHttpClient();
        CloseableHttpClient client = HttpClientBuilder.create().build();
        // CookieStore cookieStore = new BasicCookieStore();
        // httpclient.setCookieStore( cookieStore );
        // HttpStack httpStack = new HttpClientStack( client );
        mQueue = Volley.newRequestQueue(mContext);
    }

    public static void volleyGet(String url, final Map<String, String> params, final AsyncHttpResponseHandler responseHandler) {
        StringRequest strRequest = new StringRequest(Method.GET, url + getGETParams(params), new Listener<String>() {
            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                Log.d("TAG", response.toString());
                Header[] headers = null;
                responseHandler.onSuccess(0, headers, response.toString().getBytes());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("TAG", error.getMessage(), error);
                Header[] headers = null;
                responseHandler.onFailure(404, headers, null, new Throwable());
            }
        });
        strRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(strRequest);
    }

    /**
     * Default encoding for POST or PUT parameters. See
     * {@link #getParamsEncoding()}.
     */
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    private static String getGETParams(Map<String, String> params) {
        // TODO Auto-generated method stub
        StringBuilder encodedParams = new StringBuilder("&");
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                encodedParams.append(URLEncoder.encode(entry.getKey(), DEFAULT_PARAMS_ENCODING));
                encodedParams.append('=');
                encodedParams.append(URLEncoder.encode(entry.getValue() == null ? "" : entry.getValue(), DEFAULT_PARAMS_ENCODING));
                encodedParams.append('&');
            }
            encodedParams.append("d=" + Math.random());
            Log.i(tag, "encodedParams: " + encodedParams.toString());
            encodedParams.deleteCharAt(encodedParams.length() - 1);
            return encodedParams.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("Encoding not supported: " + DEFAULT_PARAMS_ENCODING, uee);
        }
    }

    public static void volleyPost(String url, final Map<String, String> params, final AsyncHttpResponseHandler responseHandler) {
        StringRequest strRequest = new StringRequest(Method.POST, url, new Listener<String>() {

            @Override
            public void onResponse(String response) {
                // TODO Auto-generated method stub
                Log.d("TAG", response.toString());
                Header[] headers = null;
                responseHandler.onSuccess(0, headers, response.toString().getBytes());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("TAG", error.getMessage(), error);
                Header[] headers = null;
                responseHandler.onFailure(404, headers, null, new Throwable());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // TODO Auto-generated method stub
                Log.e("TAG", "params: " + params.toString());
                return (Map<String, String>) params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // TODO Auto-generated method stub
                return getSendHeader();
            }
        };
        strRequest.SetCookie();
        strRequest.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(strRequest);
    }
}

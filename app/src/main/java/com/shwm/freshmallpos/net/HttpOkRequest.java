package com.shwm.freshmallpos.net;

import android.os.RecoverySystem;
import android.text.TextUtils;

import com.shwm.freshmallpos.R;
import com.shwm.freshmallpos.base.ApplicationMy;
import com.shwm.freshmallpos.inter.IHttpRequest;
import com.shwm.freshmallpos.util.ExceptionUtil;
import com.shwm.freshmallpos.util.NetUtil;
import com.shwm.freshmallpos.util.UL;
import com.shwm.freshmallpos.util.UtilSPF;
import com.shwm.freshmallpos.value.ValueKey;
import com.shwm.freshmallpos.value.ValueStatu;
import com.shwm.freshmallpos.value.ValueType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody.Builder;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * OkHttpClient网络请求
 */
public class HttpOkRequest implements IHttpRequest {
    private static final String TAG = "HttpOkRequest";
    private static final int HttpPost = 1;
    private static final int HttpGet = 2;
    public static Exception exception;
    public static int statuException;
    public static int typeH = ValueType.HttpType_Default;// 注册为 ValueTypeUtil.HttpType_regin
    private OkHttpClient mOkHttpClient;
    private Request request;
    private RequestBody requestBody;
    private Response response;

    /** 异步和同步post */
    public HashMap<String, Object> requestByPostOrGet(int type, String url, HashMap<String, Object> map) {
        this.mOkHttpClient = ApplicationMy.getInstance().getOkHttpClient();
        HashMap<String, Object> hashmapResult = new HashMap<String, Object>();
        Builder builder = new Builder();
        // 遍历map中所有参数到builder
        String urlAllGet = url;
        if (map != null) {
            Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, Object> entry = iterator.next();
                String key = entry.getKey();
                String value = String.valueOf(entry.getValue());
                builder.add(key, value);
                urlAllGet = urlAllGet + "&" + key + "=" + value;
            }
        }
        UL.i(TAG + " / " + (type == HttpPost ? "post" : "get"), urlAllGet);
        // 构建请求体
        requestBody = builder.build();
        // 构建请求
        if (type == HttpPost) {
            if (typeH == ValueType.HttpType_Default) {
                request = new Request.Builder().url(url)// 地址 Post 用url
                        .post(requestBody)// 添加请求体
                        .build();
            }
            if (typeH == ValueType.HttpType_Regin) {// 注册 添加cookie
                request = new Request.Builder().url(url).post(requestBody)
                        .addHeader("Cookie", "JSESSIONID=" + UtilSPF.getString("cookie", "")).build();
                UL.d("cookie", UtilSPF.getString("cookie", ""));
            }
            type = ValueType.HttpType_Default;
        }
        if (type == HttpGet) {
            CacheControl.Builder builderCache = new CacheControl.Builder();
            if (NetUtil.isNetworkAvailable(ApplicationMy.getContext())) {
//				UL.d(TAG, "有网不使用缓存");
                builderCache.noCache();// 有网不使用缓存，全部走网络
            } else {
                UL.d(TAG, "没有网使用缓存");
                builderCache.onlyIfCached();
            }
            CacheControl cache = builderCache.build();
            request = new Request.Builder().cacheControl(cache).url(urlAllGet)// 地址 get 用拼接的urlAllGet
                    .build();
        }
        try {
            response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                hashmapResult.put(ValueKey.HTTP_STATU, ValueStatu.REQUEST_SUCCESS);
                hashmapResult.put(ValueKey.HTTP_RESUTL, result);
                statuException = ValueStatu.REQUEST_SUCCESS;
                exception = new ExceptionUtil(ApplicationMy.getStringRes(R.string.success));
                UL.i(TAG + "  SUCCESS", result);
                return hashmapResult;

            } else {
                hashmapResult.put(ValueKey.HTTP_STATU, ValueStatu.REQUEST_FAIL);
                hashmapResult.put(ValueKey.HTTP_FAILINFO, response.code() + "");
                statuException = ValueStatu.REQUEST_FAIL;
                exception = new ExceptionUtil(response.code() + "");
                UL.e(TAG + "  FAIL", response.code() + "");
                return hashmapResult;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            statuException = ValueStatu.REQUEST_Execute;
            exception = e;
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                UL.i(TAG + "  Execute", e.getMessage());
            }
            hashmapResult.put(ValueKey.HTTP_STATU, statuException);
            hashmapResult.put(ValueKey.HTTP_FAILINFO, e.getMessage());
            return hashmapResult;
        }
    }

    @Override
    public HashMap<String, Object> requestByPost(String url, HashMap<String, Object> map) {
        // TODO Auto-generated method stub
        return requestByPostOrGet(HttpPost, url, map);
    }

    @Override
    public HashMap<String, Object> requestByGet(String url, HashMap<String, Object> map) {
        // TODO Auto-generated method stub
        return requestByPostOrGet(HttpGet, url, map);
    }

    /**
     * @return null
     */
    @Override
    public HashMap<String, Object> requestByPost(String url, String xml) {
        // TODO Auto-generated method stub
        return new HttpUrlRequest().requestByPost(url, xml);
    }

    @Override
    public HashMap<String, Object> requestByGet(String url) {
        // TODO Auto-generated method stub
        return requestByPostOrGet(HttpGet, url, null);
    }

    public void downFile(String url, final File saveFile, final ProgressResponseBody.ProgressListener progressListener) {
        // 添加拦截器，在 OkHttpClient build时添加
        Request request = new Request.Builder().url(url).tag(url).build();
        OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressResponseBody(originalResponse.body(), progressListener)).build();
            }
        }).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO Auto-generated method stub
                FileUtil.inputStream2File(response.body().byteStream(), saveFile);
            }

            @Override
            public void onFailure(Call call, IOException exception) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 取消任务
     * 这里tag 我设置的是url
     * @param tag url
     */
    public void cancelByTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }

        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

}

package com.example.fe.myapplication.network;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;


/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
public class HttpExceptionInterceptor implements Interceptor {

    private final String TAG = "HttpExceptionInterceptor";

    private final Charset UTF8 = StandardCharsets.UTF_8;
    private final String jsonStart = "{";
    private final String errorCode = "errorCode";
    private final String statusCode = "statusCode";
    private final String comments = "comments";

    @NotNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);
        try {
            // 拦截了响应体
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful()) {
                LogUtil.e("code:" + response.code() + ", message:" + response.message());
                return response;
            }
            if (HttpHeaders.hasBody(response)) {
                String result = getResponseString(responseBody);
                String comments = getErrorComments(result);
                if (!TextUtils.isEmpty(comments)) {
                    LogUtil.e(comments);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return response;
    }

    private String getResponseString(@Nullable ResponseBody responseBody) throws IOException {
        if (responseBody != null) {
            BufferedSource source = responseBody.source();
            // Buffer the entire body.
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.buffer();
            return buffer.clone().readString(UTF8);
        }
        return "";
    }

    private String getErrorComments(String bodyString) {
        try {
            if (bodyString.startsWith(jsonStart)) {
                JSONObject json = new JSONObject(bodyString);
                int statusCode = json.optInt(this.statusCode);
                if (statusCode != HttpStatusCode.INSTANCE.getSUCCESS_OK()) {
                    return json.optString(this.comments);
                }
                return "";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
    }

}

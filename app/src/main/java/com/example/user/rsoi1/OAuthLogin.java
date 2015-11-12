package com.example.user.rsoi1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;


public class OAuthLogin extends Activity {

    private static final String TAG = "OAuthLogin";

    public static final String AUTH_URL = "https://api.vimeo.com/oauth/authorize";
    public static final String CLIENT_ID = "f8a95c802457818040ead70324528442c410f882";
    public static final String CLIENT_SECRET = "kfLUVS7TzzjPCuD0oNAnlykq7CxLn4BEVt+w/PCQUiyD8KeXOogcdX438EHIo2KDfx2426B6RmJHBxDa7pVq41VJbd570whFRYtUMk8DMUyKReLMNaaDl4SxfP70XK3v";
    public static final String REDIRECT_URI = "http://localhost/";
    public static final String TOKEN_URL = "https://api.vimeo.com/oauth/access_token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                try {
                    PostRequest postRequest = new PostRequest();

                    if (url.startsWith(REDIRECT_URI)) {
                        Uri uri = Uri.parse(url);
                        String code = uri.getQueryParameter("code");
                        String state = uri.getQueryParameter("state");
                        Log.d(TAG, "state: " + state + ", code: " + code);
                        if (state.equals("ololo")) {
                            String auth = getBasicAuth(CLIENT_ID, CLIENT_SECRET);
                            postRequest.execute(TOKEN_URL, "POST", auth, createTokenRequest(code));
                        }
                        Intent intent = new Intent();
                        intent.putExtra("ACCESS_TOKEN", postRequest.get());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return;
                }
            }
        });
        webView.loadUrl(createCodeRequest());

    }

    public static String createCodeRequest() {
        try {
            String url = AUTH_URL;
            url += "?response_type=code&client_id=";
            url += URLEncoder.encode(CLIENT_ID, "UTF-8");
            url += "&state=ololo";
            url += "&redirect_uri=";
            url += URLEncoder.encode(REDIRECT_URI, "UTF-8");
            Log.d(TAG, "url: " + url);
            return url;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    };

    public static String createTokenRequest(String code)
    {
        try {
            String request = "grant_type=authorization_code";
            request += "&code=";
            request += URLEncoder.encode(code, "UTF-8");
            request += "&redirect_uri=";
            request += URLEncoder.encode(REDIRECT_URI, "UTF-8");
            Log.d(TAG, "request: " + request);
            return request;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private String getBasicAuth(String clientID, String clientSecret)
    {
        String forBase64 = clientID + ":" + clientSecret;
        String basicAuth = new String(Base64.encode(forBase64.getBytes(), Base64.NO_WRAP));
        return "basic " + basicAuth;
    }
}

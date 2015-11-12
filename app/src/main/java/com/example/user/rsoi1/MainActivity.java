package com.example.user.rsoi1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

public class MainActivity extends Activity {
    static final String TAG = "MainActivity";

    static final int AUTH = 0;

    String authCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onAuthLogin(View view)
    {
        Intent intent = new Intent(this, OAuthLogin.class);
        startActivityForResult(intent, AUTH);
    }

    public void onUARequest(View view)
    {
        try {
            Intent intent = new Intent(this, ViewResponse.class);
            PostRequest postRequest = new PostRequest();
            postRequest.execute("https://api.vimeo.com/", "GET", "", "");
            intent.putExtra("RESPONSE", postRequest.get());
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
    }

    public void onARequest(View view)
    {

        try
        {
            Intent intent = new Intent(this, ViewResponse.class);
            PostRequest postRequest = new PostRequest();
            postRequest.execute("https://api.vimeo.com/me", "GET", "bearer " + authCode, "");
            intent.putExtra("RESPONSE", postRequest.get());
            startActivity(intent);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTH)
        {
            if (resultCode == RESULT_OK)
            {
                String json = data.getStringExtra("ACCESS_TOKEN");
                authCode = getAccessToken(json);
                Intent intent = new Intent(this, ViewResponse.class);
                intent.putExtra("RESPONSE", json);
                startActivity(intent);
            }
        }
    }

    private String getAccessToken(String json)
    {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String result = jsonObject.getString("access_token");
            Log.d(TAG, "accessToken from json: " + result);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

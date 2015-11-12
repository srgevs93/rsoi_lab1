package com.example.user.rsoi1;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by user on 19/10/15.
 */
public class PostRequest extends AsyncTask <String, Void, String> {
    private static final String TAG = "PostRequest";

    @Override
    protected String doInBackground(String ... params)
    {
        String address = params[0];
        Log.d(TAG, "address: " + address);
        String method = params[1];
        Log.d(TAG, "method: " + method);
        String authorization = params[2];
        Log.d(TAG, "authorization: " + authorization);
        String data = params[3];
        Log.d(TAG, "data: " + data);

        try {
            InputStream inputStream;
            URL url = new URL(address);
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(method);
            httpURLConnection.setDoOutput(false);
            httpURLConnection.setRequestProperty("Authorization", authorization);
            httpURLConnection.setRequestProperty("Accept", "*/*");
            if (method.equals("POST"))
            {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection.getOutputStream().write(data.getBytes());
            }
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

            }
            else
            {
                inputStream = new BufferedInputStream(httpURLConnection.getErrorStream());
            }
            String response = convertStreamToString(inputStream);
            Log.d(TAG, "response: " + response);
            return response;
        }
        catch (Exception E)
        {
            E.printStackTrace();
            return null;
        }
    }

    private static String convertStreamToString(InputStream is)
    {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String result = new String();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return result;
    }
}

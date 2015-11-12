package com.example.user.rsoi1;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.json.JSONObject;

public class ViewResponse extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_response);
        TextView textView = (TextView) findViewById(R.id.textView1);
        Intent intent = getIntent();
        String json = intent.getStringExtra("RESPONSE");
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(parseJSON(json));
    }

    private String parseJSON(String input)
    {
        try {
            JSONObject jsonObject = new JSONObject(input);
            return jsonObject.toString(4);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }

}

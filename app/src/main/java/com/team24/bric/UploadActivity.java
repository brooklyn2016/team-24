package com.team24_jpm.bric;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class UploadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button button = (Button) findViewById(R.id.upload_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int reqCode = 1;
                Intent action = new Intent(Intent.ACTION_GET_CONTENT);
                action = action.setType("file/*").addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(action, reqCode);

                // Runs immediately, causing getPath to run on a Null object. Fix

            }


        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String s = data.getData().getPath();
            Log.d("Upload Activity", s);
        }

    }
}

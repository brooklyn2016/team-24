package com.team24_jpm.bric;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

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
            }


        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            String s = data.getData().getPath();
            //Log.d("Upload Activity", s);
            addtoFireBase(s);
        }


    }

    private void addtoFireBase(String s) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReferenceFromUrl("gs://bric-dd7cd.appspot.com");
        StorageReference mountainsRef = storageRef.child("default.prop");
        Uri file = Uri.fromFile(new File("/default.prop"));
        StorageReference riversRef = storageRef.child("default.prop");
        UploadTask uploadTask = riversRef.putFile(file);
        uploadTask = storageRef.child("default.prop").putFile(file);

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }
}

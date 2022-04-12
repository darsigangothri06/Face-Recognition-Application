package com.example.faceDetectionLGMVIPTaskTwo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.droidsonroids.gif.GifImageView;

public class startActivity extends AppCompatActivity {
    GifImageView GiffyView;
    TextView TextViewInStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        // binding with the UI buttons
        GiffyView =findViewById(R.id.gif_button);
        TextViewInStart = findViewById(R.id.textView);

        // When the TextView is clicked, it should go to Main Activity
        TextViewInStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(startActivity.this,MainActivity.class));
            }
        });

        // When GiffyView is Clicked, It should go to Main Activity
        GiffyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(startActivity.this,MainActivity.class));
            }
        });
    }
}